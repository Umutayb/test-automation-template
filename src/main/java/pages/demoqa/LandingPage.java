package pages.demoqa;

import common.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class LandingPage extends PageObject {

    @FindBy(css = "[class='card mt-4 top-card']")
    List<WebElement> categoryCards;

}
