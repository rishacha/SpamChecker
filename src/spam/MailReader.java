package spam;

import org.jsoup.Jsoup;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Jasmin2332 on 4/18/2016.
 */
class MailReader {

    private Tokenizer mTokenizer;

    public MailReader() {
        mTokenizer = new Tokenizer();
    }

    public MailData read (File file) throws Exception {
        InputStream mailFileInputStream = new FileInputStream(file);
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session, mailFileInputStream);
        Address sender = message.getSender();
        String subject = message.getSubject();
        String result = "";
        try {
            if (message.isMimeType("text/plain")) {
                result = message.getContent().toString();
            } else if (message.isMimeType("multipart/*")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                int count = mimeMultipart.getCount();
                for (int i = 0; i < count; i++) {
                    BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                    if (bodyPart.isMimeType("text/plain")) {
                        result = result + "\n" + bodyPart.getContent();
                        break;  //without break same text appears twice in my tests
                    } else if (bodyPart.isMimeType("text/html")) {
                        String html = (String) bodyPart.getContent();
                        result = result + "\n" + Jsoup.parse(html).text();
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Broken file " + file.getName());
        }
        String[] tokens = mTokenizer.tokenize(result, true);
        return new MailData(sender != null ? sender.toString().split("@")[1].trim() : null, subject!=null ? subject.trim() : null, tokens);
    }
}
