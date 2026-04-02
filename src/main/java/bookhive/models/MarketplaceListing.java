package bookhive.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketplaceListing {
    private String id;
    private String sellerId;
    private String bookId;
    private String condition;
    private double price;
    private String listedAt;
    private String status;
}
