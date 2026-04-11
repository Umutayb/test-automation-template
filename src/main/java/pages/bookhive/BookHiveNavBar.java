package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class BookHiveNavBar {

    @FindBy(css = "[data-testid='nav-login']")
    public WebElement loginLink;

    @FindBy(css = "[data-testid='nav-signup']")
    public WebElement signUpLink;

    @FindBy(css = "[data-testid='nav-cart']")
    public WebElement cartLink;

    @FindBy(css = "[data-testid='nav-orders']")
    public WebElement ordersLink;

    @FindBy(css = "[data-testid='nav-sell']")
    public WebElement sellLink;

    @FindBy(css = "[data-testid='nav-profile']")
    public WebElement profileLink;

    @FindBy(css = "[data-testid='logout-btn']")
    public WebElement logoutButton;

    @FindBy(css = "[data-testid='user-balance']")
    public WebElement userBalance;

    @FindBy(css = "[data-testid='nav-all-books']")
    public WebElement allBooksLink;

    @FindBy(css = "[data-testid='nav-marketplace']")
    public WebElement marketplaceLink;
}
