package steps;

import pages.Google;
import io.cucumber.java.en.Given;
import org.openqa.selenium.NoSuchElementException;

public class GoogleSteps {

    Google google = new Google();

    @Given("Dismiss privacy notification")
    public void dismissPrivacyNotification(){
        try {google.click(google.cookieAcceptButton);}
        catch (NoSuchElementException ignored) {}
    }

    @Given("Perform google search for phrase: {}")
    public void search(String searchPhrase) {google.searchFor(searchPhrase);}

    @Given("Print exchange rate")
    public void printExchangeRate() {google.printRate();}

    @Given("Notify if the rate has changed")
    public void notifyIfChanged() {google.notifyIfChanged();}
}