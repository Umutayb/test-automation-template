package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

import java.util.List;

public class PageShortcuts extends WebUtilities {

    @FindBy(tagName = "div")
    public List<WebElement> categories;

}
