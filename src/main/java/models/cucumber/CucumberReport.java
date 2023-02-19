package models.cucumber;

import lombok.Getter;
import java.util.List;

@Getter
public class CucumberReport {

    Integer line;
    List<Element> elements;
    String name;
    String description;
    String id;
    String keyword;
    String uri;
    List<Tag> tags;

    public Boolean testSuccessful(){
        for (Element.StepComponent step: getElements().get(0).getSteps()) {
            if (!step.getResult().getStatus().equals("passed")) return false;
        }
        return true;
    }

    @Getter
    public static class Element {
        String start_timestamp;
        List<Component> before;
        String line;
        String name;
        String description;
        String id;
        List<Component> after;
        String type;
        String keyword;
        List<StepComponent> steps;
        List<Tag> tags;

        @Getter
        public static class Component {
            Result result;
            Match match;

            @Getter
            public static class Result {
                Long duration;
                String status;
                String error_message;
            }

            @Getter
            public static class Match {
                String location;
            }
        }

        @Getter
        public static class StepComponent {
            Result result;
            String line;
            String name;
            Match match;
            List<Row> rows;
            String keyword;

            @Getter
            public static class Row {
                List<String> cells;
            }

            @Getter
            public static class Result {

                String error_message;
                Long duration;
                String status;

            }

            @Getter
            public static class Match {
                String location;
                List<Argument> arguments;

                @Getter
                public static class Argument {
                    String val;
                    Integer offset;
                }
            }
        }
    }

    @Getter
    public static class Tag {
        String name;
        String type;
        Location location;

        @Getter
        public static class Location {
            Integer line;
            Integer column;
        }
    }
}
