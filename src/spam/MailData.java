package spam;

/**
 * Created by Jasmin2332 on 4/18/2016.
 */
public class MailData {
    private String sender;
    private String subject;
    private String[] tokens;

    public MailData(MailData mailData) {
        this(mailData.sender, mailData.subject, mailData.tokens);
    }

    MailData(String sender, String subject, String[] tokens) {
        this.sender = sender!=null ? sender : "";
        this.subject = subject!=null ? subject : "";
        this.tokens = tokens!=null ? tokens : new String[]{""};
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public String[] getTokens() {
        return tokens;
    }
}
