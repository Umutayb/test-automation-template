package jira;

import api_assured.ApiUtilities;
import api_assured.ResponsePair;
import api_assured.ServiceGenerator;
import context.ContextStore;
import models.jira.*;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Jira extends ApiUtilities {
    int timeout = Integer.parseInt(ContextStore.get("jira-timeout"));

    JiraServices jiraServices = new ServiceGenerator(new Headers.Builder()
            .add("Content-Type", "application/json")
            .add("Authorization", "Basic " + getEncodedToken())
            .build()
    )
            .setConnectionTimeout(timeout)
            .setReadTimeout(timeout)
            .setWriteTimeout(timeout)
            .generate(JiraServices.class);

    private String getEncodedToken() {
        String username = ContextStore.get("jira-username");
        String password = ContextStore.get("jira-password");
        String authString = username + ":" + password;

        return Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));
    }

    public <ErrorBody> ResponsePair<Response<NewIssueResponse>, ErrorBody> createNewIssue(NewIssueRequestModel body) {
        log.info("Creating a new Jira issue...");
        Call<NewIssueResponse> messageCall = jiraServices.createIssue(body);
        return getResponse(messageCall, false, true, Object.class);

    }

    public JiraSearchResponse searchIssue(JqlQuery body) {
        log.info("Searching issue...");
        Call<JiraSearchResponse> messageCall = jiraServices.searchIssues(body);
        return perform(messageCall, false, true);
    }

    public GetAllSprintsResponse getSprints(int boardId, String state) {
        log.info("Getting all sprints...");
        Call<GetAllSprintsResponse> messageCall = jiraServices.getSprints(boardId, state);
        return perform(messageCall, false, true);
    }
}
