package common;

import calliope.Calliope;
import calliope.CalliopeApi;
import models.calliope.ReportResponse;
import models.cucumber.CucumberReport;
import models.slack.Receivers;
import models.slack.SuccessfulMessage;
import retrofit2.Response;
import slack.Slack;
import utils.Printer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ShutdownSequence extends CucumberUtilities {
    static Calliope calliope = new Calliope();
    static Slack slack = new Slack();
    static Printer log = new Printer(ShutdownSequence.class);

    public void publishReports(String testName){ // This method is called upon after the tests are done running
        log.new Info("Performing final sequence for the test specification: " + highlighted(Color.PURPLE,testName));
        if (Boolean.parseBoolean(properties.getProperty("upload-results"))) {
            try {
                String directory = properties.getProperty("report-directory");
                String channelId = properties.getProperty("slack-channel-id");
                File reportFile = new File(directory);
                List<CucumberReport> reports = getCucumberReport(directory);
                String profileId = properties.getProperty("profile-id");
                String profileUrl = CalliopeApi.BASE_REPORT_URL + CalliopeApi.PROFILES_SUFFIX + profileId;
                log.new Info("Uploading test results to Calliope.pro");
                Response<ReportResponse> reportResponse = calliope.uploadResults(reportFile, profileId);
                assert reportResponse.body() != null;
                if (reportResponse.isSuccessful()){
                    log.new Success("Test results are successfully uploaded!");
                    log.new Info(reportResponse.body().message());
                    log.new Info("The results can be accessed from: " + reportResponse.body().report_url());
                }
                else {
                    log.new Warning("Test results are not uploaded");
                    log.new Info(reportResponse.body().message());
                }
                boolean testFailure = false;
                for (CucumberReport report: reports) {if (!report.testSuccessful()) testFailure = true;}
                assert reportResponse.body() != null;
                SuccessfulMessage message = slack.postSimpleMessage(
                        "Results of the *" + testName +"* tests have been uploaded to the <"+ profileUrl + "|Calliope Dashboard>.",
                        channelId
                );
                if (testFailure){
                    log.new Warning("There are test failures!");
                    String ts = message.ts();
                    StringBuilder threadMessage = new StringBuilder("There are failed tests ");
                    for (Receivers.Receiver receiver:getReceivers()) {
                        threadMessage.append(", <@").append(receiver.userId()).append(">");
                    }
                    threadMessage.append("!");
                    slack.postThreadMessage(threadMessage.toString(), channelId, ts);
                    slack.postThreadMessage("Screenshots:", channelId, ts);
                    File dir = new File("screenshots");
                    File[] screenshots = dir.listFiles();
                    assert screenshots != null;
                    for (File screenshot : screenshots) {
                        String mediaType;
                        try {mediaType = Files.probeContentType(screenshot.toPath());}
                        catch (IOException e) {throw new RuntimeException(e);}
                        if (mediaType.equals("image/jpeg"))
                            slack.postMultipartThreadMessage(screenshot, screenshot.getName(), channelId, ts);
                    }
                    log.new Warning("Reports are posted on slack.");
                }
            }
            catch (Exception exception) {log.new Warning(exception.getMessage());}
        }
        log.new Info("Final sequence completed.");
    }
}