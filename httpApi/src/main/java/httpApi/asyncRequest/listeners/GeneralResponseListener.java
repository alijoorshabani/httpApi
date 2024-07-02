package httpApi.asyncRequest.listeners;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.BoundRequestBuilder;

public class GeneralResponseListener
{

    public void doWork(BoundRequestBuilder requestBuilder, AsyncCompletionHandler asyncCompletionHandler)
    {
        requestBuilder.execute(asyncCompletionHandler);
    }

}
