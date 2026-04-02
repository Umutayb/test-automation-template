package bookhive.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartItem {
    private String id;
    private String userId;
    private String bookId;
    private int quantity;
}
