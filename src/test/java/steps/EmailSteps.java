package steps;

import common.FilterPair;
import context.ContextStore;
import io.cucumber.java.en.Given;
import collections.Pair;
import lombok.Getter;
import org.junit.Assert;
import utils.Printer;
import utils.StringUtilities;
import utils.email.EmailAcquisition;
import utils.email.EmailUtilities;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static utils.StringUtilities.markup;
import static utils.email.EmailUtilities.Inbox.EmailField.SUBJECT;

public class EmailSteps {

    public Printer log = new Printer(this.getClass());
    EmailAcquisition emailAcquisition = new EmailAcquisition(getEmailInbox(ContextStore.get("environment"))); // multiple environment support

    @Getter
    public enum EmailSubjects {
        ACCOUNT_VERIFICATION("Please verify your email address"),
        RESET_PASSWORD("Please reset your password");

        private final String key; // Set custom subject values here

        EmailSubjects(String key) {
            this.key = key;
        }
    }

    /**
     * Acquires and saves email based on the specified filter type and key.
     *
     * @param filterType the type of email filter
     * @param filterKey  the key for filtering emails
     */
    @Given("Acquire & save email by filter {} -> {}")
    public void acquireEmailByFilter(String filterType, String filterKey) {
        acquireEmailByMultipleFilters(List.of(new FilterPair(filterType, filterKey)));
    }

    /**
     * Acquires and saves email based on the subject.
     *
     * @param subject the subject of the email
     */
    @Given("Acquire & save email by subject -> {}")
    public void acquireEmailBySubject(EmailSubjects subject) {
        try{
            LocalDateTime startEmailAcquisitionTime = LocalDateTime.now();
            ContextStore.put("emailPath", emailAcquisition.acquireEmail(SUBJECT, subject.getKey()));
            long timeForEmailAcquisition = ChronoUnit.SECONDS.between(startEmailAcquisitionTime, LocalDateTime.now());
            log.info("It took about " + markup(StringUtilities.Color.BLUE, String.valueOf(timeForEmailAcquisition)) + " seconds to acquire the email.");
        }
        catch (Exception e){
            log.error("No emails was found with conditions indicated!", e);
        }
        Assert.assertNotNull("The acquired email path should not be null or empty", ContextStore.get("emailPath"));
    }

    /**
     * Acquires and saves email based on multiple filters.
     *
     * @param pairs a list of pairs containing email filter type and key
     */
    @Given("Acquire & save email by multiple filters")
    public void acquireEmailByMultipleFilters(List<FilterPair> pairs) {
        try{
            List<Pair<EmailUtilities.Inbox.EmailField, String>> updatedPairs = pairs.stream().map(pair-> {
                if (pair.getKey().equals("SUBJECT"))
                    return Pair.of(
                            EmailUtilities.Inbox.EmailField.SUBJECT,
                            EmailSubjects.valueOf(pair.getValue()).getKey()
                    );
                else
                    return Pair.of(
                            EmailUtilities.Inbox.EmailField.valueOf(pair.getKey()),
                            StringUtilities.contextCheck(pair.getValue())
                    );
            }).collect(Collectors.toList());

            LocalDateTime startEmailAcquisitionTime = LocalDateTime.now();
            ContextStore.put("emailPath", emailAcquisition.acquireEmail(updatedPairs));
            long timeForEmailAcquisition = ChronoUnit.SECONDS.between(startEmailAcquisitionTime, LocalDateTime.now());
            log.info("It took about " + markup(StringUtilities.Color.BLUE, String.valueOf(timeForEmailAcquisition)) + " seconds to get email");
        }
        catch (Exception e){
            log.error("No emails was found with conditions indicated!", e);
        }
        Assert.assertNotNull("The acquired email path should not be null or empty", ContextStore.get("emailPath"));
    }

    /**
     * Cleans the email inbox.
     */
    @Given("Clean the {} email inbox")
    public void flushEmail(String environment) {
        getEmailInbox(environment).clearInbox();
    }

    /**
     * Cleans the email inbox on environment from context.
     */
    @Given("Clean the email inbox")
    public void flushEmailOnEnvironmentFromContext() {
        getEmailInbox(ContextStore.get("environment")).clearInbox();
    }

    private EmailUtilities.Inbox getEmailInbox(String environment) {
        Pair<String, String> emailCredentials = switch (environment) {
            case "test" -> Pair.of(ContextStore.get("test-email"), ContextStore.get("email-application-password"));
            case "acc" -> Pair.of(ContextStore.get("acc-email"), ContextStore.get("acc-email-application-password"));
            default -> throw new RuntimeException(
                    "Email or email application password is null! Please make sure that the test.properties for " +
                            "'test-email' and 'email-application-password' properties are set."
            );
        };

        return new EmailUtilities.Inbox(
                "pop.gmail.com",
                "995",
                emailCredentials.alpha(),
                emailCredentials.beta(),
                "ssl"
        );
    }
}
