package bookhive;

import bookhive.models.*;
import context.ContextStore;
import okhttp3.Headers;
import retrofit2.Call;
import wasapi.WasapiClient;
import wasapi.WasapiUtilities;

import java.util.List;
import java.util.Map;

/**
 * BookHive API client using Wasapi (Retrofit).
 * Provides static methods for unauthenticated calls (admin, books, auth)
 * and instance methods for authenticated calls (cart, orders, marketplace).
 */
public class BookHiveApi extends WasapiUtilities {

    private static final BookHiveServices publicApi;
    private static final BookHiveServices.Auth authApi;
    private final BookHiveServices.Authenticated authenticatedApi;

    static {
        String baseUrl = ContextStore.get("bookhive-url", "http://localhost:3000/");
        if (!baseUrl.endsWith("/")) baseUrl += "/";
        BookHiveEndpoints.BASE_URL = baseUrl;

        publicApi = new WasapiClient.Builder().build(BookHiveServices.class);
        authApi = new WasapiClient.Builder().build(BookHiveServices.Auth.class);
    }

    /**
     * Creates an authenticated API client using the token from ContextStore.
     */
    public BookHiveApi() {
        String token = ContextStore.get("bookhive-token");
        authenticatedApi = new WasapiClient.Builder()
                .headers(new Headers.Builder()
                        .add("Authorization", "Bearer " + token)
                        .build())
                .build(BookHiveServices.Authenticated.class);
    }

    /**
     * Creates an authenticated API client with the given token.
     */
    public BookHiveApi(String token) {
        authenticatedApi = new WasapiClient.Builder()
                .headers(new Headers.Builder()
                        .add("Authorization", "Bearer " + token)
                        .build())
                .build(BookHiveServices.Authenticated.class);
    }

    // ── Admin (static, no auth) ─────────────────────────────────────────

    public static Map<String, String> resetDatabase() {
        return perform(publicApi.reset(), true, true);
    }

    public static Map<String, String> seedDatabase() {
        return perform(publicApi.seed(), true, true);
    }

    public static Map<String, String> healthCheck() {
        return perform(publicApi.health(), true, true);
    }

    // ── Auth (static, no auth) ──────────────────────────────────────────

    public static BookHiveUser signup(SignupRequest request) {
        return perform(authApi.signup(request), true, true);
    }

    public static BookHiveUser login(LoginRequest request) {
        return perform(authApi.login(request), true, true);
    }

    // ── Books (static, no auth) ─────────────────────────────────────────

    public static BookPage getBooks(String query, String genre, int page, int size) {
        return perform(publicApi.getBooks(query, genre, page, size), true, true);
    }

    public static Book getBook(String bookId) {
        return perform(publicApi.getBook(bookId), true, true);
    }

    // ── Marketplace (static, no auth for listing) ───────────────────────

    public static List<MarketplaceListing> getListings() {
        return perform(publicApi.getListings(), true, true);
    }

    // ── Profile (authenticated, instance methods) ───────────────────────

    public BookHiveUser getProfile() {
        return perform(authenticatedApi.getProfile(), true, true);
    }

    public void logout() {
        perform(authenticatedApi.logout(), true, true);
    }

    // ── Cart (authenticated) ────────────────────────────────────────────

    public List<CartItem> getCart() {
        return perform(authenticatedApi.getCart(), true, true);
    }

    public CartItem addToCart(CartItemRequest request) {
        return perform(authenticatedApi.addToCart(request), true, true);
    }

    public void clearCart() {
        perform(authenticatedApi.clearCart(), false, false);
    }

    // ── Orders (authenticated) ──────────────────────────────────────────

    public List<Order> getOrders() {
        return perform(authenticatedApi.getOrders(), true, true);
    }

    public Order getOrder(String orderId) {
        return perform(authenticatedApi.getOrder(orderId), true, true);
    }

    public Order checkout() {
        return perform(authenticatedApi.checkout(), true, true);
    }

    public void returnOrder(String orderId) {
        perform(authenticatedApi.returnOrder(orderId), true, true);
    }

    // ── Marketplace (authenticated) ─────────────────────────────────────

    public MarketplaceListing createListing(ListingRequest request) {
        return perform(authenticatedApi.createListing(request), true, true);
    }

    public void buyListing(String listingId) {
        perform(authenticatedApi.buyListing(listingId), true, true);
    }

    public void cancelListing(String listingId) {
        perform(authenticatedApi.cancelListing(listingId), true, true);
    }
}
