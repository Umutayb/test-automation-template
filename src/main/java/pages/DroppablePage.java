package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class DroppablePage extends PickleibPageObject {

    @FindBy(css = "h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='droppable-item-1']")
    public WebElement draggableItem;

    @FindBy(css = "[data-testid='droppable-zone-red']")
    public WebElement dropTarget;

    @FindBy(css = "[data-testid='droppable-status']")
    public WebElement statusText;

    @FindBy(css = ".drop-item")
    public List<WebElement> sourceItems;

    @FindBy(css = ".drop-zone")
    public List<WebElement> dropZones;

    @FindBy(css = "[data-testid='droppable-reset']")
    public WebElement resetButton;
}
