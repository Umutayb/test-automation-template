package bookhive.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListingRequest {
    private String bookId;
    private String condition;
    private double price;

    public ListingRequest(String bookId, String condition, double price) {
        this.bookId = bookId;
        this.condition = condition;
        this.price = price;
    }
}
