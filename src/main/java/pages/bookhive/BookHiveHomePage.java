package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@PageObject
public class BookHiveHomePage {

    @FindBy(css = "input[placeholder*='Search']")
    public WebElement searchInput;

    @FindBy(css = "a[href^='/books/']")
    public List<WebElement> bookCards;

    @FindBy(css = "[data-testid='pagination-prev']")
    public WebElement paginationPrev;

    @FindBy(css = "[data-testid='pagination-next']")
    public WebElement paginationNext;

    @FindBy(css = "[data-testid='pagination-info']")
    public WebElement paginationInfo;
}
