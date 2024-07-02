package httpApi.retrofitConnection;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Component
public class ServiceGenerator
{
    public <S> S createService(String url, Class<S> serviceClass)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(serviceClass);
    }
}
