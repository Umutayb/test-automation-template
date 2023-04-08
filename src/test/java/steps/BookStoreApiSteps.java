package steps;

import bookstore.BookStore;
import bookstore.models.*;
import context.ContextStore;
import io.cucumber.java.en.Given;

import java.util.ArrayList;
import java.util.List;

public class BookStoreApiSteps {

    BookStore bookStore = new BookStore();
    @Given("Get all the books from database")
    public void getBooks() {
        BookListModel books = bookStore.getAllBooks();
        ContextStore.put("books", books);

    }
    @Given("Get user by Id: {}")
    public void getUserById(String userId) {
        UserResponseModel userResponse = bookStore.getUser(userId);
        ContextStore.put("userResponse", userResponse);
    }
    @Given("Get the user in context")
    public void getContextUser() {
        UserResponseModel userResponse = new UserResponseModel();
        getUserById(ContextStore.get("userId").toString());
    }

    @Given("Get all books on bookstore and add by publisher named {} to user in context")
    public void postBooks(String publisher) {
        CollectionOfIsbnsModel books = new CollectionOfIsbnsModel();
        List<IsbnModel> isbnList = new ArrayList<>();

        for (BookModel book : bookStore.getAllBooks().getBooks()){
            if (book.getPublisher().equals(publisher))
                isbnList.add(new IsbnModel(book.getIsbn()));
        }

        books.setUserId(ContextStore.get("userId").toString());
        books.setCollectionOfIsbns(isbnList);
        bookStore.postBooks(books);
    }

}
