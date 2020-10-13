package br.com.alexfarma.sgq.api.config.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("api")
public class ApiProperty {

	private String originPermitida = "http://localhost:4200";

	private final Seguranca seguranca = new Seguranca();
	private final Mail mail = new Mail();

	public Seguranca getSeguranca() {
		return seguranca;
	}

	public Mail getMail() {
		return mail;
	}

	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}

	public static class Seguranca {

		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}

	}

	public static class Mail {

		private String host;

		private Integer porta;

		private String username;

		private String senha;

		@Value("${api.mail.destinatarios}")
		private String[] destinatarios;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPorta() {
			return porta;
		}

		public void setPorta(Integer porta) {
			this.porta = porta;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getSenha() {
			return senha;
		}

		public void setSenha(String senha) {
			this.senha = senha;
		}

		public String[] getDestinatarios() {
			return destinatarios;
		}

		public void setDestinatarios(String[] destinatarios) {
			this.destinatarios = destinatarios;
		}

	}

}
