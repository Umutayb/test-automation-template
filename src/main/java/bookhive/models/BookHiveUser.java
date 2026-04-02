package bookhive.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookHiveUser {
    private String token;
    private String userId;
    private String username;
    private String email;
    private double balance;
}
