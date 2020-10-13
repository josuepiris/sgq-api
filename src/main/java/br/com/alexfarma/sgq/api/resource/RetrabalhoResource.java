package br.com.alexfarma.sgq.api.resource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alexfarma.sgq.api.dto.PrimeiroAndUltimoRegistro;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaDia;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaFuncionario;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaLoja;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatisticaMotivo;
import br.com.alexfarma.sgq.api.event.RecursoCriadoEvent;
import br.com.alexfarma.sgq.api.exceptionhandler.ApiExceptionHandler.Erro;
import br.com.alexfarma.sgq.api.model.Retrabalho;
import br.com.alexfarma.sgq.api.repository.RetrabalhoRepository;
import br.com.alexfarma.sgq.api.repository.filter.DashboardFilter;
import br.com.alexfarma.sgq.api.repository.filter.RetrabalhoFilter;
import br.com.alexfarma.sgq.api.repository.projection.ResumoRetrabalho;
import br.com.alexfarma.sgq.api.service.RetrabalhoService;
import br.com.alexfarma.sgq.api.service.exception.FuncionarioInexistenteOuInativoException;
import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/retrabalhos")
public class RetrabalhoResource {

	@Autowired
	private RetrabalhoRepository retrabalhoRepository;

	@Autowired
	private RetrabalhoService retrabalhoService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;

	@GetMapping(value = "/estatisticas", params = "porDia")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<RetrabalhoEstatisticaDia> porDia(@RequestParam("mesReferencia")
		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mesReferencia, @RequestParam Long codigoProcesso) {
		return this.retrabalhoRepository.porDia(mesReferencia, codigoProcesso);
	}

	@GetMapping(value = "/estatisticas", params = "porMotivo")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<RetrabalhoEstatisticaMotivo> porMotivo(DashboardFilter filtros) {
		return this.retrabalhoRepository.porMotivo(filtros);
	}

	@GetMapping(value = "/estatisticas", params = "porLoja")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<RetrabalhoEstatisticaLoja> porLoja(
		@RequestParam("dataRegistroDe")	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRegistroDe,
		@RequestParam("dataRegistroAte") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRegistroAte) {
		return this.retrabalhoRepository.totalRetrabalhosPorLoja(dataRegistroDe, dataRegistroAte);
	}

	@GetMapping(value = "/estatisticas", params = "porFuncionario")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public List<RetrabalhoEstatisticaFuncionario> porFuncionario(
		@RequestParam("dataRegistroDe")	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRegistroDe,
		@RequestParam("dataRegistroAte") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRegistroAte,
		@RequestParam("codigoProcesso") Long codigoProcesso, Long codigoDepartamento) {

		RetrabalhoFilter filtros = new RetrabalhoFilter(dataRegistroDe, dataRegistroAte, codigoProcesso);

		if(codigoDepartamento != null) {
			filtros.setDepartamento(codigoDepartamento);
		}

		return this.retrabalhoRepository.totalRetrabalhosPorFuncionario(filtros);
	}

	@GetMapping("/relatorio")
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_RETRABALHO') and #oauth2.hasScope('read')")
	public ResponseEntity<byte[]> estatisticasRetrabalho(RetrabalhoFilter filtros) throws JRException {

		byte[] relatorio = retrabalhoService.gerarEstatisticaRetrabalho(filtros);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
		headers.add("Content-Disposition", "attachment; filename=estatisticas-retrabalho.pdf");

		return ResponseEntity.ok().headers(headers).body(relatorio);

	}

	@GetMapping(params = "firstAndTop")
	public PrimeiroAndUltimoRegistro buscarPrimeiroAndUltimoRegistro() {
		return retrabalhoRepository.buscarPrimeiroAndUltimoRegistro();
	}

	@GetMapping(params = "exists")
	@PreAuthorize("isAuthenticated() and #oauth2.hasScope('read')")
	public boolean exists(@RequestParam String protocolo, @RequestParam Long formula, @RequestParam Long motivo) {
		return this.retrabalhoRepository.existsRetrabalhoByProtocoloAndNumeroFormulaAndMotivoRetrabalhoCodigo(
				protocolo, formula, motivo);
	}

	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_RETRABALHO') and #oauth2.hasScope('read')")
	public Page<ResumoRetrabalho> resumir(RetrabalhoFilter retrabalhoFilter, Pageable pageable) {
		return retrabalhoRepository.resumir(retrabalhoFilter, pageable);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CONSULTAR_RETRABALHO') and #oauth2.hasScope('read')")
	public ResponseEntity<Retrabalho> buscarPeloCodigo(@PathVariable Long codigo) {
		Optional<Retrabalho> lancamento = retrabalhoRepository.findById(codigo);
		return lancamento.isPresent() ? ResponseEntity.ok(lancamento.get()) : ResponseEntity.notFound().build();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_RETRABALHO') and #oauth2.hasScope('write')")
	public ResponseEntity<Retrabalho> criar(@Valid @RequestBody Retrabalho retrabalho, HttpServletResponse response) {
		Retrabalho retrabalhoSalvo = retrabalhoService.salvar(retrabalho);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, retrabalhoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(retrabalhoSalvo);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_RETRABALHO') and #oauth2.hasScope('write')")
	public ResponseEntity<Retrabalho> atualizar(@PathVariable Long codigo, @Valid @RequestBody Retrabalho lancamento) {
		try {
			Retrabalho lancamentoSalvo = retrabalhoService.atualizar(codigo, lancamento);
			return ResponseEntity.ok(lancamentoSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAnyAuthority('ROLE_REMOVER_RETRABALHO', 'ROLE_1000414') and #oauth2.hasScope('write')")
	public void remover(@PathVariable Long codigo) {
		retrabalhoRepository.deleteById(codigo);
	}

	@ExceptionHandler({FuncionarioInexistenteOuInativoException.class})
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(FuncionarioInexistenteOuInativoException ex) {
		String mensagemUsuario = messageSource.getMessage("funcionario.inexistente-ou-inativo", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

}
