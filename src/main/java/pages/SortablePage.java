package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class SortablePage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='sortable-count-1']")
    public WebElement firstListCount;

    @FindBy(css = "[data-testid='sortable-count-2']")
    public WebElement secondListCount;

    @FindBy(css = "[data-testid='sortable-item-1']")
    public WebElement firstItem;

    @FindBy(css = "[data-testid='sortable-list-1'] [data-testid^='sortable-item-']")
    public List<WebElement> firstListItems;

    @FindBy(css = "[data-testid='sortable-list-2'] [data-testid^='sortable-item-']")
    public List<WebElement> secondListItems;
}
