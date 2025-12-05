package bookstore;

import bookstore.models.*;
import context.ContextStore;
import okhttp3.Headers;
import retrofit2.Call;
import wasapi.WasapiClient;
import wasapi.WasapiUtilities;

import static java.awt.Color.*;

public class BookStore extends WasapiUtilities {

    BookStoreServices.Authorised bookStoreAuthorized;
    BookStoreServices bookStore;

    public BookStore(){
        bookStoreAuthorized = new WasapiClient.Builder()
                .headers(new Headers.Builder().add(
                        "Authorization",
                                "Bearer " + ContextStore.get("token").toString()
                        ).build()
                )
                .build(BookStoreServices.Authorised.class);
        bookStore = new WasapiClient.Builder().build(BookStoreServices.class);
    }

    public BookListModel getAllBooks() {
        log.info("Getting all books on the bookstore");
        Call<BookListModel> bookCall = bookStore.getBooks();
        return perform(bookCall, true, true);
    }
    public UserResponseModel getUser(String userId) {
        log.info("Getting user by Id: " + BLUE+userId);
        Call<UserResponseModel> userCall = bookStoreAuthorized.getUser(userId);
        return perform(userCall, true, true);
    }

    public Object postBooks(CollectionOfIsbnsModel books) {
        log.info("Posting selected books to context user");
        Call<Object> bookCall = bookStoreAuthorized.postBooks(books);
        return perform(bookCall, true, true);
    }

}
