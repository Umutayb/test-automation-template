package pages.bookhive;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

@PageObject
public class BookHiveOrdersPage extends PickleibPageObject {

    @FindBy(css = "[data-testid^='order-']")
    public List<WebElement> orderCards;

    @FindBy(css = "[data-testid='no-orders']")
    public WebElement noOrdersMessage;
}
