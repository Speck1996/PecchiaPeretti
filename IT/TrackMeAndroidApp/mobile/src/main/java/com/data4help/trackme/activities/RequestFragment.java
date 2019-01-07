package com.data4help.trackme.activities;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.data4help.trackme.R;
import com.google.android.gms.common.internal.Constants;

import java.util.ArrayList;
import java.util.List;

import adapter.RequestAdapter;
import gesture.SwipeListener;
import model.ThirdPartyRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;

import static android.content.Context.MODE_PRIVATE;

public class RequestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;

    private RetrofitClient rClient;

    private RecyclerView recyclerView;

    private String token;

    private String individualUsername;

    List<ThirdPartyRequest> requests;

    private View view;


    private RecyclerView.Adapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_request, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        rClient = RetrofitClient.getInstance();


        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        //retrieving the token and the username from the shared preferences
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
       individualUsername = preferences.getString("username", "");
        token = "Bearer " + preferences.getString("token", "");

        requests = new ArrayList<>();
        recyclerView.setAdapter(new RequestAdapter(requests,view.getContext(),rClient));

      //  callApi(token,individualUsername);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                // Fetching data from server
                callApi(token,individualUsername);
            }
        });




        return view;

    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {

        // Fetching data from server
        callApi(token,individualUsername);
    }

    private void callApi(String token, String individualUsername){

        mSwipeRefreshLayout.setRefreshing(true);

        //calling the server
        Call call = rClient.getApi().getPendingRequests(individualUsername, token);
        call.enqueue(new Callback<List<ThirdPartyRequest>>() {
            @Override
            public void onResponse(Call<List<ThirdPartyRequest>> call, Response<List<ThirdPartyRequest>> response) {

                //successful response obtained
                if (response.isSuccessful()) {


                    requests= new ArrayList<>(response.body());


                    if(requests!= null) {
                        adapter = new RequestAdapter(requests,view.getContext(),rClient);
                        recyclerView.setAdapter(new RequestAdapter(requests,view.getContext(),rClient));

                        for(ThirdPartyRequest request:requests){
                            Log.i("Received request: ", request.toString());

                        }
                        //notification of success
                        Toast.makeText(view.getContext(), "Data received",
                                Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);

                    }else{
                        Toast.makeText(view.getContext(), "You don't have any request",
                                Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                } else {

                    mSwipeRefreshLayout.setRefreshing(false);

                    //server made some mess while handling data
                    Log.i("Response message: ", response.message() + " " + response.code());


                    Toast.makeText(getContext(), "Something went wrong",
                            Toast.LENGTH_SHORT).show();

                }


            }

            //something went wrong when calling the server
            @Override
            public void onFailure(Call call, Throwable t) {

                mSwipeRefreshLayout.setRefreshing(false);

                Toast.makeText(view.getContext(), "Server not reachable",
                        Toast.LENGTH_SHORT).show();
            }
        });



    }

}
