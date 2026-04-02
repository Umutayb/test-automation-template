package steps;

import bookhive.BookHiveApi;
import bookhive.BookHiveAuth;
import bookhive.models.*;
import context.ContextStore;
import io.cucumber.java.en.Given;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for BookHive API operations used in cross-functional testing.
 * Follows the same pattern as BookStoreApiSteps — one API instance per step class,
 * static auth via BookHiveAuth, business operations via BookHiveApi.
 */
public class BookHiveSteps {

    // ─── Admin ──────────────────────────────────────────────────────────

    @Given("Reset BookHive database")
    public void resetDatabase() {
        // Admin calls don't need auth — use a temporary public-only client
        BookHiveApi bookHive = publicApi();
        bookHive.resetDatabase();
    }

    @Given("Seed BookHive database")
    public void seedDatabase() {
        BookHiveApi bookHive = publicApi();
        bookHive.seedDatabase();
    }

    // ─── Auth ───────────────────────────────────────────────────────────

    @Given("Create BookHive user {} with email {} and password {}")
    public void createUser(String username, String email, String password) {
        BookHiveUser user = BookHiveAuth.signup(new SignupRequest(username, email, password));
        ContextStore.put("bookhive-token", user.getToken());
        ContextStore.put("bookhive-userId", user.getUserId());
        ContextStore.put("bookhive-username", user.getUsername());
    }

    @Given("Login to BookHive API as {} with password {}")
    public void loginApi(String email, String password) {
        BookHiveUser user = BookHiveAuth.login(new LoginRequest(email, password));
        ContextStore.put("bookhive-token", user.getToken());
        ContextStore.put("bookhive-userId", user.getUserId());
        ContextStore.put("bookhive-username", user.getUsername());
    }

    @Given("Verify BookHive API profile username is {}")
    public void verifyProfile(String expectedUsername) {
        BookHiveApi bookHive = new BookHiveApi();
        BookHiveUser profile = bookHive.getProfile();
        assertEquals(expectedUsername, profile.getUsername(), "Profile username mismatch");
    }

    // ─── Books ──────────────────────────────────────────────────────────

    @Given("Verify BookHive API has books available")
    public void verifyBooksExist() {
        BookHiveApi bookHive = publicApi();
        BookPage books = bookHive.getBooks(null, null, 0, 12);
        assertTrue(books.getTotalElements() > 0, "No books found in API");
    }

    @Given("Get BookHive book details for {} via API")
    public void getBookDetails(String bookId) {
        BookHiveApi bookHive = publicApi();
        Book book = bookHive.getBook(bookId);
        ContextStore.put("bookTitle", book.getTitle());
        ContextStore.put("bookAuthor", book.getAuthor());
        ContextStore.put("bookPrice", "$" + String.format(Locale.US, "%.2f", book.getPrice()));
        ContextStore.put("bookGenre", book.getGenre());
    }

    // ─── Cart ───────────────────────────────────────────────────────────

    @Given("Add book {} to BookHive cart via API with quantity {int}")
    public void addToCart(String bookId, int quantity) {
        BookHiveApi bookHive = new BookHiveApi();
        bookHive.addToCart(new CartItemRequest(bookId, quantity));
    }

    @Given("Verify BookHive API cart has {int} items")
    public void verifyCartSize(int expectedSize) {
        BookHiveApi bookHive = new BookHiveApi();
        List<CartItem> cart = bookHive.getCart();
        assertEquals(expectedSize, cart.size(),
                "Cart size mismatch. Expected " + expectedSize + " but got " + cart.size());
    }

    @Given("Clear BookHive cart via API")
    public void clearCart() {
        BookHiveApi bookHive = new BookHiveApi();
        bookHive.clearCart();
    }

    // ─── Orders ─────────────────────────────────────────────────────────

    @Given("Checkout BookHive cart via API")
    public void checkout() {
        BookHiveApi bookHive = new BookHiveApi();
        Order order = bookHive.checkout();
        ContextStore.put("bookhive-orderId", order.getId());
    }

    @Given("Verify BookHive API has {int} orders")
    public void verifyOrderCount(int expectedCount) {
        BookHiveApi bookHive = new BookHiveApi();
        List<Order> orders = bookHive.getOrders();
        assertEquals(expectedCount, orders.size(),
                "Order count mismatch. Expected " + expectedCount + " but got " + orders.size());
    }

    // ─── Marketplace ────────────────────────────────────────────────────

    @Given("Create BookHive marketplace listing for book {} in {} condition at price {double}")
    public void createListing(String bookId, String condition, double price) {
        BookHiveApi bookHive = new BookHiveApi();
        bookHive.createListing(new ListingRequest(bookId, condition, price));
    }

    @Given("Verify BookHive API marketplace has listings")
    public void verifyMarketplaceHasListings() {
        BookHiveApi bookHive = publicApi();
        List<MarketplaceListing> listings = bookHive.getListings();
        assertFalse(listings.isEmpty(), "No marketplace listings found");
    }

    // ─── Helpers ────────────────────────────────────────────────────────

    /**
     * Creates a public-only API client for unauthenticated calls.
     * Uses a dummy token since the constructor requires one,
     * but only public endpoints are called.
     */
    private BookHiveApi publicApi() {
        if (ContextStore.get("bookhive-token") == null)
            ContextStore.put("bookhive-token", "none");
        return new BookHiveApi();
    }
}
