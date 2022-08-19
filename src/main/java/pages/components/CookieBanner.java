package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

public class CookieBanner extends WebUtilities {

    @FindBy(css = "button#bbccookies-continue-button")
    public WebElement agreeButton;

    @FindBy(css = "#bbccookies-settings")
    public WebElement settingsButton;
}
