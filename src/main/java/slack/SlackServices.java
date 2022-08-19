package slack;

import models.slack.SimpleMessageModel;
import models.slack.SuccessfulMessage;
import models.slack.ThreadMessageModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface SlackServices {

    String BASE_URL = SlackApi.BASE_URL;

    @POST(BASE_URL + SlackApi.CHAT_SUFFIX + SlackApi.POST_MESSAGE)
    Call<SuccessfulMessage> postMessage(@Body SimpleMessageModel body);

    @POST(BASE_URL + SlackApi.CHAT_SUFFIX + SlackApi.POST_MESSAGE)
    Call<Object> postThreadMessage(@Body ThreadMessageModel body);

    @FormUrlEncoded
    @POST(BASE_URL + SlackApi.FILE_SUFFIX + SlackApi.UPLOAD_FILE)
    Call<Object> postMultipartMessage(
            @Part MultipartBody.Part file,
            @Part("initial_comment") RequestBody comment,
            @Part("channels") RequestBody channelId
    );

    @Multipart
    @POST(BASE_URL + SlackApi.FILE_SUFFIX + SlackApi.UPLOAD_FILE)
    Call<Object> postMultipartThreadMessage(
            @Part MultipartBody.Part file,
            @Part("initial_comment") RequestBody comment,
            @Part("channels") RequestBody channelId,
            @Part("thread_ts") RequestBody thread_ts
    );
}
