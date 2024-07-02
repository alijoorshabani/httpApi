package httpApi.request.retrofitCalls;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.Map;


public interface RequestService {

    @GET
    public Call<JsonObject> getRequest(@Url String url, Map options, @HeaderMap Map<String,String> headers);

    @POST
    public Call<JsonObject> PostRequest(@Url String url, Map options, @HeaderMap Map<String,String> headers, @Body Map<String,String>  bodyData);

}
