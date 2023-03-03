package pages.demoqa;

import org.openqa.selenium.support.FindBy;
import pages.demoqa.components.BookDetailRow;
import pages.demoqa.components.BookRow;
import utils.WebUtilities;

import java.util.List;

public class ProfilePage extends WebUtilities {

    @FindBy(css = ".rt-tr-group")
    public List<BookRow> bookRows;

    @FindBy(css = "[class='mt-2 row']")
    public List<BookDetailRow> bookDetailRows;

    public BookDetailRow getDetailRow(String label){
        for (BookDetailRow bookDetailRow:bookDetailRows) {
            if (bookDetailRow.label.getText().equals(label)) return bookDetailRow;
        }
        return null;
    }

    public BookRow getBookRow(String bookTitle){
        log.new Warning("SIZE: " + bookRows.size());
        for (BookRow bookRow:bookRows) {
            log.new Warning("TITLE: " + bookRow.getTitle());
            log.new Warning("TEXT: " + bookRow.getText());
            if (bookRow.getTitle().equalsIgnoreCase(bookTitle)) return bookRow;
        }
        return null;
    }

    public String getRowAttribute(String bookTitle, String bookAttribute){
        return getBookRow(bookTitle).cells.get(2).getText();
    }
}
