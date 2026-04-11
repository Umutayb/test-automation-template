package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class TooltipPage {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='tooltip-trigger-1']")
    public WebElement hoverTarget;

    @FindBy(css = "[data-testid='tooltip-content-1']")
    public WebElement tooltipContent;

    @FindBy(css = "[data-testid='popover-trigger-1']")
    public WebElement popoverTrigger;

    @FindBy(css = "[data-testid='popover-content-1']")
    public WebElement popoverContent;
}
