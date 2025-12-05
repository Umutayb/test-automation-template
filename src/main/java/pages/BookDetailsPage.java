package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class BookDetailsPage extends PickleibPageObject {

    @FindBy(css = "[class='mt-2 row'] [class='col-md-3 col-sm-12']")
    public List<WebElement> labels;

    @FindBy(css = "[class='mt-2 row'] [class='col-md-9 col-sm-12']")
    public List<WebElement> values;

}
