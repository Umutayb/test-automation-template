package bookstore;

import api_assured.ApiUtilities;
import api_assured.ServiceGenerator;
import bookstore.models.CredentialModel;
import bookstore.models.TokenResponseModel;
import bookstore.models.UserResponseModel;
import retrofit2.Call;
import retrofit2.http.Headers;
import utils.Printer;

public class BookStoreAuthorisation extends ApiUtilities {
    static BookStoreServices.Authorisation bookStore = new ServiceGenerator().generate(BookStoreServices.Authorisation.class);
    static Printer log = new Printer(BookStoreAuthorisation.class);

    public static UserResponseModel createUser(CredentialModel user) {
        log.new Info("Creating a new user");
        Call<UserResponseModel> userCall = bookStore.createUser(user);
        return perform(userCall, true, true);
    }

    public static TokenResponseModel generateToken(CredentialModel user) {
        log.new Info("Generation token for the user in context");
        Call<TokenResponseModel> tokenCall = bookStore.generateToken(user);
        return perform(tokenCall, true, true);
    }

}
