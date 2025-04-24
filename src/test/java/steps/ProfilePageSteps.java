package steps;

import bookstore.models.BookModel;
import bookstore.models.UserResponseModel;
import common.PageObject;
import context.ContextStore;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import pages.ProfilePage;

import java.util.List;
import static utils.StringUtilities.Color.*;

public class ProfilePageSteps extends PageObject {

    ProfilePage profilePage = new ProfilePage();

    //TODO: check
    //@Given("Print book attribute")
    //public void printAttribute(){
    //    profilePage.getRowAttribute("Git Pocket Guide","");
    //}

    @Given("Verify the book info for the user in context")
    public void verifyBookDetails() {
        List<BookModel> bookList = ((UserResponseModel) ContextStore.get("userResponse")).getBooks();

        profilePage.waitForList(profilePage.bookTitles);

        for (BookModel book : bookList) {
            clickElement(profilePage.getBookTitle(book.getTitle()), true);

            String expectedIsbn = book.getIsbn().trim();
            String actualIsbn = profilePage.getDetailRow("ISBN");
            Assert.assertEquals("Isbn not match!",expectedIsbn,actualIsbn);
            log.success("Isbn matches for book named: " +BLUE.getValue()+book.getTitle());

            String expectedPublisher = book.getPublisher().trim();
            String actualPublisher = profilePage.getDetailRow("Publisher");
            Assert.assertEquals("Publisher not match!", expectedPublisher, actualPublisher);
            log.success("Publisher matches for book named: " +BLUE.getValue()+book.getTitle());

            String expectedAuthor = book.getAuthor().trim();
            String actualAuthor = profilePage.getDetailRow("Author");
            Assert.assertEquals("Author not match!", expectedAuthor, actualAuthor);
            log.success("Author matches for book named: " +BLUE.getValue()+book.getTitle());

            String expectedTitle = book.getTitle().trim();
            String actualTitle = profilePage.getDetailRow("Title");
            Assert.assertEquals("Title not match", expectedTitle, actualTitle);
            log.success("Title matches for book named: " +BLUE.getValue()+book.getTitle());

            String expectedDescription = book.getDescription().trim();
            String actualDescription = profilePage.getDetailRow("Description");
            Assert.assertEquals("Description not match!", expectedDescription, actualDescription);
            log.success("Description matches for book named: " +BLUE.getValue()+book.getTitle());

            String expectedWebsite = book.getWebsite().trim();
            String actualWebsite = profilePage.getDetailRow("Website");
            Assert.assertEquals("Website not match", expectedWebsite, actualWebsite);
            log.success("Website matches for book named: " +BLUE.getValue()+book.getTitle());

            String expectedSubTitle = book.getSubTitle().trim();
            String actualSubTitle = profilePage.getDetailRow("Sub Title");
            Assert.assertEquals("SubTitle not match!", expectedSubTitle, actualSubTitle);
            log.success("SubTitle matches for book named: " +BLUE.getValue()+book.getTitle());

            Integer expectedPages = book.getPages();
            Integer  actualPages = Integer.valueOf(profilePage.getDetailRow("Total Pages"));
            Assert.assertEquals("Page number not match!", expectedPages, actualPages);
            log.success("Page number matches for book named: " +BLUE.getValue()+book.getTitle());

            log.success("Publish date for " +BLUE.getValue()+book.getTitle() +GRAY.getValue()+" is: " +PURPLE.getValue()+book.getPublish_date().substring(0,10));

            clickElement(profilePage.backToBookStoreButton, true);
            profilePage.waitForList(profilePage.bookTitles);
        }
    }

}
