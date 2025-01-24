package jira.xray;

import api_assured.ApiUtilities;
import api_assured.ServiceGenerator;
import context.ContextStore;
import models.jira.*;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;

public class Xray extends ApiUtilities {

    int timeout = Integer.parseInt(ContextStore.get("jira-timeout"));

    XrayServices xrayServices = new ServiceGenerator(new Headers.Builder()
            .add("Content-Type", "application/json")
            .add("Authorization", "Bearer " + getXrayToken())
            .build()
    )
            .setConnectionTimeout(timeout)
            .setReadTimeout(timeout)
            .setWriteTimeout(timeout)
            .generate(XrayServices.class);

    private String getXrayToken() {
        log.info("Generating token for Xray cloud");
        XrayServices authService = new ServiceGenerator(new Headers.Builder()
                .build()
        )
                .setConnectionTimeout(timeout)
                .setReadTimeout(timeout)
                .setWriteTimeout(timeout)
                .generate(XrayServices.class);

        String clientId = ContextStore.get("xray-client-id");
        String clientSecret = ContextStore.get("xray-client-secret");
        Call<String> call = authService.authenticate(new XrayAuthentication(clientId, clientSecret));

        try {
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                String token = response.body();
                log.success("Xray authentication token successfully generated!");
                return token;
            } else {
                log.info("Failed to authenticate with Xray: " + response.errorBody().string());
            }
        } catch (Exception e) {
            log.error("An error occurred during Xray authentication", e);
        }

        return null;
    }

    public NewIssueResponse postImportExecution(ImportExecutionRequest importExecutionRequest) {
        log.info("Importing test execution results to xray...");
        Call<NewIssueResponse> messageCall = xrayServices.importExecution(importExecutionRequest);
        return perform(messageCall, false, true);
    }
}
