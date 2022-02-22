package steps;

import io.cucumber.java.*;
import io.cucumber.java.en.*;
import utils.WebUtilities;
import utils.driver.Driver;
import io.cucumber.core.api.Scenario;

public class CommonSteps extends WebUtilities {

    Driver browser = new Driver();

    @Before
    public void start(){browser.initialize();}

    @After
    public void kill(Scenario scenario){
        browser.terminate(scenario);
    }

    @Given("Navigate to {}")
    public void getUrl(String url) {navigate(url);}

    @Given("Refresh the page")
    public void refresh() {
        refreshThePage();
    }

    @Given("Navigate browser {}")
    public void browserNavigate(String direction) {navigateBrowser(direction);}

    @Given("Click button with {} text")
    public void clickWithText(String text) {clickButtonWithText(text);}

    @Given("Wait {} seconds")
    public void wait(Integer duration) {
        waitFor(duration);
    }

}