package bookhive.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Book {
    private String id;
    private String title;
    private String author;
    private String genre;
    private String description;
    private double price;
    private String coverImage;
    private int stock;
    private String isbn;
}
