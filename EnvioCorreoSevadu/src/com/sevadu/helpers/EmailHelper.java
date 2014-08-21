package com.sevadu.helpers;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailHelper {
	public static String SERVER = "ntfp01ven";
	public static String SENDER = "cdusa@dusa.com.ve";
	public static String SUBJECT = "CONTROL DE DATOS INTEGRADOS AL SEVADU";

	/** Creates a new instance of EmailHelper */
	public EmailHelper() {

	}

	public static void sendCommentEmail(String recipients, String texto,
			byte[] attachmentData) {

		String body = "";
		body = texto + ". " + "\n\n";
		body = body + "CONTROL DE DATOS INTEGRADOS AL SEVADU";

		Properties props = new Properties();
		props.put("mail.smtp.host", "172.23.20.66");
		props.put("mail.smtp.port", "2525");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getInstance(props, auth);

		Message simpleMessage = new MimeMessage(session);

		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		try {
			fromAddress = new InternetAddress("cdusa@dusa.com.ve");
			toAddress = new InternetAddress(recipients);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			simpleMessage.setFrom(fromAddress);
			simpleMessage.setRecipient(RecipientType.TO, toAddress);
			simpleMessage.setSubject(SUBJECT);
			simpleMessage.setText(body);

			DataSource dataSource = new ByteArrayDataSource(attachmentData,
					"application/pdf");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName("controlDatosIntegrados.pdf");

			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(pdfBodyPart);

			simpleMessage.setContent(mimeMultipart);

			Transport.send(simpleMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class SMTPAuthenticator extends javax.mail.Authenticator {
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication("cdusa", "cartucho");
	}
}
