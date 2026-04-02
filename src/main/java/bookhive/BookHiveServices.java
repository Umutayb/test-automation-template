package bookhive;

import bookhive.models.*;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

import static bookhive.BookHiveEndpoints.*;

/**
 * Retrofit service interfaces for the BookHive API.
 * Separated into three interfaces by authentication context.
 */
public interface BookHiveServices {

    String BASE_URL = BookHiveEndpoints.BASE_URL;

    // ── Admin (no auth) ─────────────────────────────────────────────────

    @POST(RESET)
    Call<Map<String, String>> reset();

    @POST(SEED)
    Call<Map<String, String>> seed();

    @GET(HEALTH)
    Call<Map<String, String>> health();

    // ── Books (no auth) ─────────────────────────────────────────────────

    @GET(BOOKS)
    Call<BookPage> getBooks(
            @Query("query") String query,
            @Query("genre") String genre,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET(BOOK_BY_ID)
    Call<Book> getBook(@Path("id") String bookId);

    // ── Marketplace (no auth for listing) ───────────────────────────────

    @GET(MARKETPLACE)
    Call<List<MarketplaceListing>> getListings();

    // ── Auth ─────────────────────────────────────────────────────────────

    interface Auth {
        String BASE_URL = BookHiveEndpoints.BASE_URL;

        @POST(AUTH_SIGNUP)
        Call<BookHiveUser> signup(@Body SignupRequest body);

        @POST(AUTH_LOGIN)
        Call<BookHiveUser> login(@Body LoginRequest body);
    }

    // ── Authenticated ───────────────────────────────────────────────────

    interface Authenticated {
        String BASE_URL = BookHiveEndpoints.BASE_URL;

        @GET(AUTH_ME)
        Call<BookHiveUser> getProfile();

        @POST(AUTH_LOGOUT)
        Call<Map<String, String>> logout();

        // Cart
        @GET(CART)
        Call<List<CartItem>> getCart();

        @POST(CART_ITEMS)
        Call<CartItem> addToCart(@Body CartItemRequest body);

        @DELETE(CART)
        Call<Void> clearCart();

        // Orders
        @GET(ORDERS)
        Call<List<Order>> getOrders();

        @GET(ORDER_BY_ID)
        Call<Order> getOrder(@Path("id") String orderId);

        @POST(ORDERS)
        Call<Order> checkout();

        @POST(ORDER_RETURN)
        Call<Map<String, String>> returnOrder(@Path("id") String orderId);

        // Marketplace
        @POST(MARKETPLACE_LISTINGS)
        Call<MarketplaceListing> createListing(@Body ListingRequest body);

        @POST(MARKETPLACE_LISTING_BUY)
        Call<Map<String, String>> buyListing(@Path("id") String listingId);

        @DELETE(MARKETPLACE_LISTING_BY_ID)
        Call<Map<String, String>> cancelListing(@Path("id") String listingId);
    }
}
