package slack;

import models.slack.SimpleMessageModel;
import models.slack.SuccessfulMessage;
import models.slack.ThreadMessageModel;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import utils.Caller;
import utils.ServiceGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static utils.FileUtilities.properties;

public class Slack extends Caller {

    SlackServices slackServices = new ServiceGenerator(new Headers.Builder()
            .add("Authorization", properties.getProperty("slack-token"))
            .build())
            .generate(SlackServices.class);

    public SuccessfulMessage postSimpleMessage(String message, String channelId){
        Call<SuccessfulMessage> messageCall = slackServices.postMessage(
                new SimpleMessageModel(channelId, "mrkdwn", message)
        );
        return perform(messageCall, false, false, "postSimpleMessage");
    }

    public Object postThreadMessage(String message, String channelId, String threadTs){
        Call<Object> messageCall = slackServices.postThreadMessage(
                new ThreadMessageModel(channelId, "mrkdwn", message, threadTs)
        );
        return perform(messageCall, false, false, "postThreadMessage");
    }

    public Object postMultipartThreadMessage(File file, String comments, String channelId, String threadTs){
        String mediaType;
        try {mediaType = Files.probeContentType(file.toPath());}
        catch (IOException e) {throw new RuntimeException(e);}
        RequestBody fileBody = RequestBody.create(file, MediaType.parse(mediaType));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        Call<Object> postFile = slackServices.postMultipartThreadMessage(
                part,
                RequestBody.create(comments, MediaType.parse("text/plain")),
                RequestBody.create(channelId, MediaType.parse("text/plain")),
                RequestBody.create(threadTs, MediaType.parse("text/plain"))
        );
        return perform(postFile, true, false, "postMultipartThreadMessage");
    }

    public Object postMultipartMessage(File file, String comments, String channelId){
        String mediaType;
        try {mediaType = Files.probeContentType(file.toPath());}
        catch (IOException e) {throw new RuntimeException(e);}
        RequestBody fileBody = RequestBody.create(file, MediaType.parse(mediaType));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        Call<Object> postFile = slackServices.postMultipartMessage(
                part,
                RequestBody.create(comments, MediaType.parse("text/plain")),
                RequestBody.create(channelId, MediaType.parse("text/plain"))
        );
        return perform(postFile, true, false, "postMultipartThreadMessage");
    }
}
