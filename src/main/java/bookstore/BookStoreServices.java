package bookstore;

import bookstore.models.*;
import retrofit2.Call;
import retrofit2.http.*;

import static bookstore.BookStoreApi.*;

public interface BookStoreServices {
    String BASE_URL = BookStoreApi.BASE_URL;

    @GET(BOOKS_SUFFIX)
    Call<BookListModel> getBooks();

    interface Authorisation {
        String BASE_URL = BookStoreApi.BASE_URL;
        @POST(USER_SUFFIX)
        Call<UserResponseModel> createUser(@Body CredentialModel body);
        @POST(TOKEN_SUFFIX)
        Call<TokenResponseModel> generateToken(@Body CredentialModel body);
    }

    interface Authorised {
        String BASE_URL = BookStoreApi.BASE_URL;
        @GET(USER_SUFFIX + USER_ID)
        Call<UserResponseModel> getUser(@Path("UUID") String userId);
        @POST(BOOKS_SUFFIX)
        Call<Object> postBooks(@Body CollectionOfIsbnsModel books);
    }
}
