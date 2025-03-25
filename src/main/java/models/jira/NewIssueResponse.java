package models.jira;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewIssueResponse {
    String id;
    String key;
    String self;
}
