package bookstore.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.StringUtilities;

@Data
public class CredentialModel {
    String userName;
    String password;

    public CredentialModel() {}

    public CredentialModel(String baseUserName) {
        this.userName = StringUtilities.generateRandomString(baseUserName,4,false,true);
    }
}
