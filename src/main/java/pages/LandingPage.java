package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;
import java.util.List;

public class LandingPage extends WebUtilities {

    @FindBy(css = "[class='card mt-4 top-card']")
    List<WebElement> categoryCards;

}
