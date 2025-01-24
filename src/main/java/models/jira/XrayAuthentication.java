package models.jira;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class XrayAuthentication {
    String client_id;
    String client_secret;
}
