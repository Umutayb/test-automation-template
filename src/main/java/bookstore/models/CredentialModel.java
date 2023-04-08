package bookstore.models;

import lombok.Getter;
import lombok.Setter;
import utils.StringUtilities;

@Getter @Setter
public class CredentialModel {
    String userName;
    String password;

    public CredentialModel() {}
    public CredentialModel(String baseUserName) {
        this.userName = new StringUtilities().generateRandomString(baseUserName,4,false,true);
    }
}
