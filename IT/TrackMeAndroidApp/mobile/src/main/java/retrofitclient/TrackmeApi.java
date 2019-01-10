package retrofitclient;

import java.util.List;

import model.IndividualData;
import model.ThirdPartyRequest;
import model.User;
import model.Individual;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    @POST("authenticate")
    @Headers("Content-Type: application/json")
    Call<String> login(@Body User credentials);

    /**
     * Sign up api
     * @param individual to be passed to the server
     * @return response from the server
     */
    @POST("signup/individual")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> signup(@Body Individual individual);

    /**
     * Send data API
     * @param data data to be sent
     * @param token token needed to authorize the client
     * @return response from the server
     */
    @POST("senddata")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> sendData(@Body IndividualData data, @Header("Authorization") String token);


    @POST("requests/getrequests")
    @Headers("Content-Type: text/plain")
    Call<List<ThirdPartyRequest>> getPendingRequests(@Body String username, @Header("Authorization") String token);

    @POST("requests/giveresponse")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> receiveRequestResponse(@Body ThirdPartyRequest request, @Header("Authorization") String token);

}
