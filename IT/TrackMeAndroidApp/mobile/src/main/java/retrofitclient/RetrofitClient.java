package retrofitclient;

import android.content.SharedPreferences;
import android.webkit.URLUtil;

import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Singleton used to call server api.
 */
public class RetrofitClient {

    /**
     * Base string of the server
     * NOTE: use value 10.0.2.2 instead of localhost for local test purpose
     */
    public final static String BASE_URL = "http://10.0.2.2:8080/aaaa/rest/";

    /**
     * Attribute containing the client singleton object
     */
    private static RetrofitClient singleClient;

    /**
     * Class containing the properties to communicate with the server
     */
    private static Retrofit retrofit;

    /**
     * Class instantiating the builder with all the properties needed to
     * communicate with the server
     */
    private RetrofitClient(String url) {

        String serverString;
        if(URLUtil.isHttpUrl(url)) {
             serverString = url;
        }else{
            serverString = BASE_URL;
        }

            retrofit = new Retrofit.Builder()
                    .baseUrl(serverString)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();



    }

    /**
     * Getting an instance of the retrofit client
     * @return the retrofit client
     */
    public static synchronized RetrofitClient getInstance(String url){

        if(singleClient== null ){
            singleClient = new RetrofitClient(url);
        }

        return singleClient;
    }

    public static synchronized void clearClient(){
        singleClient = null;
    }



    /**
     * Creating a retrofit client for the specific apis
     * @return the retrofit client needed for the http request
     */
    public TrackmeApi getApi(){

        return retrofit.create(TrackmeApi.class);
    }
}
