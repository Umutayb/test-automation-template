package bookhive;

import context.ContextStore;

public class BookHiveEndpoints {

    public static String BASE_URL = ContextStore.get("bookhive-api-url", "http://localhost:3000") + "/";

    public static final String RESET = "api/reset";
    public static final String SEED = "api/seed";
    public static final String HEALTH = "api/health";

    public static final String AUTH_SIGNUP = "api/auth/signup";
    public static final String AUTH_LOGIN = "api/auth/login";
    public static final String AUTH_LOGOUT = "api/auth/logout";
    public static final String AUTH_ME = "api/auth/me";

    public static final String BOOKS = "api/books";
    public static final String BOOK_BY_ID = "api/books/{id}";

    public static final String CART = "api/cart";
    public static final String CART_ITEMS = "api/cart/items";
    public static final String CART_ITEM_BY_ID = "api/cart/items/{id}";

    public static final String ORDERS = "api/orders";
    public static final String ORDER_BY_ID = "api/orders/{id}";
    public static final String ORDER_RETURN = "api/orders/{id}/return";

    public static final String MARKETPLACE = "api/marketplace";
    public static final String MARKETPLACE_LISTINGS = "api/marketplace/listings";
    public static final String MARKETPLACE_LISTING_BY_ID = "api/marketplace/listings/{id}";
    public static final String MARKETPLACE_LISTING_BUY = "api/marketplace/listings/{id}/buy";
}
