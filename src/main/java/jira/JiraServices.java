package jira;

import models.jira.*;
import retrofit2.Call;
import retrofit2.http.*;

import static jira.JiraApi.*;

public interface JiraServices {
    String BASE_URL = JiraApi.JIRA_BASE_URL;

    @POST(BASE_URL + REST + API + JIRA_2 + ISSUE)
    Call<NewIssueResponse> createIssue (@Body NewIssueRequestModel body);

    @POST(BASE_URL + REST + API + JIRA_2 + SEARCH)
    Call<JiraSearchResponse> searchIssues(@Body JqlQuery jqlQuery);

    @GET(BASE_URL + REST + AGILE + ONE_ZERO + BOARD + BOARD_ID + SPRINT)
    Call<GetAllSprintsResponse> getSprints(@Path("boardId") int boardId, @Query("state") String state);
}
