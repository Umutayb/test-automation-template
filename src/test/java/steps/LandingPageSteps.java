package steps;

import io.cucumber.java.en.Given;
import pages.LandingPage;

public class LandingPageSteps {
    LandingPage landingPage = new LandingPage();

    @Given("Click the media container titled {}")
    public void getUrl(String title) {landingPage.clickMediaContainerTitled(title);}
}
