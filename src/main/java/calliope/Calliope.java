package calliope;

import common.ApiUtilities;
import models.calliope.ReportResponse;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import utils.ServiceGenerator;

import java.io.File;

public class Calliope extends ApiUtilities {
    CalliopeServices calliopeServices = new ServiceGenerator(new Headers.Builder()
            .add("x-api-key", properties.getProperty("x-api-key"))
            .build())
            .generate(CalliopeServices.class);

    public Response<ReportResponse> uploadResults(File reportFile, String profileId){
        RequestBody fileBody = RequestBody.create(reportFile, MediaType.parse("application/json"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", reportFile.getName(), fileBody);
        Call<ReportResponse> uploadCall = calliopeServices.uploadResults(profileId, part);
        return getResponse(uploadCall, false, true, "uploadResults");
    }
}
