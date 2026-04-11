package bookhive.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookHiveUser {
    private String token;
    private String userId;
    private String username;
    private String email;
    private double balance;
}
