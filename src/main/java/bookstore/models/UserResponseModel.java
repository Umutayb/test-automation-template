package bookstore.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter @Setter
public class UserResponseModel {
    String userID;
    String username;
    List<BookModel> books;

}
