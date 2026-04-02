package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

@PageObject
public class TallPage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = ".scroll-info")
    public WebElement scrollInfo;

    @FindBy(css = ".tall-section")
    public List<WebElement> sections;

    @FindBy(css = ".tall-section h2")
    public List<WebElement> sectionHeadings;
}
