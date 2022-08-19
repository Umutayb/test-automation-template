package models.cucumber;

import java.util.List;

public class CucumberReport {

    Integer line;
    List<Element> elements;
    String name;
    String description;
    String id;
    String keyword;
    String uri;
    List<Tag> tags;

    public Integer getLine() {
        return line;
    }

    public List<Element> getElements() {
        return elements;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getUri() {
        return uri;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Boolean testSuccessful(){
        for (Element.StepComponent step: getElements().get(0).getSteps()) {
            if (!step.getResult().getStatus().equals("passed")) return false;
        }
        return true;
    }

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

        public String getStart_timestamp() {
            return start_timestamp;
        }

        public List<Component> getBefore() {
            return before;
        }

        public String getLine() {
            return line;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getId() {
            return id;
        }

        public List<Component> getAfter() {
            return after;
        }

        public String getType() {
            return type;
        }

        public String getKeyword() {
            return keyword;
        }

        public List<StepComponent> getSteps() {
            return steps;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public static class Component {
            Result result;
            Match match;

            public static class Result {
                Long duration;
                String status;
            }

            public static class Match {
                String location;
            }
        }

        public static class StepComponent {
            Result result;
            String line;
            String name;
            Match match;
            String keyword;

            public Result getResult() {
                return result;
            }

            public String getLine() {
                return line;
            }

            public String getName() {
                return name;
            }

            public Match getMatch() {
                return match;
            }

            public String getKeyword() {
                return keyword;
            }

            public static class Result {

                String error_message;
                Long duration;
                String status;

                public Long getDuration() {
                    return duration;
                }

                public String getStatus() {
                    return status;
                }
            }

            public static class Match {
                String location;
                List<Argument> arguments;

                public static class Argument {
                    String val;
                    Integer offset;
                }
            }
        }
    }

    public static class Tag {
        String name;
        String type;
        Location location;

        public static class Location {
            Integer line;
            Integer column;
        }
    }
}
