package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

public class CookiePopup extends WebUtilities {

    @FindBy(css = "[aria-label='Consent']")
    public WebElement consentButton;

    @FindBy(css = "[aria-label='Do not consent']")
    public WebElement refuseButton;

    @FindBy(css = "[aria-label='Manage options']")
    public WebElement optionsButton;
}
