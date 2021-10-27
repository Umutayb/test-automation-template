package steps;

import io.cucumber.java.en.Given;
import pages.UpsHomePage;

public class UpsSteps {
    UpsHomePage upsHomePage = new UpsHomePage();

    @Given("Perform package check for tracking number {}")
    public void trackingCheck(String trackingNumber){
        upsHomePage.clearFillInput(upsHomePage.domesticPackageTrackingNumberInput,trackingNumber,true);
    }

    @Given("Submit tracking number")
    public void clickSubmitButton() {upsHomePage.clickElement(upsHomePage.domesticPackageTrackingNumberSubmitButton);}

    @Given("Click details button")
    public void clickDetailsButton() {upsHomePage.clickElement(upsHomePage.detailsButton);}

    @Given("Click progress tab")
    public void clickProgressTab() {upsHomePage.clickElement(upsHomePage.progressTab);}

    @Given("List UPS activities")
    public void clickCategoryCard() {upsHomePage.listActivities();}
}
