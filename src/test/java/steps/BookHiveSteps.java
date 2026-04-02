package steps;

import bookhive.BookHiveApi;
import bookhive.models.*;
import context.ContextStore;
import io.cucumber.java.en.Given;
import utils.Printer;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for BookHive API operations used in cross-functional testing.
 * These steps set up state via the API that is then verified through the UI,
 * and verify API state after UI interactions.
 */
public class BookHiveSteps {

    private final Printer log = new Printer(this.getClass());

    // ─── Admin ──────────────────────────────────────────────────────────

    @Given("Reset BookHive database")
    public void resetDatabase() {
        BookHiveApi.resetDatabase();
        log.info("BookHive database reset");
    }

    @Given("Seed BookHive database")
    public void seedDatabase() {
        BookHiveApi.seedDatabase();
        log.info("BookHive database seeded");
    }

    // ─── Auth ───────────────────────────────────────────────────────────

    @Given("Create BookHive user {} with email {} and password {}")
    public void createUser(String username, String email, String password) {
        BookHiveUser user = BookHiveApi.signup(new SignupRequest(username, email, password));
        ContextStore.put("bookhive-token", user.getToken());
        ContextStore.put("bookhive-userId", user.getUserId());
        ContextStore.put("bookhive-username", user.getUsername());
        log.info("Created BookHive user: " + username);
    }

    @Given("Login to BookHive API as {} with password {}")
    public void loginApi(String email, String password) {
        BookHiveUser user = BookHiveApi.login(new LoginRequest(email, password));
        ContextStore.put("bookhive-token", user.getToken());
        ContextStore.put("bookhive-userId", user.getUserId());
        ContextStore.put("bookhive-username", user.getUsername());
        log.info("Logged in as: " + email);
    }

    @Given("Verify BookHive API profile username is {}")
    public void verifyProfile(String expectedUsername) {
        BookHiveApi api = new BookHiveApi();
        BookHiveUser profile = api.getProfile();
        assertEquals(expectedUsername, profile.getUsername(),
                "Profile username mismatch");
        log.info("Verified profile username: " + expectedUsername);
    }

    // ─── Books ──────────────────────────────────────────────────────────

    @Given("Verify BookHive API has books available")
    public void verifyBooksExist() {
        BookPage books = BookHiveApi.getBooks(null, null, 0, 12);
        assertTrue(books.getTotalElements() > 0, "No books found in API");
        log.info("API has " + books.getTotalElements() + " books");
    }

    @Given("Get BookHive book details for {} via API")
    public void getBookDetails(String bookId) {
        Book book = BookHiveApi.getBook(bookId);
        ContextStore.put("bookTitle", book.getTitle());
        ContextStore.put("bookAuthor", book.getAuthor());
        ContextStore.put("bookPrice", "$" + String.format(Locale.US, "%.2f", book.getPrice()));
        ContextStore.put("bookGenre", book.getGenre());
        log.info("Got book: " + book.getTitle() + " by " + book.getAuthor());
    }

    // ─── Cart ───────────────────────────────────────────────────────────

    @Given("Add book {} to BookHive cart via API with quantity {int}")
    public void addToCart(String bookId, int quantity) {
        BookHiveApi api = new BookHiveApi();
        api.addToCart(new CartItemRequest(bookId, quantity));
        log.info("Added " + quantity + "x " + bookId + " to cart via API");
    }

    @Given("Verify BookHive API cart has {int} items")
    public void verifyCartSize(int expectedSize) {
        BookHiveApi api = new BookHiveApi();
        List<CartItem> cart = api.getCart();
        assertEquals(expectedSize, cart.size(),
                "Cart size mismatch. Expected " + expectedSize + " but got " + cart.size());
        log.info("Verified cart has " + expectedSize + " item(s)");
    }

    @Given("Clear BookHive cart via API")
    public void clearCart() {
        BookHiveApi api = new BookHiveApi();
        api.clearCart();
        log.info("Cart cleared via API");
    }

    // ─── Orders ─────────────────────────────────────────────────────────

    @Given("Checkout BookHive cart via API")
    public void checkout() {
        BookHiveApi api = new BookHiveApi();
        Order order = api.checkout();
        ContextStore.put("bookhive-orderId", order.getId());
        log.info("Checked out order: " + order.getId());
    }

    @Given("Verify BookHive API has {int} orders")
    public void verifyOrderCount(int expectedCount) {
        BookHiveApi api = new BookHiveApi();
        List<Order> orders = api.getOrders();
        assertEquals(expectedCount, orders.size(),
                "Order count mismatch. Expected " + expectedCount + " but got " + orders.size());
        log.info("Verified " + expectedCount + " order(s)");
    }

    // ─── Marketplace ────────────────────────────────────────────────────

    @Given("Create BookHive marketplace listing for book {} in {} condition at price {double}")
    public void createListing(String bookId, String condition, double price) {
        BookHiveApi api = new BookHiveApi();
        api.createListing(new ListingRequest(bookId, condition, price));
        log.info("Created marketplace listing for " + bookId);
    }

    @Given("Verify BookHive API marketplace has listings")
    public void verifyMarketplaceHasListings() {
        List<MarketplaceListing> listings = BookHiveApi.getListings();
        assertFalse(listings.isEmpty(), "No marketplace listings found");
        log.info("Marketplace has " + listings.size() + " listing(s)");
    }
}
