package httpApi.request.imp;

import com.google.gson.JsonArray;
import httpApi.cache.contract.TransferWithRedis;
import httpApi.exp.InvalidDataException;
import httpApi.exp.NotReadFromRedisException;
import httpApi.exp.NotWriteToRedisException;
import httpApi.parser.imp.ParserImp;
import httpApi.request.RequestType.RequestType;
import httpApi.request.contract.GeneralRequest;
import httpApi.request.retrofitCalls.RequestService;
import httpApi.retrofitConnection.ServiceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import timeApi.apis.TimeHandler;
import timeApi.apis.enums.SourceTimeType;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


@Component
public class GeneralRequestImp implements GeneralRequest
{

    @Autowired
    private ServiceGenerator serviceGenerator;

    @Autowired
    private TransferWithRedis transferWithRedis;

    @Override
    public JsonArray getJsonResponse(String url, RequestType requestType, Map data, Map options, Map headers) throws IOException
    {
        RequestService requestService = serviceGenerator.createService(url, RequestService.class);

        Map<String, String> option = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        Map<String, String> header = new HashMap<>();
        body.put("data", ParserImp.ConvertToJson(data));
        option.put("options", ParserImp.ConvertToJson(options));
        header.put("headers", ParserImp.ConvertToJson(headers));

        if (requestType != null)
        {
            if(requestType.equals(RequestType.GET)||requestType.equals(RequestType.OPTIONS)||requestType.equals(RequestType.HEAD))
                return requestService.getRequest(url, option, header).execute().body().getAsJsonArray();
            else if(requestType.equals(RequestType.POST)||requestType.equals(RequestType.PUT)||requestType.equals(RequestType.PATCH))
                return requestService.PostRequest(url, options, header, body).execute().body().getAsJsonArray();
            else
                throw new RuntimeException("type not found");
        }
        else
            throw new RuntimeException("type is null");
    }


    @Override
    public Map<String, Object> getCacheableJsonResponse(String url, RequestType requestType, Map data, Map options, Map headers) throws IOException
    {
        Map result= new HashMap();
        result.put("url",url);
        result.put("request_type",requestType);
        if(requestType.equals(RequestType.POST)) result.put("request_body",data);
        result.put("data",getJsonResponse(url,requestType,data,options,headers));
        if (transferWithRedis.writeInRedis(result))
            return result;
        else
            throw new NotWriteToRedisException("can not write to redis");
    }


    @Override
    public Map<String, Object> getFromCachedJsonResponse(String url, RequestType requestType, Map data, Map options, Map headers) throws IOException
    {
        if (transferWithRedis.checkExistByKey(url, requestType))
        {
            Map<String,Object> map=transferWithRedis.readFromRedis(url, requestType);
            Timestamp current_timestamp=new TimeHandler.TimeHandlerBuilder(SourceTimeType.SERVERTIME, null).build().getTimeStamp();
            if (map.containsKey("last_update_time"))
            {
//                if (new TimeManipulateImp().differentBetweenTwoPlainTimeStamp(Time4jUtil.convertTimeStampToPlainTimestamp(current_timestamp),
//                        Time4jUtil.convertTimeStampToPlainTimestamp((Timestamp) map.get("last_update_time")),null)< 15*60)
                if ((current_timestamp.getTime() - ((Timestamp) map.get("last_update_time")).getTime()) < 15*60)
                    return map;
                return getCacheableJsonResponse(url,requestType,data,options,headers);
            }
            throw new InvalidDataException("invalid Data");
        }
        throw new NotReadFromRedisException("can not read from redis");
    }
}
