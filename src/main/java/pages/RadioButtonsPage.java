package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class RadioButtonsPage extends PickleibPageObject {

    @FindBy(css = ".radio-button")
    public List<WebElement> radioButtons;

    @FindBy(css = ".radio-button label")
    public List<WebElement> radioLabels;
}
