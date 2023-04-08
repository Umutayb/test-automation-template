package bookstore.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class IsbnModel {
    String isbn;

    public IsbnModel(String isbn) {
        this.isbn = isbn;
    }
    public IsbnModel() {
    }
}
