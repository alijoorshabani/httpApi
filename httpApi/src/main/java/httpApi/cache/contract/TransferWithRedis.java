package httpApi.cache.contract;

import httpApi.request.RequestType.RequestType;

import java.util.Map;

public interface TransferWithRedis
{
    boolean checkExistByKey(String url, RequestType requestType);

    Map<String, Object> readFromRedis(String url,RequestType requestType);

    boolean writeInRedis(Map<String,Object> map);

    boolean removeCachedObject(String url,RequestType requestType);

    Map<String, Object> readFromRedisWithSpecConnection(String url,RequestType requestType);
    boolean writeInRedisWithSpecConnection(Map<String,Object> map);

}
