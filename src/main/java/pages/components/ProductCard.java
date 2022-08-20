package pages.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.WebUtilities;

public class ProductCard extends WebUtilities {

    @FindBy(tagName = "h3")
    public WebElement title;

    @FindBy(tagName = "img")
    public WebElement image;

    @FindBy(css = "a[class*='content-header']")
    public WebElement header;

    @FindBy(css = "[class*='price']")
    public WebElement price;

    public String getImageSource(){ return image.getAttribute("src"); }

    public String getTitleText(){ return title.getText(); }

    public String getHeaderText(){ return header.getText(); }

    public String getPrice(){ return price.getText(); }
}
