package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.web.PickleibPageObject;

import java.util.List;

public class BookStorePage extends PickleibPageObject {

    @FindBy(css = ".rt-tr-group .rt-td")
    public List<WebElement> bookRowCells;

    @FindBy(css = ".rt-resizable-header-content")
    public List<WebElement> sectionNames;

}
