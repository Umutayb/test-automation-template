package models.slack;

public class ThreadMessageModel extends SimpleMessageModel {

    String thread_ts;

    public ThreadMessageModel(String channelId, String type, String message, String thread_ts) {
        super(channelId, type, message);
        this.thread_ts = thread_ts;
    }

    public ThreadMessageModel(String thread_ts) {
        this.thread_ts = thread_ts;
    }

    public ThreadMessageModel(){}

    public String thread_ts() {return thread_ts;}

    public void setThread_ts(String thread_ts) {this.thread_ts = thread_ts;}
}
