package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

public class PiniaCounterPage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='counter-value']")
    public WebElement counterDisplay;

    @FindBy(css = "[data-testid='counter-increment']")
    public WebElement incrementButton;

    @FindBy(css = "[data-testid='counter-decrement']")
    public WebElement decrementButton;

    @FindBy(css = "[data-testid='counter-reset']")
    public WebElement resetButton;

    @FindBy(css = "[data-testid='counter-step-input']")
    public WebElement stepInput;

    @FindBy(css = "[data-testid='counter-history']")
    public WebElement historyList;
}
