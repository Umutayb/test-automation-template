package bookstore.models;

import lombok.Getter;

@Getter
public class TokenResponseModel {
    String token;
    String expires;
    String status;
    String result;
}
