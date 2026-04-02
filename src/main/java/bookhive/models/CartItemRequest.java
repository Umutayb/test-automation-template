package bookhive.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemRequest {
    private String bookId;
    private int quantity;

    public CartItemRequest(String bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }
}
