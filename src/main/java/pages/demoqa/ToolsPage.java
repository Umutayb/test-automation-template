package pages.demoqa;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.demoqa.components.DatePicker;
import pages.demoqa.components.PracticeForm;
import pages.demoqa.components.ResultModel;
import utils.WebUtilities;
import java.util.List;

public class ToolsPage extends WebUtilities {

    @FindBy(css = ".element-group")
    List<WebElement> categories;

    @FindBy(css = ".menu-list li")
    List<WebElement> tools;

    @FindBy(css = ".practice-form-wrapper")
    PracticeForm practiceForm;

    @FindBy(css = ".react-datepicker__month-container")
    DatePicker datePicker;

    @FindBy(css = ".modal-content")
    ResultModel resultModel;
}
