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
import br.com.alexfarma.sgq.api.model.Departamento;
import br.com.alexfarma.sgq.api.repository.DepartamentoRepository;
import br.com.alexfarma.sgq.api.repository.filter.DepartamentoFilter;
import br.com.alexfarma.sgq.api.service.DepartamentoService;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoResource {

	@Autowired
	private DepartamentoRepository departamentoRepository;

	@Autowired
	private DepartamentoService departamentoService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping(params = "listarTodos")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<Departamento> listar() {
		return departamentoRepository.findAll();
	}

	@GetMapping(params = "listarAtivos")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<Departamento> listarAtivos() {
		return departamentoRepository.findByAtivoTrue();
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_DEPARTAMENTO') and #oauth2.hasScope('read')")
	public Page<Departamento> pesquisar(DepartamentoFilter filtros, Pageable pageable) {
		return departamentoRepository.filtrar(filtros, pageable);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_DEPARTAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Departamento> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Departamento> dpto = departamentoRepository.findById(codigo);
		return dpto.isPresent() ? ResponseEntity.ok(dpto.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_DEPARTAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Departamento> criar(@Valid @RequestBody Departamento dpto, HttpServletResponse response) {
		Departamento dptoSalvo = departamentoRepository.save(dpto);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, dptoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(dptoSalvo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_FUNCIONARIO') and #oauth2.hasScope('write')")
	public ResponseEntity<Departamento> atualizar(@PathVariable("codigo") Long codigo, @Valid @RequestBody Departamento dpto) {
		Departamento dptoSalvo = departamentoService.atualizar(codigo, dpto);
		return ResponseEntity.ok(dptoSalvo);
	}

	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_DEPARTAMENTO') and #oauth2.hasScope('write')")
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		departamentoService.atualizarPropriedadeAtivo(codigo, ativo);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_DEPARTAMENTO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		departamentoRepository.deleteById(codigo);
	}

}
