import httpApi.config.Config;
import httpApi.request.imp.GeneralRequestImp;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpApiMain {

    public static void main(String[] args) throws IOException
    {

        System.out.println(" ====== httpApi Is Started!=====");
        AnnotationConfigApplicationContext context = new
                AnnotationConfigApplicationContext(Config.class);

        Map map=new HashMap();
        map.put("email","eve.holt@reqres.in");
        map.put("password", "cityslicka");

        GeneralRequestImp generalRequestImp=new GeneralRequestImp();
//        System.out.println(generalRequestImp.getJsonResponse("https://reqres.in/api/login",
//                RequestType.POST,map,null));

    }
}
