package bookhive.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItem {
    private String id;
    private String userId;
    private String bookId;
    private int quantity;
}
