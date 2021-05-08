package covaccine.alerts.util;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Session;

public class MailUtil {
	private String fromAddress;
	private String fromPassword;
	public MailUtil(String fromAddress,String fromPassword){
		this.fromAddress = fromAddress;
		this.fromPassword = fromPassword;
	}
	public boolean sendMail(List<String> toAddressList,String Subject,String msgContent) {
        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(fromAddress, fromPassword);

            }

        });

        // Used to debug SMTP issues
        session.setDebug(false);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(fromAddress));
            // Set Subject: header field
            message.setSubject(Subject);

            // Now set the actual message
            message.setContent(msgContent,"text/html");

            // Set To: header field of the header.
            for(String toAddress : toAddressList) {
            	message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            	// Send message
                Transport.send(message);
            }
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }

	}
}
