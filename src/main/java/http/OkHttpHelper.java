package http;

import Utils.Constants;
import Utils.RedmineProperties;
import Utils.RedmineRequest;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sun.print.resources.serviceui_zh_TW;

import java.io.IOException;

public class OkHttpHelper {

    public static RedmineRequest instance;

    public static RedmineRequest getInstance(){
        return instance;
    }

    public static void init(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new RedmineInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(RedmineProperties.getProperties().getProperty(Constants.BASE_URL)).build();

        instance = retrofit.create(RedmineRequest.class);

    }


    public static class RedmineInterceptor implements Interceptor {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request newRequest;
//            String url = originalRequest.url().toString();
//            url = url.replace("%3A", ":");
            newRequest = originalRequest.newBuilder()
                    .addHeader(Constants.REDMINE_AUTH_HEADER, RedmineProperties.getProperties().getProperty(Constants.TOKEN)).build();

            return chain.proceed(newRequest);
        }
    }

}
