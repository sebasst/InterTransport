package com.sagatechs.javaeeApp.services.general;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@ApplicationScoped
public class EmailService {

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;

	Properties prop;

	Session session;

	boolean smptpAuth = true;
	boolean smptpStarttls = true;
	String smtpHost = "shared7.arvixe.com";
	int smtpPort = 465;
	//String smtpSslTrust = "shared7.arvixe.com";
	String mailUsername = "hola@sagatechs.com";
	String mailPassword = "A1a2a3a4";
	private String adminEmailAdress="hola@sagatechs.com";

	@PostConstruct
	public void init() {
		try {
			prop = new Properties();
			Properties prop = new Properties();
			prop.put("mail.smtp.host", smtpHost);
	        prop.put("mail.smtp.port", "465");
	        prop.put("mail.smtp.auth", "true");
	        prop.put("mail.smtp.socketFactory.port", "465");
	        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			session = Session.getInstance(prop, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailUsername, mailPassword);
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendEmailMessage(String destinationAdress, String subject, String messageText) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(adminEmailAdress));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAdress));
			message.setSubject(subject);

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(messageText, "text/html");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			message.setContent(multipart);

			Transport.send(message);
		} catch (MessagingException e) {
			
			e.printStackTrace();
		}
	}

}
