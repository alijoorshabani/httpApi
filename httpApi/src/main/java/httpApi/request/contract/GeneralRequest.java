package httpApi.request.contract;

import com.google.gson.JsonArray;
import httpApi.request.RequestType.RequestType;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Map;

public interface GeneralRequest {
    JsonArray getJsonResponse(String url, RequestType requestType, Map data, Map options, Map headers) throws IOException;
    Map<String,Object> getCacheableJsonResponse(String url, RequestType requestType, Map data, Map options,Map headers) throws IOException;
    Map<String,Object> getFromCachedJsonResponse(String url, RequestType requestType, Map data,Map options,Map headers) throws IOException;
}
