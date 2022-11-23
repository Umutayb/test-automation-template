package common;

import org.openqa.selenium.TimeoutException;
import utils.*;

import java.util.concurrent.TimeUnit;

import static utils.EmailUtilities.Inbox.EmailField.CONTENT;

public class EmailInbox extends WebUtilities {

    Printer log = new Printer(EmailInbox.class);
    TextParser parser = new TextParser();

    public String getEmail(
            EmailUtilities.Inbox.EmailField filter,
            String filterKey,
            String initialKeyword,
            String finalKeyword,
            Boolean print,
            Boolean save){
        double initialTime = System.currentTimeMillis();
        log.new Info("Acquiring email...");

        EmailUtilities.Inbox inbox;
        do {
            inbox = new EmailUtilities.Inbox(
                    "pop.gmail.com",
                    "995",
                    properties.getProperty("test-email"),
                    properties.getProperty("email-application-password"),
                    "ssl",
                    filter,
                    filterKey,
                    print,
                    save,
                    save
            );
            try {TimeUnit.SECONDS.sleep(3);}
            catch (InterruptedException e) {throw new RuntimeException(e);}
            if (System.currentTimeMillis() - initialTime > 45000) throw new TimeoutException("Verification email did not arrive!");
        }
        while (inbox.messages.size() == 0);

        log.new Success("Email acquired!");
        return parser.parse(initialKeyword, finalKeyword, inbox.messages.get(0).get(CONTENT).toString());
    }

    public String getEmail(
            EmailUtilities.Inbox.EmailField filter,
            String filterKey,
            Boolean print,
            Boolean save){
        double initialTime = System.currentTimeMillis();
        log.new Info("Acquiring email...");

        EmailUtilities.Inbox inbox;
        do {
            inbox = new EmailUtilities.Inbox(
                    "pop.gmail.com",
                    "995",
                    properties.getProperty("test-email"),
                    properties.getProperty("email-application-password"),
                    "ssl",
                    filter,
                    filterKey,
                    print,
                    save,
                    save
            );
            try {TimeUnit.SECONDS.sleep(1);}
            catch (InterruptedException e) {throw new RuntimeException(e);}
            if (System.currentTimeMillis() - initialTime > 45000) throw new TimeoutException("Verification email did not arrive!");
        }
        while (inbox.messages.size() == 0);


        log.new Success("Email acquired!");
        return inbox.messages.get(0).get(CONTENT).toString();
    }

    public static void main(String[] args) { // Flush email
        new Printer(EmailInbox.class).new Info("Flushing email inbox...");
        new EmailUtilities.Inbox(
                "pop.gmail.com",
                "995",
                FileUtilities.properties.getProperty("test-email"),
                FileUtilities.properties.getProperty("email-application-password"),
                "ssl",
                false,
                false,
                false
        );
    }
}
