package pages;

import common.PageObject;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pickleib.enums.ElementState;

import java.util.List;

public class ProfilePage extends PageObject {

    @FindBy(css = ".rt-tr-group .rt-td")
    public List<WebElement> bookRowCells;

    @FindBy(css = ".rt-resizable-header-content")
    public List<WebElement> sectionNames;

    @FindBy(css = "[class='mt-2 row'] [class='col-md-3 col-sm-12']")
    public List<WebElement> labels;

    @FindBy(css = "[class='mt-2 row'] [class='col-md-9 col-sm-12']")
    public List<WebElement> values;

    @FindBy(css = "[class='rt-tr -odd']")
    public List<WebElement> bookSections;

    @FindBy(css = "[class='mr-2'] a")
    public List<WebElement> bookTitles;

    @FindBy(css = ".main-header")
    public WebElement mainHeader;

    @FindBy(id = "addNewRecordButton")
    public WebElement backToBookStoreButton;

    public String getDetailRow(String label){
        for (WebElement targetLabel : labels) {
            if (targetLabel.getText().equals(label))
                return targetLabel.getText().replaceAll(":", "").trim();
        }
        throw new RuntimeException("Label not found!");
    }

    public WebElement getBookRow(String bookTitle){
        log.warning("SIZE: " + bookRowCells.size());
        for (WebElement bookRowCell : bookRowCells) {
            log.warning("TEXT: " + bookRowCell.getText());
            if (bookRowCell.getText().equalsIgnoreCase(bookTitle)) return bookRowCell;
        }
        throw new RuntimeException("BookRowCell not found!");
    }

    public WebElement getBookTitle(String title) {
        for (WebElement bookTitle : bookTitles)
            if (bookTitle.getText().equals(title))
                return bookTitle;
        throw new RuntimeException("Title not found");
    }

    public void waitForList(List<WebElement> targetList) {
        long initialTime = System.currentTimeMillis();
        WebDriverException caughtException = null;
        boolean timeout;
        int counter = 0;
        do {
            timeout = System.currentTimeMillis()-initialTime > elementTimeout;
            try {
                if (targetList.isEmpty())
                    throw new WebDriverException("The list is empty!");
                return;
            }
            catch (WebDriverException webDriverException){
                if (counter == 0) {
                    log.warning("Iterating... (" + webDriverException.getClass().getName() + ")");
                    caughtException = webDriverException;
                }
                else if (!webDriverException.getClass().getName().equals(caughtException.getClass().getName())){
                    log.warning("Iterating... (" + webDriverException.getClass().getName() + ")");
                    caughtException = webDriverException;
                }
                counter++;
            }
        }
        while (!timeout);
        if (counter > 0) log.warning("Iterated " + counter + " time(s)!");
        log.warning(caughtException.getMessage());
    }
}
