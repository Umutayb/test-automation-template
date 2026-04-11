package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class BookHiveBookDetailPage {

    @FindBy(css = "[data-testid='book-detail-title']")
    public WebElement bookTitle;

    @FindBy(css = "[data-testid='book-detail-author']")
    public WebElement bookAuthor;

    @FindBy(css = "[data-testid='book-detail-genre']")
    public WebElement bookGenre;

    @FindBy(css = "[data-testid='book-detail-description']")
    public WebElement bookDescription;

    @FindBy(css = "[data-testid='book-detail-price']")
    public WebElement bookPrice;

    @FindBy(css = "[data-testid='book-detail-stock']")
    public WebElement bookStock;

    @FindBy(css = "[data-testid='add-to-cart-detail']")
    public WebElement addToCartButton;
}
