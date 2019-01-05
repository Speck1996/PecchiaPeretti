package retrofitclient;

import model.User;
import model.Individual;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Class containing needed server APIs
 */
public interface TrackmeApi {

    /**
     * Login api
     * @param credentials credentials associated with the individual
     * @return the token string by the server
     */
    @POST("login/authenticate")
    @Headers("Content-Type: application/json")
    Call<String> login(@Body User credentials);

    /**
     * Sign up api
     * @param individual to be passed to the server
     * @return the response of the server
     */
    @POST("register/home/signupin")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> signup(@Body Individual individual);

}
