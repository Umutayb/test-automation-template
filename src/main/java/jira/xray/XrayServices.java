package jira.xray;

import models.jira.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import static jira.xray.XrayApi.*;

public interface XrayServices {
    String BASE_URL = XrayApi.XRAY_BASE_URL;

    @POST(BASE_URL + V2 + AUTHENTICATE)
    Call<String> authenticate(@Body XrayAuthentication body);

    @POST(BASE_URL + V2 + IMPORT_SUFFIX + EXECUTION_SUFFIX)
    Call<NewIssueResponse> importExecution(@Body ImportExecutionRequest body);
}
