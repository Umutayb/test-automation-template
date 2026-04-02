package bookhive.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {
    private String bookId;
    private int quantity;
    private double priceAtPurchase;
}
