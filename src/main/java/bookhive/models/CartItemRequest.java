package bookhive.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItemRequest {
    private String bookId;
    private int quantity;

    public CartItemRequest(String bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }
}
