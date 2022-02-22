package pages;

import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.Printer;
import utils.WebUtilities;

import java.util.List;
import static resources.Colors.*;

public class HomePage extends WebUtilities {

    Printer log = new Printer(HomePage.class);

    @FindBy(css = "[class='card mt-4 top-card']")
    public List<WebElement> categoryCards;

    public void clickCategoryCardNamed(String cardName){
        log.new Info("Clicking card named "+BLUE+cardName);
        for (WebElement card:categoryCards) {
            if (card.getText().contains(cardName)){
                clickElement(card);
                return;
            }
        }
        Assert.fail(RED+"No category card named "+BLUE+cardName+GRAY+" could be found in HomePage @clickCategoryCard"+RESET);
    }
}
