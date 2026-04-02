package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

@PageObject
public class BookHiveCartPage extends PickleibPageObject {

    @FindBy(css = "[data-testid='cart-clear']")
    public WebElement clearCartButton;

    @FindBy(css = "[data-testid='cart-total']")
    public WebElement cartTotal;

    @FindBy(css = "[data-testid='checkout-btn']")
    public WebElement checkoutButton;

    @FindBy(css = "[data-testid='cart-empty']")
    public WebElement emptyCartMessage;
}
