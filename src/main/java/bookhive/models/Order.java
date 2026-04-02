package bookhive.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Order {
    private String id;
    private String userId;
    private List<OrderItem> items;
    private double totalPrice;
    private String status;
    private String purchasedAt;
    private boolean returnEligible;
}
