package pages;

import pickleib.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

@PageObject
public class GalleryPage extends PickleibPageObject {

    @FindBy(css = "main h1")
    public WebElement pageTitle;

    @FindBy(css = "[data-testid='gallery-item-1']")
    public WebElement firstImage;

    @FindBy(css = "main [data-testid^='gallery-item-']")
    public List<WebElement> galleryItems;

    @FindBy(css = "[data-testid='gallery-caption']")
    public WebElement galleryCaption;

    @FindBy(css = "[data-testid='gallery-close']")
    public WebElement closeButton;

    @FindBy(css = "[data-testid='gallery-next']")
    public WebElement nextButton;
}
