package br.com.alexfarma.sgq.api.mail;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.alexfarma.sgq.api.config.property.ApiProperty;
import br.com.alexfarma.sgq.api.model.Retrabalho;
import br.com.alexfarma.sgq.api.repository.projection.RegistroRetrabalhoDuplicado;

@Component
public class Mailer {

	@Autowired
	private ApiProperty apiProperty;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine thymeleaf;

	public void enviarRelatorioRetrabalho(List<Retrabalho> retrabalhos, List<String> destinatarios) {

		Map<String, Object> variaveis = new HashMap<>();

		// A variável "lancamentos" do template "aviso-lancamentos-vencidos.html"...
		// recebe uma lista de lançamentos vencidos;
		variaveis.put("retrabalhos", retrabalhos);

		/*
		 * // Gerar uma lista de string's de e-mails (email: atributo de "Usuario");
		 * List<String> emails = destinatarios.stream().map(u ->
		 * u.getEmail()).collect(Collectors.toList());
		 */

		this.enviarEmail(apiProperty.getMail().getUsername(), destinatarios, "Relatório analítico/diário dos registros de Retrabalho", "mail/relatorio-retrabalho-dia", variaveis);

	}

	public void enviarRelatorioRegistroRetrabalhoDuplicados(List<RegistroRetrabalhoDuplicado> registros, List<String> destinatarios) {

		Map<String, Object> variaveis = new HashMap<>();

		// A variável "lancamentos" do template "aviso-lancamentos-vencidos.html"...
		// recebe uma lista de lançamentos vencidos;
		variaveis.put("registros", registros);

		/*
		 * // Gerar uma lista de string's de e-mails (email: atributo de "Usuario");
		 * List<String> emails = destinatarios.stream().map(u ->
		 * u.getEmail()).collect(Collectors.toList());
		 */

		this.enviarEmail(apiProperty.getMail().getUsername(), destinatarios, "Relatório de inconsistências nos registros de Retrabalho", "mail/relatorio-retrabalho-registros-duplicados", variaveis);

	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template, Map<String, Object> variaveis) {

		Context context = new Context(new Locale("pt", "BR"));
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		String mensagem = thymeleaf.process(template, context);
		this.enviarEmail(remetente, destinatarios, assunto, mensagem);

	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {

		try {

			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(new InternetAddress(remetente, "QualiSystem - Sistema de Gestão da Qualidade"));
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);

			mailSender.send(mimeMessage);

		} catch (MessagingException | UnsupportedEncodingException e) {

			throw new RuntimeException("Falha no envio do e-mail de notificação!", e);

		}

	}

}
