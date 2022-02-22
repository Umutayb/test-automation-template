package steps;

import io.cucumber.java.en.*;
import org.junit.Assert;
import pages.HomePage;
import utils.Printer;

public class HomePageSteps {

    HomePage homePage = new HomePage();
    Printer log = new Printer(HomePageSteps.class);

    @Given("Click category card named {}")
    public void clickCategoryCard(String cardName) {
        try {homePage.clickCategoryCardNamed(cardName);}
        catch (Exception e) {
            log.new Error(e.getStackTrace());
            Assert.fail();
        }
    }
}
