package steps;

import com.thoughtworks.gauge.Step;
import pages.HomePage;

public class HomePageSteps {

    HomePage homePage = new HomePage();

    @Step("Click category card named <cardName>")
    public void clickCategoryCard(String cardName) {
        homePage.clickCategoryCardNamed(cardName);
    }
}
