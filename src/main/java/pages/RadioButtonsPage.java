package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@PageObject
public class RadioButtonsPage {

    @FindBy(id = "yes")
    public WebElement yesRadio;

    @FindBy(id = "impressive")
    public WebElement impressiveRadio;

    @FindBy(id = "no")
    public WebElement noRadio;

    @FindBy(css = ".radio-label")
    public List<WebElement> radioLabels;

    @FindBy(css = "input[type='radio']")
    public List<WebElement> radioButtons;

    @FindBy(css = "h2")
    public WebElement questionTitle;
}
