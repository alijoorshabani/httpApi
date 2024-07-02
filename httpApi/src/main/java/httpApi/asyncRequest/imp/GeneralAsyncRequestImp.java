package httpApi.asyncRequest.imp;

import httpApi.asyncRequest.api.GeneralRequest;
import httpApi.parser.imp.ParserImp;
import httpApi.request.RequestType.RequestType;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Dsl;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Component
public class GeneralAsyncRequestImp implements GeneralRequest
{

    private static final AsyncHttpClient client= Dsl.asyncHttpClient();

    private void prepareRequestData(BoundRequestBuilder request, Map data, Map options)
    {
        request.setBody(ParserImp.ConvertToJson(data));
        request.setHeaders(options);
    }

    public void closeClient()
    {
        try
        {
            client.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    @Override
    public BoundRequestBuilder getRequest(String url, RequestType requestType, Map data, Map options)
    {
        BoundRequestBuilder request;

        if(requestType!=null)
        {
            switch (requestType)
            {
                case POST:
                    request = client.preparePost(url);
                    prepareRequestData(request, data, options);
                    return request;

                case PUT:
                    request = client.preparePut(url);
                    prepareRequestData(request, data, options);
                    return request;

                case PATCH:
                    request = client.preparePatch(url);
                    prepareRequestData(request, data, options);
                    return request;

                case GET:
                    request = client.prepareGet(url);
                    prepareRequestData(request,null,options);
                    return request;

                case DELETE:
                    request = client.prepareDelete(url);
                    prepareRequestData(request,null,options);
                    return request;

                default:
                    throw new RuntimeException("type not found");
            }
        }
        else
            throw new RuntimeException("type is null");
    }

}
