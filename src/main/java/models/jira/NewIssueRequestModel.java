package models.jira;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewIssueRequestModel {
    JiraFields fields;

    @Data
    @AllArgsConstructor
    public static class JiraFields {
        Project project;
        String summary;
        String description;
        IssueType issuetype;
        // sprint custom field for BAC
        int customfield_10020;
    }

    @Data
    @AllArgsConstructor
    public static class Project {
        String key;

    }

    @Data
    @AllArgsConstructor
    public static class IssueType {
        String name;

    }
}
