package common;

import calliope.Calliope;
import calliope.CalliopeApi;
import models.calliope.ReportResponse;
import models.cucumber.CucumberReport;
import models.slack.Receivers;
import models.slack.SuccessfulMessage;
import retrofit2.Response;
import slack.Slack;
import utils.FileUtilities;
import utils.Printer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ShutdownSequence extends CucumberUtilities {

    Printer log = new Printer(ShutdownSequence.class);
    FileUtilities.Zip zip = new FileUtilities.Zip();
    Calliope calliope = new Calliope();
    Slack slack = new Slack();

    public void publishReports(String testName){ // This method is called upon after the tests are done running
        log.new Info("Performing final sequence for the test specification: " + highlighted(Color.PURPLE,testName));
        try {
            if (Boolean.parseBoolean(properties.getProperty("upload-results", "false"))) {
                File screenshotsDirectory = new File("screenshots");
                File[] screenshots = screenshotsDirectory.listFiles();
                assert screenshots != null;
                AtomicReference<Boolean> upload = new AtomicReference<>(false);
                for (File file: screenshots) {
                    String mediaType = Files.probeContentType(file.toPath());
                    if(mediaType != null && mediaType.equals("image/jpeg")) upload.set(true);
                }
                String directory = properties.getProperty("report-directory");
                String channelId = properties.getProperty("slack-channel-id");
                File reportFile = new File(directory);
                List<CucumberReport> reports = getCucumberReport(directory);
                String profileId = properties.getProperty("profile-id");
                String profileUrl = CalliopeApi.BASE_REPORT_URL + CalliopeApi.PROFILES_SUFFIX + profileId;
                log.new Info("Uploading test results to Calliope.pro");
                Response<ReportResponse> reportResponse;
                if (upload.get()) reportResponse = calliope.uploadResults(
                        reportFile,
                        zip.compress("attachments", screenshots,"image/jpeg"),
                        profileId
                );
                else reportResponse = calliope.uploadResults(reportFile, profileId);
                assert reportResponse.body() != null;
                if (reportResponse.isSuccessful()){
                    log.new Success("Test results are successfully uploaded!");
                    log.new Info(reportResponse.body().message());
                    log.new Success("The results can be accessed from: " + reportResponse.body().report_url());
                }
                else {
                    log.new Warning("Test results are not uploaded");
                    log.new Warning(reportResponse.body().message());
                }
                boolean testFailure = false;
                for (CucumberReport report: reports) {if (!report.testSuccessful()) testFailure = true;}
                assert reportResponse.body() != null;
                SuccessfulMessage message = slack.postSimpleMessage(
                        "Results of the *" + testName +"* tests have been uploaded to the <"+ profileUrl + "|Calliope Dashboard>.",
                        channelId
                );
                if (upload.get()){
                    String ts = message.getTs();
                    if (testFailure) log.new Warning("There are test failures!");
                    StringBuilder threadMessage = new StringBuilder("There are failed tests ");
                    for (Receivers.Receiver receiver:getReceivers())
                        threadMessage.append(", <@").append(receiver.userId()).append(">");
                    threadMessage.append("!");
                    slack.postThreadMessage(threadMessage.toString(), channelId, ts);
                    slack.postThreadMessage("Screenshots:", channelId, ts);
                    String fileName = "";
                    for (File screenshot : screenshots) {
                        String mediaType;
                        try {mediaType = Files.probeContentType(screenshot.toPath());}
                        catch (IOException e) {throw new RuntimeException(e);}
                        if (mediaType != null && mediaType.equals("image/jpeg") && !fileName.equals(screenshot.getName()))
                            slack.postMultipartThreadMessage(screenshot, screenshot.getName(), channelId, ts);
                        fileName = screenshot.getName();
                    }
                    log.new Warning("Reports are posted on slack.");
                }
            }
        }
        catch (IOException e) {throw new RuntimeException(e);}
        log.new Info("Final sequence completed.");
    }
}