package bookStore;

import models.bookStore.Account;
import models.bookStore.Book;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BookStoreServices {

    String BASE_URL = BookStoreApi.BASE_URL;

    @POST(BASE_URL + BookStoreApi.ACCOUNT + BookStoreApi.USER)
    Call<Account> createUser(@Body Account body);

    @POST(BASE_URL + BookStoreApi.ACCOUNT + BookStoreApi.GENERATE_TOKEN)
    Call<Account> generateToken(@Body Account body);

    @GET(BASE_URL + BookStoreApi.BOOKSTORE)
    Call<Account> getAllBooks();

    @POST(BASE_URL + BookStoreApi.BOOKSTORE)
    Call<Book> postBooksToUser(
            @Body Book body,
            @Header("Authorization") String header
    );

}
