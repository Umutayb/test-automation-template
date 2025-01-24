package slack;

import api_assured.ApiUtilities;
import api_assured.ServiceGenerator;
import context.ContextStore;
import models.slack.SimpleMessageModel;
import models.slack.SuccessfulMessage;
import models.slack.ThreadMessageModel;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Slack extends ApiUtilities {

    SlackServices slackServices = new ServiceGenerator(new Headers.Builder()
            .add("Authorization", ContextStore.get("slack-token").toString())
            .build()
    ).generate(SlackServices.class);

    public SuccessfulMessage postSimpleMessage(SimpleMessageModel message, String channelId){
        log.info("Posting simple message to channel: " + channelId);
        Call<SuccessfulMessage> messageCall = slackServices.postMessage(message);
        return perform(messageCall, false, false);
    }

    @SuppressWarnings("UnusedReturnValue")
    public Object postThreadMessage(ThreadMessageModel message, String channelId){
        log.info("Posting thread message to channel: " + channelId);
        Call<Object> messageCall = slackServices.postThreadMessage(message);
        return perform(messageCall, false, false);
    }

    @SuppressWarnings("UnusedReturnValue")
    public Object postMultipartThreadMessage(File file, String comments, String channelId, String threadTs){
        log.info("Posting multipart message to channel: " + channelId);
        String mediaType;
        try {mediaType = Files.probeContentType(file.toPath());}
        catch (IOException e) {throw new RuntimeException(e);}
        RequestBody fileBody = RequestBody.create(file, MediaType.parse(mediaType));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        Call<Object> postFile = slackServices.postMultipartThreadMessage(
                getMultipartFromFile(file, "file"),
                RequestBody.create(comments, MediaType.parse("text/plain")),
                RequestBody.create(channelId, MediaType.parse("text/plain")),
                RequestBody.create(threadTs, MediaType.parse("text/plain"))
        );
        return perform(postFile, false, false);
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public Object postMultipartMessage(File file, String comments, String channelId){
        log.info("Posting multipart thread message to channel: " + channelId);
        String mediaType;
        try {mediaType = Files.probeContentType(file.toPath());}
        catch (IOException e) {throw new RuntimeException(e);}
        RequestBody fileBody = RequestBody.create(file, MediaType.parse(mediaType));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        Call<Object> postFile = slackServices.postMultipartMessage(
                getMultipartFromFile(file, "file"),
                RequestBody.create(comments, MediaType.parse("text/plain")),
                RequestBody.create(channelId, MediaType.parse("text/plain"))
        );
        return perform(postFile, false, false);
    }
}
