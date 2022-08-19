package common;

import slack.Slack;
import utils.Printer;

public class ShutdownSequence extends CucumberUtilities {
    static Slack slack = new Slack();
    static Printer log = new Printer(ShutdownSequence.class);

    public void publishReports(String testName){ // This method is called upon after the tests are done running
        log.new Info("Performing final sequence for the test specification: " + highlighted(Color.PURPLE,testName));
        log.new Info("Final sequence completed.");
    }
}