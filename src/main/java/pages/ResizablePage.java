package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

public class ResizablePage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='resizable-width']")
    public WebElement widthDisplay;

    @FindBy(css = "[data-testid='resizable-panel']")
    public WebElement resizablePanel;

    @FindBy(css = "[data-testid='resizable-handle']")
    public WebElement dragHandle;
}
