package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

import java.util.List;

public class CardsShelf extends WebUtilities {

    @FindBy(className = "div")
    public List<WebElement> cards;

}
