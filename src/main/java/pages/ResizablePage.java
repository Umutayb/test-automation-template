package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class ResizablePage {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='resizable-width']")
    public WebElement widthDisplay;

    @FindBy(css = "[data-testid='resizable-panel']")
    public WebElement resizablePanel;

    @FindBy(css = "[data-testid='resizable-handle']")
    public WebElement dragHandle;
}
