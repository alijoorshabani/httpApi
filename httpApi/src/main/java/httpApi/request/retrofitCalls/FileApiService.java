package httpApi.request.retrofitCalls;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface FileApiService
{

    @GET("{path}")
    Call<ResponseBody> downloadFile(@Path("path") String path);


    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadFile(@HeaderMap Map<String,String> headers, @Part MultipartBody.Part file);
}