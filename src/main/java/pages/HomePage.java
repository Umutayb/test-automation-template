package pages;

import utils.Printer;
import java.util.List;
import utils.WebUtilities;
import static resources.Colors.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends WebUtilities {

    Printer log = new Printer(HomePage.class);

    @FindBy(css = "[class='card mt-4 top-card']")
    public List<WebElement> categoryCards;

    public void clickCategoryCardNamed(String cardName) throws Exception {
        log.new Info("Clicking card named "+BLUE+cardName);
        for (WebElement card:categoryCards) {
            if (card.getText().contains(cardName)){
                clickElement(card);
                return;
            }
        }
        log.new Error("No card named " + cardName + "could be located!");
        throw new Exception("Failed method: clickCategoryCardNamed -> HomePage");
    }
}
