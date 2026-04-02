package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

@PageObject
public class DropdownPage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "select.styled-select:not([multiple])")
    public WebElement singleSelect;

    @FindBy(css = "select.styled-select[multiple]")
    public WebElement multiSelect;

    @FindBy(css = ".custom-dd-trigger")
    public WebElement customDropdownButton;

    @FindBy(css = ".value-display")
    public List<WebElement> valueDisplays;

    @FindBy(css = "select.styled-select:not([multiple]) option")
    public List<WebElement> singleSelectOptions;
}
