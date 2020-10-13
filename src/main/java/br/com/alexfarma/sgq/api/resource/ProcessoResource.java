package br.com.alexfarma.sgq.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
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
import br.com.alexfarma.sgq.api.model.Processo;
import br.com.alexfarma.sgq.api.repository.ProcessoRepository;
import br.com.alexfarma.sgq.api.repository.filter.ProcessoFilter;

@RestController
@RequestMapping("/processos")
public class ProcessoResource {

	@Autowired
	private ProcessoRepository processoRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping(params = "listarTodos")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<Processo> listar() {
		return processoRepository.findAll();
	}

	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_PROCESSO') and #oauth2.hasScope('read')")
	public Page<Processo> pesquisar(ProcessoFilter filtros, Pageable pageable) {
		return processoRepository.filtrar(filtros, pageable);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_PROCESSO') and #oauth2.hasScope('read')")
	public ResponseEntity<Processo> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Processo> processo = processoRepository.findById(codigo);
		return processo.isPresent() ? ResponseEntity.ok(processo.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PROCESSO') and #oauth2.hasScope('write')")
	public ResponseEntity<Processo> criar(@Valid @RequestBody Processo processo, HttpServletResponse response) {
		Processo processoSalvo = processoRepository.save(processo);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, processoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(processoSalvo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_PROCESSO') and #oauth2.hasScope('write')")
	public ResponseEntity<Processo> atualizar(@PathVariable("codigo") Long codigo, @Valid @RequestBody Processo processo) {
		Processo processoSalvo = processoRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(processo, processoSalvo, "codigo");
		Processo novoProcesso = processoRepository.save(processoSalvo);
		return ResponseEntity.ok(novoProcesso);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PROCESSO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		processoRepository.deleteById(codigo);
	}

}
