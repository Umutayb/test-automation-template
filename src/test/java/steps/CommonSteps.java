package steps;

import com.thoughtworks.gauge.Step;
import utils.Utilities;

public class CommonSteps extends Utilities{

    @Step("Navigate to <url>")
    public void getUrl(String url) {navigate(url);}

    @Step("Refresh the page")
    public void refresh() {
        refreshThePage();
    }

    @Step("Navigate browser <direction>")
    public void browserNavigate(String direction) {
        navigateBrowser(direction);
    }

    @Step("Wait <duration> seconds")
    public void wait(Integer duration) {
        waitFor(duration);
    }

}