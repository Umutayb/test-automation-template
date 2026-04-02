package bookhive.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BookPage {
    private List<Book> content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private boolean first;
    private boolean last;
    private boolean empty;
}
