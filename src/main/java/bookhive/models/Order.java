package bookhive.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private String id;
    private String userId;
    private List<OrderItem> items;
    private double totalPrice;
    private String status;
    private String purchasedAt;
    private boolean returnEligible;
}
