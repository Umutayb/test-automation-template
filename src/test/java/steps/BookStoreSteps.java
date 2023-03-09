package steps;

import bookStore.BookStore;
import context.ContextStore;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.bookStore.Account;
import models.bookStore.Book;
import models.bookStore.CollectionOfIsbns;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import pages.demoqa.LoginPage;
import utils.WebUtilities;

import java.util.ArrayList;
import java.util.List;

import static utils.WebUtilities.Color.PURPLE;
import static utils.WebUtilities.Color.RED;

public class BookStoreSteps extends WebUtilities {

    BookStore bookStore = new BookStore();
    LoginPage loginPage = new LoginPage();
    List<Book> bookList;

    @Given("Create a user")
    public void createUser() {
        log.new Info("Running: " + highlighted(PURPLE,"Create a user"));
        Account accountObject = new Account();
        String username = "testUser" + RandomStringUtils.randomAlphanumeric(6);
        String password = "String123321*-";
        accountObject.setUserName(username);
        accountObject.setPassword(password);
        log.new Info("Username: " + highlighted(RED, username));
        log.new Info("Password: " + highlighted(PURPLE, password));

        ContextStore.put("username", username);
        ContextStore.put("password", password);
        ContextStore.put("accountObject", accountObject);

        Account account = bookStore.createUserAccount(accountObject);
        System.out.println("account.getUserID() = " + account.getUserID());
        System.out.println("account.getResponseUsername() = " + account.getResponseUsername());
        ContextStore.put("account", account);
        ContextStore.put("userID", account.getUserID());
    }

    @And("Generate a token")
    public void generateAToken() {
        log.new Info("Running: " + highlighted(PURPLE,"Generate a token"));
        String token = bookStore.generateToken((Account) ContextStore.get("accountObject")).getToken();
        log.new Info("Token: " + highlighted(RED,token));
        ContextStore.put("token", token);
    }

    @And("Get list of all books")
    public void getListOfAllBooks() {
        log.new Info("Running: " + highlighted(PURPLE,"Get list of all books"));
        bookList = bookStore.getAllBooks().getBooks();
        ContextStore.put("books", bookList);
    }

    @When("Post all books to the user")
    public void postAllBooksToTheUser() {
        log.new Info("Running: " + highlighted(PURPLE,"Post all books to the user"));
        CollectionOfIsbns collection = new CollectionOfIsbns();
        List<CollectionOfIsbns> list = new ArrayList<>();

        Book book = new Book();
        book.setUserId(ContextStore.get("userID").toString());

        for (Book item : bookList) {
            collection.setIsbn(item.getIsbn());
            list.add(collection);
            book.setCollectionOfIsbns(list);
            bookStore.postBooksToUser(book, "Bearer " + ContextStore.get("token"));
            list.clear();
        }

    }

    @Given("Navigate to the url: {}")
    public void getUrl(String url) {
        navigate(url);
    }

    @And("Log in to the page")
    public void logInToThePage() {
        log.new Info("Running: " + highlighted(PURPLE,"Log in to the page"));
        loginPage.login(ContextStore.get("username").toString(), ContextStore.get("password").toString());
    }

    @Then("Assert the details of the books")
    public void assertTheDetailsOfTheBooks() {
        log.new Info("Running: " + highlighted(PURPLE,"Assert the details of the books"));
        loginPage.selectRows10();
        int i = 0;
        for (Book item : bookList) {
            Assert.assertEquals(item.getTitle(), loginPage.findElementsTitle().get(i).getText());
            Assert.assertEquals(item.getAuthor(), loginPage.getAuthor(i + 1));
            Assert.assertEquals(item.getPublisher(), loginPage.getPublisher(i + 1));
            i++;
        }
    }
}
