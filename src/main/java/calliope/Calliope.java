package calliope;

import api_assured.ApiUtilities;
import api_assured.ServiceGenerator;
import models.calliope.ReportResponse;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Calliope extends ApiUtilities {
    CalliopeServices calliopeServices = new ServiceGenerator(new Headers.Builder()
            .add("x-api-key", properties.getProperty("x-api-key"))
            .build())
            .generate(CalliopeServices.class);

    public Response<ReportResponse> uploadResults(File reportFile, String profileId){
        RequestBody fileBody = RequestBody.create(reportFile, MediaType.parse("application/json"));
        MultipartBody.Part part = MultipartBody.Part.createFormData("file[]", reportFile.getName(), fileBody);
        Call<ReportResponse> uploadCall = calliopeServices.uploadResults(profileId, part);
        return getResponse(uploadCall, false, true);
    }

    public Response<ReportResponse> uploadResults(File reportFile, File attachmentFile, String profileId){
        String mediaType;
        String attachmentType;
        try {
            mediaType = Files.probeContentType(reportFile.toPath());
            attachmentType = Files.probeContentType(attachmentFile.toPath());
        }
        catch (IOException e) {throw new RuntimeException(e);}

        RequestBody reportBody = RequestBody.create(reportFile, MediaType.parse(mediaType));
        RequestBody attachmentBody = RequestBody.create(attachmentFile, MediaType.parse(attachmentType));

        MultipartBody.Part report = MultipartBody.Part.createFormData("file[]", reportFile.getName(), reportBody);
        MultipartBody.Part attachment = MultipartBody.Part.createFormData(
                "attachment[]",
                attachmentFile.getName(),
                attachmentBody
        );

        Call<ReportResponse> uploadCall = calliopeServices.uploadResults(profileId, report, attachment);
        return getResponse(uploadCall, false, true);
    }
}
