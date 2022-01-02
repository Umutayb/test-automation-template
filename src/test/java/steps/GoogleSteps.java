package steps;

import io.cucumber.java.en.Given;
import pages.Google;

public class GoogleSteps {

    Google google = new Google();

    @Given("Dismiss privacy notification")
    public void dismissPrivacyNotification(){google.click(google.cookieAcceptButton);}

    @Given("Perform google search for phrase: {}")
    public void search(String searchPhrase) {google.searchFor(searchPhrase);}

    @Given("Print exchange rate")
    public void printExchangeRate() {google.printRate();}

    @Given("Notify if the rate has changed")
    public void notifyIfChanged() {google.notifyIfChanged();}
}
