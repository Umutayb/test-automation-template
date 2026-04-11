package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class DrawerPage {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='drawer-open-left']")
    public WebElement openLeftButton;

    @FindBy(css = "[data-testid='drawer-open-right']")
    public WebElement openRightButton;

    @FindBy(css = "[data-testid='drawer-status']")
    public WebElement statusText;

    @FindBy(css = "[data-testid='drawer-close']")
    public WebElement closeButton;

    @FindBy(css = "[data-testid='drawer-overlay']")
    public WebElement overlay;
}
