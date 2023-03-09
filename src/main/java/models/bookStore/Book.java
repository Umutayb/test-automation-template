package models.bookStore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Book {
    String isbn;
    String title;
    String subTitle;
    String author;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
//    Date publishDate;
    String publisher;
    Integer pages;
    String description;
    String website;
    String userId;
    List<CollectionOfIsbns> collectionOfIsbns;
}
