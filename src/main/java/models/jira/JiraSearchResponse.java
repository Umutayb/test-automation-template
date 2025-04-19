package models.jira;

import lombok.Data;

import java.util.List;

@Data
public class JiraSearchResponse {
    int startAt;
    int total;
    List<Issue> issues;

    @Data
    public static class Issue {
        String id;
        String self;
        String key;
        Fields fields;
    }

    @Data
    public static class Fields {
        String summary;
    }
}
