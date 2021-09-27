package steps;

import utils.Utilities;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import utils.driver.Initialize;
import io.cucumber.core.api.Scenario;

public class CommonSteps extends Utilities {

    Initialize driverManager = new Initialize();

    @Before
    public void start(){driverManager.init();}

    @After
    public void kill(Scenario scenario){driverManager.kill(scenario);}

    @Given("Navigate to {}")
    public void getUrl(String url) {navigate(url);}

    @Given("Refresh the page")
    public void refresh() {
        refreshThePage();
    }

    @Given("Navigate browser {}")
    public void browserNavigate(String direction) {navigateBrowser(direction);}

    @Given("Wait {} seconds")
    public void wait(Integer duration) {
        waitFor(duration);
    }

}