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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alexfarma.sgq.api.event.RecursoCriadoEvent;
import br.com.alexfarma.sgq.api.model.MotivoRetrabalho;
import br.com.alexfarma.sgq.api.repository.MotivoRetrabalhoRepository;
import br.com.alexfarma.sgq.api.repository.filter.MotivoRetrabalhoFilter;

@RestController
@RequestMapping("/retrabalhos/motivos")
public class MotivoRetrabalhoResource {

	@Autowired
	private MotivoRetrabalhoRepository motivoRetrabalhoRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping(params = { "page", "size", "processo" })
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_MOTIVO_RETRABALHO') and #oauth2.hasScope('read')")
	public Page<MotivoRetrabalho> pesquisar(Pageable pageable, MotivoRetrabalhoFilter filtros) {
		return motivoRetrabalhoRepository.filtrar(filtros, pageable);
	}

	@GetMapping(params = "processo")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<MotivoRetrabalho> buscarMotivoPorProcesso(@RequestParam Long processo) {
		return motivoRetrabalhoRepository.findByProcessoCodigo(processo);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_MOTIVO_RETRABALHO') and #oauth2.hasScope('read')")
	public ResponseEntity<MotivoRetrabalho> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<MotivoRetrabalho> motivoRetrabalho = motivoRetrabalhoRepository.findById(codigo);
		return motivoRetrabalho.isPresent() ? ResponseEntity.ok(motivoRetrabalho.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_MOTIVO_RETRABALHO') and #oauth2.hasScope('write')")
	public ResponseEntity<MotivoRetrabalho> criar(@Valid @RequestBody MotivoRetrabalho motivoRetrabalho, HttpServletResponse response) {
		MotivoRetrabalho motivoRetrabalhoSalvo = motivoRetrabalhoRepository.save(motivoRetrabalho);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, motivoRetrabalhoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(motivoRetrabalhoSalvo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_MOTIVO_RETRABALHO') and #oauth2.hasScope('write')")
	public ResponseEntity<MotivoRetrabalho> atualizar(@PathVariable("codigo") Long codigo, @Valid @RequestBody MotivoRetrabalho motivoRetrabalho) {
		MotivoRetrabalho motivoRetrabalhoSalvo = motivoRetrabalhoRepository.findById(codigo)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(motivoRetrabalho, motivoRetrabalhoSalvo, "codigo");
		MotivoRetrabalho motivoRetrabalhoAtualizado = motivoRetrabalhoRepository.save(motivoRetrabalhoSalvo);
		return ResponseEntity.ok(motivoRetrabalhoAtualizado);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_MOTIVO_RETRABALHO') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		motivoRetrabalhoRepository.deleteById(codigo);
	}

}
