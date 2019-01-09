package com.data4help.trackme.activities;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.data4help.trackme.R;
import java.util.ArrayList;
import java.util.List;
import adapter.RequestAdapter;
import model.ThirdPartyRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;
import static android.content.Context.MODE_PRIVATE;

/**
 * Fragment that handles third parties requests visualization and acceptance/refusal
 */
public class RequestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * Layout used for the swipe down action that will
     * call the api for obtaining requests from the server
     */
    SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Client used to communicate with the server
     */
    private RetrofitClient rClient;

    /**
     * View that contains the various requests items
     */
    private RecyclerView recyclerView;

    /**
     * Token of the individual that is necessary to communicate with the server
     */
    private String token;

    /**
     * Username of the individual needed to build the {@link RequestAdapter}
     */
    private String individualUsername;

    /**
     * List containing all the requests obtained from the server
     */
    List<ThirdPartyRequest> requests;

    /**
     * View containing all what's necessary for the fragment visualization
     */
    private View view;


    private RecyclerView.Adapter adapter;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        //restoring the state
        super.onCreate(savedInstanceState);
    }

    /**
     * {@inheritDoc}
     * In this method the view is set up in all its components
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //binding the view
        view = inflater.inflate(R.layout.fragment_request, container, false);

        //setting up the recycler view
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //binding the client
        rClient = RetrofitClient.getInstance();

        // SwipeRefreshLayout
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        //retrieving the token and the username from the shared preferences
        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", MODE_PRIVATE);
        individualUsername = preferences.getString("username", "");
        token = "Bearer " + preferences.getString("token", "");

        //setting up the adapter with an empty list in order to not produce
        //null pointer if there are no requests
        requests = new ArrayList<>();
        recyclerView.setAdapter(new RequestAdapter(requests,view.getContext(),rClient));

      //  callApi(token,individualUsername);

        //post runnable used to bind refresh animation
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                //setting the refresh loading view
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



                    if(response.body()!=null) {

                        //requests binding
                        requests= new ArrayList<>(response.body());

                        //setting up the adapter
                        adapter = new RequestAdapter(requests,view.getContext(),rClient);
                        recyclerView.setAdapter(new RequestAdapter(requests,view.getContext(),rClient));

                        //log for testing purpose
                        for(ThirdPartyRequest request:requests){
                            Log.i("Received request: ", request.toString());
                        }

                        //notification of failure
                        Toast.makeText(view.getContext(), "Data received",
                                Toast.LENGTH_SHORT).show();

                        //stopping the refresh animation
                        mSwipeRefreshLayout.setRefreshing(false);

                    }else{

                        Toast.makeText(view.getContext(), "You don't have any request",
                                Toast.LENGTH_SHORT).show();

                        //stopping refresh animation
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                } else {


                    //stopping refresh animation
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

                //stopping refresh animation
                mSwipeRefreshLayout.setRefreshing(false);

                Toast.makeText(view.getContext(), "Server not reachable",
                        Toast.LENGTH_SHORT).show();
            }
        });



    }

}
