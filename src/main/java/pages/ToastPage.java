package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

public class ToastPage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='toast-trigger-success']")
    public WebElement successButton;

    @FindBy(css = "[data-testid='toast-trigger-error']")
    public WebElement errorButton;

    @FindBy(css = "[data-testid='toast-trigger-warning']")
    public WebElement warningButton;

    @FindBy(css = "[data-testid='toast-trigger-info']")
    public WebElement infoButton;

    @FindBy(css = "[data-testid='toast-container']")
    public WebElement toastContainer;
}
