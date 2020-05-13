package EmailPackage;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Pratik on 10/28/2017.
 */

public class SendEMail {

    private static String username = "onlinebiometricattendance@gmail.com";
    private static String password = "Password@1234";
    private static String fromAddress = username;

    public static String receiver = username;
    public static String subject = "Subject";
    public static String body = "Body";

    public static String sendMail() throws Exception{

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator(){
            protected PasswordAuthentication
            getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        //try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);

            return "Mail Send!";
//        }
//        catch (Exception e) {
//            return(e.toString());
//        }
    }


}
