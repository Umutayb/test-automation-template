package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class ProgressPage {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='progress-animated-value']")
    public WebElement animatedValue;

    @FindBy(css = "[data-testid='progress-start']")
    public WebElement startButton;

    @FindBy(css = "[data-testid='progress-reset']")
    public WebElement resetButton;

    @FindBy(css = "[data-testid='progress-circular-value']")
    public WebElement circularValue;
}
