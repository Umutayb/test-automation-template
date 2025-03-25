package common;

import api_assured.ResponsePair;
import context.ContextStore;
import jira.Jira;
import lombok.Getter;
import models.jira.*;
import retrofit2.Response;
import utils.Printer;

public class JiraUtilities {
    static Printer log = new Printer(ShutdownSequence.class);
    static Jira jira = new Jira();

    @Getter
    public enum Project {
        BOARD1(Integer.parseInt(ContextStore.get("board-id")));

        private final int boardId;

        Project(int boardId) {
            this.boardId = boardId;
        }
    }

    @Getter
    public enum IssueType {
        BUG("Bug"),
        STORY("Story"),
        TASK("Task"),
        TEST("Test"),
        PRECONDITION("Precondition"),
        TEST_PLAN("\"Test Plan\""),
        TEST_EXECUTION("\"Test Execution\""),
        TEST_SET("\"Test Set\"");

        private final String value;

        IssueType(String operator) {
            this.value = operator;
        }
    }

    public static NewIssueResponse createNewJiraIssue(IssueType issueType, Project project) {
        String type = issueType.getValue().replaceAll("\"", "");
        String summary = type + " for Project " + project.toString();
        String description = "This is a " + type + " created via Jira API.";

        NewIssueRequestModel.JiraFields body = new NewIssueRequestModel.JiraFields(
                new NewIssueRequestModel.Project(project.toString()),
                summary,
                description,
                new NewIssueRequestModel.IssueType(type),
                getActiveSprintId(project.boardId)
        );

        try {
            ResponsePair<Response<NewIssueResponse>, Object> response = jira.createNewIssue(new NewIssueRequestModel(body));
            assert response.response().body() != null;
            log.success("Successfully created a new issue with key: " + response.response().body().getKey());
            return response.response().body();

        } catch (Exception e) {
            log.error("Error when creating a new issue for Project: " + project + " with IssueType: " + type, e);
            return null;
        }
    }

    public static int getActiveSprintId(int boardId) {
        int activeSprintId = 0;
        try {
            GetAllSprintsResponse response = jira.getSprints(boardId, "active");
            log.success("All sprints are acquired for boardId " + boardId);
            activeSprintId = response.getValues().get(0).getId();
        } catch (Exception e) {
            log.error("Error when getting active sprint for Project: " + boardId, e);
        }
        return activeSprintId;
    }

    public static String searchJiraIssueInOpenSprints(IssueType issueType, Project project) {
        String jql = String.format("issueType = %s AND project = %s AND Sprint in openSprints() AND status NOT IN (Done, Cancelled)",
                issueType.getValue(), project.toString()
        );
        log.info("Executing JQL Query: " + jql);

        try {
            JqlQuery jqlQuery = new JqlQuery(jql, 1); // Limiting results to 1
            JiraSearchResponse response = jira.searchIssue(jqlQuery);

            if (response != null && !response.getIssues().isEmpty()) {
                String issueKey = response.getIssues().get(0).getKey();
                log.success("Found issue with key: " + issueKey);
                return issueKey;
            } else {
                log.warning("No issues found matching the query.");
                return null;
            }
        } catch (Exception e) {
            log.error("Error when searching for issue in open sprints for Project: " + project + " with IssueType: " + issueType.getValue(), e);
            return null;
        }
    }

    public static String getCurrentOrCreateNew(IssueType issueType, Project project) {
        try {
            String existingIssueKey = searchJiraIssueInOpenSprints(issueType, project);
            if (existingIssueKey != null) return existingIssueKey;
            else {
                NewIssueResponse newIssueResponse = createNewJiraIssue(issueType, project);
                return (newIssueResponse != null) ? newIssueResponse.getKey() : null;
            }
        } catch (Exception e) {
            log.error("Error in getting or creating issue for Project: " + project + " with IssueType: " + issueType.getValue(), e);
            return null;
        }
    }
}
