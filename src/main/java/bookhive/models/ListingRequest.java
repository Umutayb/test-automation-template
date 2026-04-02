package bookhive.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
