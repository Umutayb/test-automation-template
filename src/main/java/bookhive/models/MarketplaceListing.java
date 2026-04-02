package bookhive.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarketplaceListing {
    private String id;
    private String sellerId;
    private String bookId;
    private String condition;
    private double price;
    private String listedAt;
    private String status;
}
