package bookhive.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItem {
    private String bookId;
    private int quantity;
    private double priceAtPurchase;
}
