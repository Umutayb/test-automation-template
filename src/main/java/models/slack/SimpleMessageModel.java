package models.slack;

import java.util.List;

public class SimpleMessageModel {

    String channel;
    List<Block> blocks;

    public SimpleMessageModel(String channelId, String type, String message){
        setChannel(channelId);
        setBlock(type, message);
    }

    public SimpleMessageModel(){}

    public void setChannel(String channel) {this.channel = channel;}

    public void setBlock(String type, String message) {this.blocks = List.of(new Block(type, message));}

    public void setBlocks(List<Block> blocks){this.blocks = blocks;}

    public static class Block{
        String type;
        Text text;

        public Block(String type, String message){
            setType("section");
            setText(type, message);
        }

        public Block(){}

        public void setType(String type) {this.type = type;}

        public void setText(String type, String message) {this.text = new Text(type, message);}

        public static class Text {
            String type;
            String text;

            public Text(String type, String text){
                setText(text);
                setType(type);
            }

            public Text(){}

            public void setType(String type) {this.type = type;}

            public void setText(String text) {this.text = text;}
        }
    }
}
