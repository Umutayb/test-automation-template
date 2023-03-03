package common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import models.cucumber.CucumberReport;
import models.slack.Receivers;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.FileUtilities;
import utils.PropertyUtility;
import utils.WebUtilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public  class CucumberUtilities extends WebUtilities {

    ObjectMapper mapper = new ObjectMapper();

    Properties properties;

    public CucumberUtilities(){
        properties = PropertyUtility.properties;
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public List<Receivers.Receiver> getReceivers() {
        try(FileReader file = new FileReader(properties.getProperty("receivers-directory"))) {
            return mapper.readValue(file, Receivers.class).receivers();
        }
        catch (IOException e) {throw new RuntimeException(e);}
    }

    public List<CucumberReport> getCucumberReport(String directory){
        try {
            List<CucumberReport> reports = new ArrayList<>();
            FileReader reportFile = new FileReader(directory);
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(reportFile);
            for (Object jsonObject: array) {
                String json = jsonObject.toString();
                reports.add(mapper.readValue(json , CucumberReport.class));
            }
            return  reports;
        }
        catch (IOException | ParseException e) {throw new RuntimeException(e);}
    }
}
