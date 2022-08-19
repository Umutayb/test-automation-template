package models.slack;

import java.util.List;

public class SuccessfulMessage {
    Boolean ok;
    String channel;
    String ts;
    Message message;

    public Boolean ok() {return ok;}

    public String channel() {return channel;}

    public String ts() {return ts;}

    public Message message() {return message;}

    public static class Message {
        String bot_,d;
        String type;
        String text;
        String user;
        String ts;
        String app_id;
        String team;
        Profile bot_profile;
        List<Block> blocks;

        public static class Profile {
            String id;
            String app_id;
            String name;
            Icons icons;
            Boolean deleted;
            Double updated;
            String team_id;

            public static class Icons {
                String image_36;
                String image_48;
                String image_72;
            }
        }

        public static class Block {
            String type;
            String block_id;
            Text text;

            public static class Text {
                String type;
                String text;
                Boolean verbatim;
            }
        }
    }
}
