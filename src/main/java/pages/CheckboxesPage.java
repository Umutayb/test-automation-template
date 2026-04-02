package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

@PageObject
public class CheckboxesPage extends PickleibPageObject {

    @FindBy(css = "h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='checkbox-unchecked']")
    public WebElement uncheckedCheckbox;

    @FindBy(css = "[data-testid='checkbox-checked']")
    public WebElement checkedCheckbox;

    @FindBy(css = "[data-testid='checkbox-indeterminate']")
    public WebElement indeterminateCheckbox;

    @FindBy(css = "[data-testid='checkbox-disabled']")
    public WebElement disabledUncheckedCheckbox;

    @FindBy(css = "[data-testid='checkbox-disabled-checked']")
    public WebElement disabledCheckedCheckbox;

    @FindBy(css = "[data-testid='toggle-off']")
    public WebElement toggleOff;

    @FindBy(css = "[data-testid='toggle-on']")
    public WebElement toggleOn;

    @FindBy(css = "[data-testid='toggle-disabled']")
    public WebElement toggleDisabled;

    @FindBy(css = "[data-testid='checkbox-state-summary']")
    public WebElement stateSummary;
}
