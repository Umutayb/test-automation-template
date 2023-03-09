package models.bookStore;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Account {
    String userName;
    String userID;
    String password;
    String code;
    String message;
    String token;
    String expires;
    String status;
    String result;
    @SerializedName("username")
    String responseUsername;
    List<Book> books;
}
