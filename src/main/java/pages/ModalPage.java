package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

public class ModalPage extends PickleibPageObject {

    @FindBy(css = "h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='modal-open']")
    public WebElement openModalButton;

    @FindBy(css = "[data-testid='modal-status']")
    public WebElement statusText;
}
