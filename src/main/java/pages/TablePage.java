package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@PageObject
public class TablePage {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='table-selected-count']")
    public WebElement selectedCount;

    @FindBy(css = "[data-testid='table-search']")
    public WebElement searchInput;

    @FindBy(css = "[data-testid='table-row-checkbox-1']")
    public WebElement firstRowCheckbox;

    @FindBy(css = "[data-testid='table-row-1']")
    public WebElement firstRow;

    @FindBy(css = "[data-testid='table-header-name']")
    public WebElement nameHeader;

    @FindBy(css = "[data-testid='table-page-info']")
    public WebElement pageInfo;

    @FindBy(css = "[data-testid='table-next']")
    public WebElement nextButton;

    @FindBy(css = "[data-testid='table-prev']")
    public WebElement prevButton;

    @FindBy(css = "[data-testid='table'] tbody tr")
    public List<WebElement> tableRows;
}
