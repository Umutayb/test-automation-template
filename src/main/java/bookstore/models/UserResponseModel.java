package bookstore.models;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
@Getter @Setter
public class UserResponseModel {

    String userId;
    String username;
    List<BookModel> books;

}
