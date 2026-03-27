package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

public class AccordionPage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='accordion-expand-all']")
    public WebElement expandAllButton;

    @FindBy(css = "[data-testid='accordion-collapse-all']")
    public WebElement collapseAllButton;

    @FindBy(css = "[data-testid='accordion-header-1']")
    public WebElement firstHeader;

    @FindBy(css = "[data-testid='accordion-body-1']")
    public WebElement firstBody;

    @FindBy(css = "[data-testid='accordion-header-4']")
    public WebElement fourthHeader;

    @FindBy(css = "[data-testid='accordion-body-4']")
    public WebElement fourthBody;
}
