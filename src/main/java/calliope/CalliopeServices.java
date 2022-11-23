package calliope;

import models.calliope.ReportResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CalliopeServices {
    @SuppressWarnings("unused") // BASE_URL is picked up by the service generator
    String BASE_URL = CalliopeApi.BASE_URL;

    @Multipart
    @POST(CalliopeApi.PROFILE_SUFFIX + CalliopeApi.IMPORT_SUFFIX)
    Call<ReportResponse> uploadResults(@Path("profileId") String profileId, @Part MultipartBody.Part report);


    @Multipart
    @POST(CalliopeApi.PROFILE_SUFFIX + CalliopeApi.IMPORT_SUFFIX)
    Call<ReportResponse> uploadResults(
            @Path("profileId") String profileId,
            @Part MultipartBody.Part  report,
            @Part MultipartBody.Part attachment
    );
}
