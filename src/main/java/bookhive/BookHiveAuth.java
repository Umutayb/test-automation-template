package bookhive;

import bookhive.models.*;
import retrofit2.Call;
import wasapi.WasapiClient;
import wasapi.WasapiUtilities;

/**
 * Static authentication helper for BookHive API.
 * Handles user creation and login — no auth token required.
 */
public class BookHiveAuth extends WasapiUtilities {

    static BookHiveServices.Auth authService = new WasapiClient.Builder().build(BookHiveServices.Auth.class);

    public static BookHiveUser signup(SignupRequest request) {
        log.info("Creating a new BookHive user: " + request.getUsername());
        Call<BookHiveUser> call = authService.signup(request);
        return perform(call, true, true);
    }

    public static BookHiveUser login(LoginRequest request) {
        log.info("Logging in as: " + request.getEmail());
        Call<BookHiveUser> call = authService.login(request);
        return perform(call, true, true);
    }
}
