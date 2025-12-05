package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class LandingPage extends PickleibPageObject {

    @FindBy(css = "[class='card mt-4 top-card']")
    List<WebElement> categoryCards;

}
