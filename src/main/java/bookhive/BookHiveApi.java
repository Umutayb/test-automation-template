package bookhive;

import bookhive.models.*;
import context.ContextStore;
import okhttp3.Headers;
import retrofit2.Call;
import wasapi.WasapiClient;
import wasapi.WasapiUtilities;

import java.util.List;
import java.util.Map;

import static utils.StringUtilities.Color.*;

/**
 * BookHive API client for authenticated and public operations.
 * Mirrors the BookStore pattern: instance-based with token injection at construction time.
 *
 * Usage:
 * <pre>
 *   // After login, token is in ContextStore
 *   BookHiveApi bookHive = new BookHiveApi();
 *   List<CartItem> cart = bookHive.getCart();
 * </pre>
 */
public class BookHiveApi extends WasapiUtilities {

    BookHiveServices.Authenticated authenticated;
    BookHiveServices publicApi;

    public BookHiveApi() {
        authenticated = new WasapiClient.Builder()
                .headers(new Headers.Builder().add(
                        "Authorization",
                        "Bearer " + ContextStore.get("bookhive-token").toString()
                ).build())
                .build(BookHiveServices.Authenticated.class);
        publicApi = new WasapiClient.Builder().build(BookHiveServices.class);
    }

    // ── Admin ────────────────────────────────────────────────────────────

    public Map<String, String> resetDatabase() {
        log.info("Resetting BookHive database");
        Call<Map<String, String>> call = publicApi.reset();
        return perform(call, true, true);
    }

    public Map<String, String> seedDatabase() {
        log.info("Seeding BookHive database");
        Call<Map<String, String>> call = publicApi.seed();
        return perform(call, true, true);
    }

    public Map<String, String> healthCheck() {
        log.info("Checking BookHive health");
        Call<Map<String, String>> call = publicApi.health();
        return perform(call, true, true);
    }

    // ── Books ────────────────────────────────────────────────────────────

    public BookPage getBooks(String query, String genre, int page, int size) {
        log.info("Getting books" +
                (query != null ? " matching: " + BLUE + query : "") +
                (genre != null ? " genre: " + BLUE + genre : ""));
        Call<BookPage> call = publicApi.getBooks(query, genre, page, size);
        return perform(call, true, true);
    }

    public Book getBook(String bookId) {
        log.info("Getting book by ID: " + BLUE + bookId);
        Call<Book> call = publicApi.getBook(bookId);
        return perform(call, true, true);
    }

    // ── Profile ──────────────────────────────────────────────────────────

    public BookHiveUser getProfile() {
        log.info("Getting user profile");
        Call<BookHiveUser> call = authenticated.getProfile();
        return perform(call, true, true);
    }

    public void logout() {
        log.info("Logging out");
        Call<Map<String, String>> call = authenticated.logout();
        perform(call, true, false);
    }

    // ── Cart ─────────────────────────────────────────────────────────────

    public List<CartItem> getCart() {
        log.info("Getting cart contents");
        Call<List<CartItem>> call = authenticated.getCart();
        return perform(call, true, true);
    }

    public CartItem addToCart(CartItemRequest request) {
        log.info("Adding to cart: " + BLUE + request.getBookId() + GRAY + " x" + request.getQuantity());
        Call<CartItem> call = authenticated.addToCart(request);
        return perform(call, true, true);
    }

    public void clearCart() {
        log.info("Clearing cart");
        Call<Void> call = authenticated.clearCart();
        perform(call, false, false);
    }

    // ── Orders ───────────────────────────────────────────────────────────

    public List<Order> getOrders() {
        log.info("Getting orders");
        Call<List<Order>> call = authenticated.getOrders();
        return perform(call, true, true);
    }

    public Order getOrder(String orderId) {
        log.info("Getting order: " + BLUE + orderId);
        Call<Order> call = authenticated.getOrder(orderId);
        return perform(call, true, true);
    }

    public Order checkout() {
        log.info("Checking out cart");
        Call<Order> call = authenticated.checkout();
        return perform(call, true, true);
    }

    public void returnOrder(String orderId) {
        log.info("Returning order: " + BLUE + orderId);
        Call<Map<String, String>> call = authenticated.returnOrder(orderId);
        perform(call, true, false);
    }

    // ── Marketplace ──────────────────────────────────────────────────────

    public List<MarketplaceListing> getListings() {
        log.info("Getting marketplace listings");
        Call<List<MarketplaceListing>> call = publicApi.getListings();
        return perform(call, true, true);
    }

    public MarketplaceListing createListing(ListingRequest request) {
        log.info("Creating listing for book: " + BLUE + request.getBookId() +
                GRAY + " condition: " + BLUE + request.getCondition() +
                GRAY + " price: $" + request.getPrice());
        Call<MarketplaceListing> call = authenticated.createListing(request);
        return perform(call, true, true);
    }

    public void buyListing(String listingId) {
        log.info("Buying listing: " + BLUE + listingId);
        Call<Map<String, String>> call = authenticated.buyListing(listingId);
        perform(call, true, false);
    }

    public void cancelListing(String listingId) {
        log.info("Cancelling listing: " + BLUE + listingId);
        Call<Map<String, String>> call = authenticated.cancelListing(listingId);
        perform(call, true, false);
    }
}
