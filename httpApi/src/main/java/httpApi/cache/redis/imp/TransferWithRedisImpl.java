package httpApi.cache.redis.imp;

import com.google.gson.Gson;
import httpApi.cache.contract.TransferWithRedis;
import httpApi.parser.imp.ParserImp;
import httpApi.request.RequestType.RequestType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import timeApi.apis.TimeHandler;
import timeApi.apis.enums.SourceTimeType;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Map;

@Component
public class TransferWithRedisImpl implements TransferWithRedis
{
    @Autowired
    private  JedisPool jedisPool;

    @Autowired
    private Connection connection;

    @Autowired
    private Gson gson;

    private String generateKey(String url, RequestType requestType)
    {
        return url + "_" + requestType.toString();
    }

    public boolean checkExistByKey(String url, RequestType requestType)
    {
        try (Jedis jedis = jedisPool.getResource())
        {
            String redisKey = generateKey(url, requestType);
            return jedis.exists(redisKey);
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public Map<String, Object> readFromRedis(String url, RequestType requestType)
    {
        try (Jedis jedis = jedisPool.getResource())
        {
            if (checkExistByKey(url,requestType))
            {
                String jsonData = jedis.get(generateKey(url,requestType));

                if (jsonData != null && !jsonData.isEmpty())
                {
                    Map<String, Object> result = ParserImp.convertJsonToMap(jsonData);

                    result.put("last_access_time", new TimeHandler.TimeHandlerBuilder(SourceTimeType.SERVERTIME, null).build().getTimeStamp());

                    return result;
                }
            }
            return Collections.emptyMap();
        }
        catch (Exception e)
        {
            return Collections.emptyMap();
        }
    }


    @Override
    public boolean writeInRedis(Map<String, Object> map)
    {
        try (Jedis jedis = jedisPool.getResource())
        {
            if (map.containsKey("url") && map.containsKey("request_type"))
            {
                String key = generateKey(map.get("url").toString(), RequestType.valueOf(map.get("request_type").toString()));
                Timestamp currentTimestamp = new TimeHandler.TimeHandlerBuilder(SourceTimeType.SERVERTIME, null).build().getTimeStamp();

                map.put("last_access_time", currentTimestamp);
                map.put("last_update_time", currentTimestamp);

                String jsonData = gson.toJson(map);
                String responseRedis = jedis.set(key, jsonData);
                return "OK".equals(responseRedis);
            }
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public boolean removeCachedObject(String url, RequestType requestType)
    {
        try (Jedis jedis = jedisPool.getResource())
        {
            if (checkExistByKey(url,requestType))
            {
                String key = generateKey(url, requestType);
                return jedis.del(key)>0;
            }
            return  false;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    public Map<String, Object> readFromRedisWithSpecConnection(String url, RequestType requestType)
    {
        try (Connection connection = new Connection("127.0.0.1", 6397))
        {
            Jedis jedis = new Jedis(connection);
            jedis.connect();

            if(checkExistByKey(url, requestType))
            {
                String jsonData = jedis.get(generateKey(url, requestType));

                if (jsonData != null && !jsonData.isEmpty())
                {
                    Map<String, Object> result = ParserImp.convertJsonToMap(jsonData);

                    result.put("last_access_time", new TimeHandler.TimeHandlerBuilder(SourceTimeType.SERVERTIME, null).build().getTimeStamp());

                    return result;
                }
            }

            jedis.close();
            return Collections.emptyMap();
        }
        catch (Exception e)
        {
            return Collections.emptyMap();
        }
    }

    @Override
    public boolean writeInRedisWithSpecConnection(Map<String, Object> map)
    {
        try (Connection connection = new Connection("127.0.0.1", 6397))
        {
            Jedis jedis = new Jedis(connection);
            jedis.connect();

            if (map.containsKey("key") && map.containsKey("request_type"))
            {
                String key = generateKey(map.get("url").toString(), RequestType.valueOf(map.get("request_type").toString()));
                Timestamp currentTimestamp = new TimeHandler.TimeHandlerBuilder(SourceTimeType.SERVERTIME, null).build().getTimeStamp();

                map.put("last_access_time", currentTimestamp);
                map.put("last_update_time", currentTimestamp);

                String jsonData = gson.toJson(map);
                String responseRedis = jedis.set(key, jsonData);
                return "OK".equals(responseRedis);
            }

            jedis.close();
            return false;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
