package pages;

import common.PageObject;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.components.BookDetailRow;
import pages.components.BookRow;
import pickleib.enums.ElementState;

import java.util.List;

public class ProfilePage extends PageObject {

    @FindBy(css = ".rt-tr-group")
    public List<BookRow> bookRows;

    @FindBy(css = "[class='mt-2 row']")
    public List<BookDetailRow> bookDetailRows;

    @FindBy(css = ".rt-tr")
    public BookRow bookRow;

    @FindBy(css = "[class='rt-tr -odd']")
    public List<WebElement> bookSections;

    @FindBy(css = "[class='mr-2'] a")
    public List<WebElement> bookTitles;

    @FindBy(css = ".main-header")
    public WebElement mainHeader;

    @FindBy(id = "addNewRecordButton")
    public WebElement backToBookStoreButton;

    public BookDetailRow getDetailRow(String label){
        for (BookDetailRow bookDetailRow:bookDetailRows) {

            if (elementIs(bookDetailRow.label, ElementState.enabled) && bookDetailRow.getLabel().replaceAll(":", "").trim().equals(label))
                return bookDetailRow;
        }
        throw new RuntimeException("Label not found!");
    }

    public BookRow getBookRow(String bookTitle){
        log.warning("SIZE: " + bookRows.size());
        for (BookRow bookRow:bookRows) {
            log.warning("TITLE: " + bookRow.getTitle());
            log.warning("TEXT: " + bookRow.getText());
            if (bookRow.getTitle().equalsIgnoreCase(bookTitle)) return bookRow;
        }
        return null;
    }

    public String getRowAttribute(String bookTitle, String bookAttribute){
        return getBookRow(bookTitle).cells.get(2).getText();
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
