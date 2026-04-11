package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@PageObject
public class BookHiveMarketplacePage {

    @FindBy(css = "[data-testid^='listing-']")
    public List<WebElement> listings;

    @FindBy(css = "[data-testid='no-listings']")
    public WebElement noListingsMessage;
}
