package bookhive;

import bookhive.models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import context.ContextStore;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class BookHiveApi {

    private static final Gson gson = new Gson();
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static String baseUrl() {
        return ContextStore.get("bookhive-api-url", "http://localhost:3000/api");
    }

    // ── Admin ────────────────────────────────────────────────────────────

    public static void resetDatabase() {
        execute(new Request.Builder().url(baseUrl() + "/reset").post(emptyBody()).build());
    }

    public static void seedDatabase() {
        execute(new Request.Builder().url(baseUrl() + "/seed").post(emptyBody()).build());
    }

    public static Map<String, String> healthCheck() {
        String body = execute(new Request.Builder().url(baseUrl() + "/health").get().build());
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        return gson.fromJson(body, type);
    }

    // ── Auth ─────────────────────────────────────────────────────────────

    public static BookHiveUser signup(SignupRequest request) {
        String body = execute(jsonPost(baseUrl() + "/auth/signup", request));
        return gson.fromJson(body, BookHiveUser.class);
    }

    public static BookHiveUser login(LoginRequest request) {
        String body = execute(jsonPost(baseUrl() + "/auth/login", request));
        return gson.fromJson(body, BookHiveUser.class);
    }

    public static void logout(String token) {
        execute(authed(new Request.Builder().url(baseUrl() + "/auth/logout").post(emptyBody()), token).build());
    }

    public static BookHiveUser getProfile(String token) {
        String body = execute(authed(new Request.Builder().url(baseUrl() + "/auth/me").get(), token).build());
        return gson.fromJson(body, BookHiveUser.class);
    }

    // ── Books ────────────────────────────────────────────────────────────

    public static BookPage getBooks(String query, String genre, int page, int size) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl() + "/books").newBuilder()
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("size", String.valueOf(size));
        if (query != null && !query.isEmpty()) urlBuilder.addQueryParameter("query", query);
        if (genre != null && !genre.isEmpty()) urlBuilder.addQueryParameter("genre", genre);
        String body = execute(new Request.Builder().url(urlBuilder.build()).get().build());
        return gson.fromJson(body, BookPage.class);
    }

    public static Book getBook(String bookId) {
        String body = execute(new Request.Builder().url(baseUrl() + "/books/" + bookId).get().build());
        return gson.fromJson(body, Book.class);
    }

    // ── Cart ─────────────────────────────────────────────────────────────

    public static List<CartItem> getCart(String token) {
        String body = execute(authed(new Request.Builder().url(baseUrl() + "/cart").get(), token).build());
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        return gson.fromJson(body, type);
    }

    public static void addToCart(String token, CartItemRequest request) {
        execute(authed(jsonPostBuilder(baseUrl() + "/cart/items", request), token).build());
    }

    public static void clearCart(String token) {
        execute(authed(new Request.Builder().url(baseUrl() + "/cart").delete(), token).build());
    }

    // ── Orders ───────────────────────────────────────────────────────────

    public static Order checkout(String token) {
        String body = execute(authed(new Request.Builder().url(baseUrl() + "/orders").post(emptyBody()), token).build());
        return gson.fromJson(body, Order.class);
    }

    public static List<Order> getOrders(String token) {
        String body = execute(authed(new Request.Builder().url(baseUrl() + "/orders").get(), token).build());
        Type type = new TypeToken<List<Order>>() {}.getType();
        return gson.fromJson(body, type);
    }

    public static Order getOrder(String token, String orderId) {
        String body = execute(authed(new Request.Builder().url(baseUrl() + "/orders/" + orderId).get(), token).build());
        return gson.fromJson(body, Order.class);
    }

    public static void returnOrder(String token, String orderId) {
        execute(authed(new Request.Builder().url(baseUrl() + "/orders/" + orderId + "/return").post(emptyBody()), token).build());
    }

    // ── Marketplace ──────────────────────────────────────────────────────

    public static List<MarketplaceListing> getListings() {
        String body = execute(new Request.Builder().url(baseUrl() + "/marketplace").get().build());
        Type type = new TypeToken<List<MarketplaceListing>>() {}.getType();
        return gson.fromJson(body, type);
    }

    public static void createListing(String token, ListingRequest request) {
        execute(authed(jsonPostBuilder(baseUrl() + "/marketplace/listings", request), token).build());
    }

    public static void buyListing(String token, String listingId) {
        execute(authed(new Request.Builder().url(baseUrl() + "/marketplace/listings/" + listingId + "/buy").post(emptyBody()), token).build());
    }

    public static void cancelListing(String token, String listingId) {
        execute(authed(new Request.Builder().url(baseUrl() + "/marketplace/listings/" + listingId).delete(), token).build());
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private static Request.Builder authed(Request.Builder builder, String token) {
        return builder.header("Authorization", "Bearer " + token);
    }

    private static Request jsonPost(String url, Object body) {
        return new Request.Builder()
                .url(url)
                .post(RequestBody.create(gson.toJson(body), JSON))
                .build();
    }

    private static Request.Builder jsonPostBuilder(String url, Object body) {
        return new Request.Builder()
                .url(url)
                .post(RequestBody.create(gson.toJson(body), JSON));
    }

    private static RequestBody emptyBody() {
        return RequestBody.create("", JSON);
    }

    private static String execute(Request request) {
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            if (!response.isSuccessful()) {
                throw new RuntimeException(
                        "HTTP " + response.code() + " : " + responseBody
                );
            }
            return responseBody;
        } catch (IOException e) {
            throw new RuntimeException("Request failed: " + e.getMessage(), e);
        }
    }
}
