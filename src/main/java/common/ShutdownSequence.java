package common;

import context.ContextStore;
import models.cucumber.CucumberReport;
import models.slack.Receivers;
import models.slack.SimpleMessageModel;
import models.slack.SuccessfulMessage;
import models.slack.ThreadMessageModel;
import slack.Slack;
import utils.Printer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static utils.StringUtilities.Color.PURPLE;
import static utils.StringUtilities.highlighted;

public class ShutdownSequence extends CucumberUtilities {

    static Printer log = new Printer(ShutdownSequence.class);
    static Slack slack = new Slack();

    public static void publishReports(String testName) {
        log.info("Performing final sequence for the test specification: " + highlighted(PURPLE, testName));
        try {
            if (Boolean.parseBoolean(ContextStore.get("upload-results", "false"))) {
                File screenshotsDirectory = new File("screenshots");
                File[] screenshots = screenshotsDirectory.listFiles();

                String directory = ContextStore.get("report-directory", "target/reports/Cucumber.json");
                String channelId = ContextStore.get("slack-channel-id");
                List<CucumberReport> reports = getCucumberReport(directory);
                log.info("Uploading test results to Calliope.pro");

                String ts = postThreadMessage(channelId, testName);

                boolean testFailure = checkForTestFailures(reports);

                if (testFailure) {
                    log.warning("There are test failures!");
                    if (screenshots != null && screenshots.length > 0)
                        postScreenshots(channelId, ts, screenshots);
                    tagReceivers(channelId, getReceivers());
                }

                log.warning("Reports are posted on Slack.");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("Final sequence completed.");
    }

    public static String postThreadMessage(String channelId, String testName) {
        SuccessfulMessage message = slack.postSimpleMessage(
                new SimpleMessageModel(
                        channelId,
                        "mrkdwn",
                        "Results of *" + testName + "* tests have been uploaded!."
                ),
                channelId
        );
        return message.getTs();
    }

    public static void tagReceivers(String channelId, List<Receivers.Receiver> receivers) {
        StringBuilder tagString = new StringBuilder();

        for (Receivers.Receiver receiver : receivers){
            if (!tagString.isEmpty()) tagString.append(", ");
            tagString.append("<@").append(receiver.userId()).append("|").append(receiver.name()).append(">");
            if (receivers.indexOf(receiver) > (receivers.size() - 1)){

            }
        }
        tagString.append(".");
        slack.postSimpleMessage(
                new SimpleMessageModel(
                        channelId,
                        "mrkdwn",
                        tagString.toString()
                ),
                channelId
        );
    }

    public static boolean checkForTestFailures(List<CucumberReport> reports) {
        boolean testFailure = false;
        for (CucumberReport report : reports) {
            if (!report.testSuccessful()) {
                testFailure = true;
                break;
            }
        }
        return testFailure;
    }

    public static void postScreenshots(String channelId, String ts, File[] screenshots) {
        slack.postThreadMessage(
                new ThreadMessageModel(channelId, "mrkdwn", "Screenshots:", ts),
                channelId
        );

        String fileName = "";
        for (File screenshot : screenshots) {
            String mediaType;
            try {
                mediaType = Files.probeContentType(screenshot.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (mediaType != null && mediaType.equals("image/png") && !fileName.equals(screenshot.getName())) {
                slack.postMultipartThreadMessage(screenshot, screenshot.getName(), channelId, ts);
            }
            fileName = screenshot.getName();
        }
    }
}