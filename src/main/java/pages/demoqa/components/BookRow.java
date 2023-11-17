package pages.demoqa.components;

import com.github.webdriverextensions.WebComponent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class BookRow extends WebComponent {

    @FindBy(css = ".rt-td")
    public List<WebElement> cells;

    @FindBy(css = ".rt-resizable-header-content")
    public List<WebElement> sectionNames;

    public String getTitle(){
        return cells.get(1).getText();
    }

    public String getAuthor(){
        return cells.get(2).getText();
    }

    public String getPublisher(){
        return cells.get(3).getText();
    }

}
