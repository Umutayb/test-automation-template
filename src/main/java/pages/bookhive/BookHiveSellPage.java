package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class BookHiveSellPage {

    @FindBy(css = "[data-testid='listing-book-select']")
    public WebElement bookSelect;

    @FindBy(css = "[data-testid='listing-condition']")
    public WebElement conditionSelect;

    @FindBy(css = "[data-testid='listing-price']")
    public WebElement priceInput;

    @FindBy(css = "[data-testid='listing-create']")
    public WebElement createListingButton;
}
