package models.jira;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportExecutionRequest {
    Info info;
    List<Test> tests;

    @Data
    @AllArgsConstructor
    public static class Info {
        String summary;
        String description;
        String user;
        String testPlanKey;
        List<String> testEnvironments;
    }

    @Data
    public static class Test {
        String testKey;
        String start;
        String finish;
        String comment;
        String status;
        //List<Evidence> evidences;
        List<String> examples;

        @Data
        public static class Evidence {
            String data;
            String filename;
            String contentType;
        }
    }
}
