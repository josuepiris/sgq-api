package br.com.alexfarma.sgq.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alexfarma.sgq.api.config.property.ApiProperty;
import br.com.alexfarma.sgq.api.dto.RetrabalhoEstatistica;
import br.com.alexfarma.sgq.api.mail.Mailer;
import br.com.alexfarma.sgq.api.model.Departamento;
import br.com.alexfarma.sgq.api.model.Funcionario;
import br.com.alexfarma.sgq.api.model.Processo;
import br.com.alexfarma.sgq.api.model.Retrabalho;
import br.com.alexfarma.sgq.api.model.Usuario;
import br.com.alexfarma.sgq.api.repository.DepartamentoRepository;
import br.com.alexfarma.sgq.api.repository.FuncionarioRepository;
import br.com.alexfarma.sgq.api.repository.ProcessoRepository;
import br.com.alexfarma.sgq.api.repository.RetrabalhoRepository;
import br.com.alexfarma.sgq.api.repository.UsuarioRepository;
import br.com.alexfarma.sgq.api.repository.filter.RetrabalhoFilter;
import br.com.alexfarma.sgq.api.repository.projection.RegistroRetrabalhoDuplicado;
import br.com.alexfarma.sgq.api.service.exception.FuncionarioInexistenteOuInativoException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class RetrabalhoService {

	//private static final String DESTINATARIOS = "ROLE_PESQUISAR_RETRABALHO";
	private static final Logger logger = LoggerFactory.getLogger(RetrabalhoService.class);

	@Autowired
	private RetrabalhoRepository retrabalhoRepository;

	@Autowired
	private DepartamentoRepository departamentoRepository;

	@Autowired
	private ProcessoRepository processoRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private Mailer mailer;

	@Autowired
	private ApiProperty apiProperty;

	public Retrabalho salvar(Retrabalho retrabalho) {
		validarFuncionario(retrabalho);
		return retrabalhoRepository.save(retrabalho);
	}

	public Retrabalho atualizar(Long codigo, Retrabalho retrabalho) {

		Retrabalho retrabalhoSalvo = buscarRetrabalhoPeloCodigo(codigo);

		// Validar Funcionario ao atualizar um registro de Retrabalho;
		if (!retrabalho.getFuncionario().equals(retrabalhoSalvo.getFuncionario())) {
			validarFuncionario(retrabalho);
		}

		BeanUtils.copyProperties(retrabalho, retrabalhoSalvo, "codigo");

		return retrabalhoRepository.save(retrabalhoSalvo);

	}

	public void enviarRelatorioDiarioRetrabalho() {

		logger.info("Preparando envio de relatório dos registros de Retrabalho...");

		LocalDate data = LocalDate.now();

		List<Departamento> departamentos = departamentoRepository.findByAtivoTrue();

		departamentos.forEach(departamento -> {

			List<Retrabalho> retrabalhos = retrabalhoRepository.findByDepartamentoCodigoAndDataHoraCriacaoBetween(
					departamento.getCodigo(), data.atTime(0, 0), data.atTime(23, 59, 59, 999999999));

			if(retrabalhos.isEmpty()) {
				logger.info(departamento.getNome() + " - Nenhum registro de Retrabalho!");
				return;
			}

			logger.info(departamento.getNome() + " - Encontrados {} registros de Retrabalho!", retrabalhos.size());

			List<String> destinatarios = new ArrayList<>();

			if(departamento.getGestor().getEmail() != null) {
				destinatarios.add(departamento.getGestor().getEmail());
			}

			if(destinatarios.isEmpty()) {
				logger.info(departamento.getNome() + " - Nenhum destinatário definido para o recebimento do relatório!");
				return;
			}

			logger.info(departamento.getNome() + " - Invocando o método enviarRelatorioRetrabalho...");
			mailer.enviarRelatorioRetrabalho(retrabalhos, destinatarios);

		});

		logger.info("Envio de relatório concluído com sucesso!");

	}

	public void enviarRelatorioRegistrosDuplicados() {

		logger.info("Iniciando a verificação de inconsistências nos regitros de Retrabalhos...");

		List<Departamento> departamentos = departamentoRepository.findByAtivoTrue();

		departamentos.forEach(departamento -> {

			List<RegistroRetrabalhoDuplicado> registros = retrabalhoRepository.buscarRegistrosDuplicados(departamento.getCodigo());

			if(registros.isEmpty()) {
				logger.info(departamento.getNome() + " - Não há inconsistências nos registros de Retrabalho!");
				return;
			}

			logger.info(departamento.getNome() + " - Encontrados {} registros de Retrabalho possivelmente duplicados!", registros.size());

			List<String> destinatarios = new ArrayList<>();

			if(departamento.getGestor().getEmail() != null) {

				destinatarios.add(departamento.getGestor().getEmail());

				// lista de destinatários que irão receber cópias dos e-mails
				List<String> outrosDestinatarios = Arrays.asList(apiProperty.getMail().getDestinatarios());
				destinatarios.addAll(outrosDestinatarios);

			}

			if(destinatarios.isEmpty()) {
				logger.info(departamento.getNome() + " - Nenhum destinatário definido para o recebimento do relatório!");
				return;
			}

			logger.info(departamento.getNome() + " - Invocando o método enviarRelatorioRegistroRetrabalhoDuplicados...");
			mailer.enviarRelatorioRegistroRetrabalhoDuplicados(registros, destinatarios);

		});

		logger.info("Envio de relatórios concluído com sucesso!");

	}

	public byte[] gerarEstatisticaRetrabalho(RetrabalhoFilter filtros) throws JRException {

		List<RetrabalhoEstatistica> dados = retrabalhoRepository.gerarEstatisticasRetrabalho(filtros);
		Long total = retrabalhoRepository.total(filtros);

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("TOTAL", total);
		parametros.put("DT_INICIO", Date.valueOf(filtros.getDataRegistroDe()));
		parametros.put("DT_FIM", Date.valueOf(filtros.getDataRegistroAte()));

		if (filtros.getProcesso() != null) {

			Optional<Processo> processo = processoRepository.findById(filtros.getProcesso());

			if (processo.isPresent()) {
				parametros.put("PROCESSO", processo.get().getDescricao());
			}

		}

		if (filtros.getDepartamento() != null) {

			Optional<Departamento> departamento = departamentoRepository.findById(filtros.getDepartamento());

			if (departamento.isPresent()) {
				parametros.put("DEPARTAMENTO", departamento.get().getNome());
			}

		}

		if (filtros.getFuncionarioRegistro() != null) {

			Optional<Usuario> usuario = usuarioRepository.findById(filtros.getFuncionarioRegistro());

			if (usuario.isPresent()) {
				parametros.put("FUNCIONARIO_REGISTRO", usuario.get().getNome());
			}

		}

		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

		InputStream	inputStream = this.getClass().getResourceAsStream("/relatorios/retrabalhos-por-funcionario-sintetico.jasper");
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));

		return JasperExportManager.exportReportToPdf(jasperPrint);

	}

	private void validarFuncionario(Retrabalho retrabalho) {

		Funcionario funcionario = null;

		if (retrabalho.getFuncionario().getFuncionarioId() != null) {
			funcionario = funcionarioRepository.getOne(retrabalho.getFuncionario().getFuncionarioId());
		}

		if (funcionario == null || !funcionario.getAtivo()) {
			throw new FuncionarioInexistenteOuInativoException();
		}

	}

	private Retrabalho buscarRetrabalhoPeloCodigo(Long codigo) {

		Optional<Retrabalho> retrabalhoSalvo = retrabalhoRepository.findById(codigo);

		if (!retrabalhoSalvo.isPresent()) {
			throw new IllegalArgumentException();
		}

		return retrabalhoSalvo.get();

	}

}
