import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/java/features"},
        plugin = {"json:target/reports/Cucumber.json"},
        glue = {"steps"},
        tags = "@BookStore",
        publish = true
)
public class TestRunner {

    @BeforeClass
    public static void initialSequence(){
        //Anything you have to set up before the test suite runs will be implemented here
        File dir = new File("screenshots");
        File[] screenshots = dir.listFiles();
        String mediaType;
        if(screenshots.length>0) for (File screenshot : screenshots) {
            try {mediaType = Files.probeContentType(screenshot.toPath());}
            catch (IOException e) {throw new RuntimeException(e);}
            if (mediaType != null && mediaType.equals("image/jpeg")) screenshot.delete();
        }
    }

    @AfterClass
    public static void finalSequence(){
        //Anything you have to set up after the test suite runs will be implemented here, such as uploading test results

        //String tags = System.getProperty("cucumber.filter.tags");
        //if (tags != null)
        //    new ShutdownSequence().publishReports(System.getProperty("cucumber.filter.tags"));
        //else new Printer(TestRunner.class).new Warning("Tags are null!");
    }
}