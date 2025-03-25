package models.slack;

import lombok.Setter;

import java.util.List;

@Setter
public class SimpleMessageModel {

    String channel;
    List<Block> blocks;

    public SimpleMessageModel(String channelId, String type, String message){
        setChannel(channelId);
        setBlock(type, message);
    }

    public SimpleMessageModel(){}

    public void setBlock(String type, String message) {this.blocks = List.of(new Block(type, message));}

    public static class Block{
        @Setter
        String type;
        Text text;

        public Block(String type, String message){
            setType("section");
            setText(type, message);
        }

        public Block(){}

        public void setText(String type, String message) {this.text = new Text(type, message);}

        @Setter
        public static class Text {
            String type;
            String text;

            public Text(String type, String text){
                setText(text);
                setType(type);
            }

            public Text(){}

        }
    }
}
