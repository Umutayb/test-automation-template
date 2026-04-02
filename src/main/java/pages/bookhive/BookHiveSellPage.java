package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

@PageObject
public class BookHiveSellPage extends PickleibPageObject {

    @FindBy(css = "[data-testid='listing-book-select']")
    public WebElement bookSelect;

    @FindBy(css = "[data-testid='listing-condition']")
    public WebElement conditionSelect;

    @FindBy(css = "[data-testid='listing-price']")
    public WebElement priceInput;

    @FindBy(css = "[data-testid='listing-create']")
    public WebElement createListingButton;
}
