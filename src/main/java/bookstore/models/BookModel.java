package bookstore.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookModel {
    String isbn;
    String title;
    String subTitle;
    String author;
    String publish_date;
    String publisher;
    int pages;
    String description;
    String website;
}