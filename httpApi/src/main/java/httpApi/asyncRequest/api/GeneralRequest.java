package httpApi.asyncRequest.api;

import httpApi.request.RequestType.RequestType;
import org.asynchttpclient.BoundRequestBuilder;

import java.io.IOException;
import java.util.Map;

public interface GeneralRequest {
    BoundRequestBuilder getRequest(String url, RequestType requestType, Map data, Map options);
}
