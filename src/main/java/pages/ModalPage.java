package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@PageObject
public class ModalPage {

    @FindBy(css = "h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='modal-open']")
    public WebElement openModalButton;

    @FindBy(css = "[data-testid='modal-status']")
    public WebElement statusText;
}
