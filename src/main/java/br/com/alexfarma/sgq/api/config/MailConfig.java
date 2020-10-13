package br.com.alexfarma.sgq.api.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import br.com.alexfarma.sgq.api.config.property.ApiProperty;

@Configuration
public class MailConfig {

	@Autowired
	private ApiProperty property;
	
	@Bean
	public JavaMailSender javaMailSender() {

		Properties props = new Properties();

		props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.ssl.trust", "*");
	    props.put("mail.smtp.connectiontimeout", 12000);

	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setJavaMailProperties(props);
	    mailSender.setHost(property.getMail().getHost());
	    mailSender.setPort(property.getMail().getPorta());
	    mailSender.setUsername(property.getMail().getUsername());
	    mailSender.setPassword(property.getMail().getSenha());
	    return mailSender;

	}

}
