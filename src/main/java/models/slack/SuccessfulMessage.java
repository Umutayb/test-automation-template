package models.slack;

import lombok.Data;
import java.util.List;

@Data
public class SuccessfulMessage {
    Boolean ok;
    String channel;
    String ts;
    Message message;

    @Data
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

        @Data
        public static class Profile {
            String id;
            String app_id;
            String name;
            Icons icons;
            Boolean deleted;
            Double updated;
            String team_id;

            @Data
            public static class Icons {
                String image_36;
                String image_48;
                String image_72;
            }
        }

        @Data
        public static class Block {
            String type;
            String block_id;
            Text text;

            @Data
            public static class Text {
                String type;
                String text;
                Boolean verbatim;
            }
        }
    }
}
