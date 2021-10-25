package steps;

import io.cucumber.java.en.*;
import pages.HomePage;

public class HomePageSteps {

    HomePage homePage = new HomePage();

    @Given("Click category card named {}")
    public void clickCategoryCard(String cardName) {homePage.clickCategoryCardNamed(cardName);}
}
