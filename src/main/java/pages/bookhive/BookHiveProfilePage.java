package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class BookHiveProfilePage {

    @FindBy(css = "[data-testid='profile-username']")
    public WebElement username;

    @FindBy(css = "[data-testid='profile-email']")
    public WebElement email;

    @FindBy(css = "[data-testid='profile-balance']")
    public WebElement balance;

    @FindBy(css = "[data-testid='no-listings']")
    public WebElement noListingsMessage;
}
