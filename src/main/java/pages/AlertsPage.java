package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

@PageObject
public class AlertsPage extends PickleibPageObject {

    @FindBy(id = "click-me")
    public WebElement clickMeButton;

    @FindBy(id = "right-click-me")
    public WebElement rightClickMeButton;

    @FindBy(id = "double-click-me")
    public WebElement doubleClickMeButton;

    @FindBy(css = ".clickables .button")
    public List<WebElement> alertButtons;

    @FindBy(css = "#tabs .button")
    public List<WebElement> tabButtons;

    @FindBy(css = "main h2")
    public WebElement pageTitle;
}
