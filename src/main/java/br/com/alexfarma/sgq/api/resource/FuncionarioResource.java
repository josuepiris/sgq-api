package br.com.alexfarma.sgq.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alexfarma.sgq.api.event.RecursoCriadoEvent;
import br.com.alexfarma.sgq.api.model.Funcionario;
import br.com.alexfarma.sgq.api.model.Permissao;
import br.com.alexfarma.sgq.api.model.Usuario;
import br.com.alexfarma.sgq.api.repository.FuncionarioRepository;
import br.com.alexfarma.sgq.api.repository.PermissaoRepository;
import br.com.alexfarma.sgq.api.repository.filter.FuncionarioFilter;
import br.com.alexfarma.sgq.api.service.FuncionarioService;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioResource {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private PermissaoRepository permissaoRepository;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping(params = "todos")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<Funcionario> listarFuncionarios() {
		return funcionarioRepository.findAll();
	}
	
	@GetMapping(params = "ativos")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<Funcionario> listarAtivos() {
		return funcionarioRepository.findByAtivoTrue();
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_FUNCIONARIO') and #oauth2.hasScope('read')")
	public Page<Funcionario> pesquisar(FuncionarioFilter filtros, Pageable pageable) {
		return funcionarioRepository.filtrar(filtros, pageable);
	}

	@GetMapping("/{funcionarioId}")
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_FUNCIONARIO') and #oauth2.hasScope('read')")
	public ResponseEntity<Funcionario> buscarPeloCodigo(@PathVariable Long funcionarioId) {
		Optional<Funcionario> funcionario = funcionarioRepository.findById(funcionarioId);
		return funcionario.isPresent() ? ResponseEntity.ok(funcionario.get()) : ResponseEntity.notFound().build();
	}

	@GetMapping("/permissoes")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_FUNCIONARIO') and #oauth2.hasScope('read')")
	public List<Permissao> listarPermissoes() {
		return permissaoRepository.findAll();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_FUNCIONARIO') and #oauth2.hasScope('write')")
	public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario, HttpServletResponse response) {
		Usuario usuarioSalvo = funcionarioService.criar(usuario);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, usuarioSalvo.getFuncionarioId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_FUNCIONARIO') and #oauth2.hasScope('write')")
	public ResponseEntity<Usuario> atualizar(@PathVariable("codigo") Long funcionarioId, @Valid @RequestBody Usuario usuario) {
		Usuario usuarioSalvo = funcionarioService.atualizar(funcionarioId, usuario);
		return ResponseEntity.ok(usuarioSalvo);
	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_FUNCIONARIO') and #oauth2.hasScope('write')")
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		funcionarioService.atualizarPropriedadeAtivo(codigo, ativo);
	}

	@PutMapping("/{codigo}/permissoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_FUNCIONARIO') and #oauth2.hasScope('write')")
	public void atualizarListaPermissoes(@PathVariable Long codigo, @RequestBody List<Permissao> permissoes) {
		funcionarioService.atualizarListaPermissoes(codigo, permissoes);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_FUNCIONARIO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		funcionarioRepository.deleteById(codigo);
	}

}
