package httpApi.parser.imp;

import com.google.gson.GsonBuilder;
import java.util.Map;

public class ParserImp {

    public static String ConvertToJson(Map<String, Object> map)
    {
        return new GsonBuilder().enableComplexMapKeySerialization().create().toJson(map);
    }

    public static Map<String, Object> convertJsonToMap(String json)
    {
        return new GsonBuilder().enableComplexMapKeySerialization().create().fromJson(json,Map.class);
    }

}
