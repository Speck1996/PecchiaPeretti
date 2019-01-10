package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.data4help.trackme.R;

import java.util.ArrayList;
import java.util.List;
import model.ThirdPartyRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;
import static android.content.Context.MODE_PRIVATE;

/**
 * Enhaching the recycler view adapter in order to
 * handle third parties request actions and their visualization
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    /**
     * Request fetched from the server that have to be displayed
     */
    List<ThirdPartyRequest> requests;

    /**
     * Client used to communicate with the server
     */
    RetrofitClient rClient;

    /**
     * Context used to bind view actions
     */
    private Context context;

    /**
     * Constructor for the request adapter
     * @param requests requests that will be bound to the adapter
     * @param context   context that will be bound to the adapter
     * @param rClient   client that will be bound to the adapter
     */
    public RequestAdapter(List<ThirdPartyRequest> requests, Context context, RetrofitClient rClient){

        //binding attributes
        this.requests = requests;
        this.context = context;
        this.rClient = rClient;
    }

    public void updateAdapter(List<ThirdPartyRequest> requests){

        this.requests.clear();

        this.requests.addAll(requests);

        notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_layout, parent, false);
        return new ViewHolder(view);
    }


    /**
     * {@inheritDoc}
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        //getting the request that will be associated to the view
        final ThirdPartyRequest request = requests.get(position);

        //setting the third party username for the request view item
        holder.tpUsernameText.setText(request.getSender());

        //for already accepted requests only the refuse button is needed
        if(request.isAccepted()){

            holder.acceptButton.setVisibility(View.GONE);

            holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accepted_req));

            holder.refuseButton.setText("Remove");


        }else {

            //setting up the click listener for the accept button
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //object representing the state of the request
                    //binding view properties to it and setting the accepted
                    //flag as true
                    ThirdPartyRequest sentReq = new ThirdPartyRequest();
                    sentReq.setSender(request.getSender());
                    sentReq.setReceiver(request.getReceiver());
                    sentReq.setAccepted(true);

                    //retrieving the token and the username from the shared preferences
                    SharedPreferences preferences = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
                    String token = "Bearer " + preferences.getString("token", "");

                    //binding the api call
                    Call call = rClient.getApi().receiveRequestResponse(sentReq, token);

                    //calling the server
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call call, Response response) {

                            //successful response obtained
                            if (response.isSuccessful()) {

                                //request successfully accepted
                                //accept button not needed anymore
                                holder.acceptButton.setVisibility(View.GONE);

                                holder.refuseButton.setText("Remove");


                                holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.accepted_req));

                                //notification of success
                                Toast.makeText(context, "Request accepted",
                                        Toast.LENGTH_SHORT).show();

                            } else {

                                //server made some mess while handling data
                                Log.i("Response message: ", response.message() + " " + response.code());

                                //notification of error
                                Toast.makeText(context, "Some mess happened",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        //something went wrong when calling the server
                        @Override
                        public void onFailure(Call call, Throwable t) {

                            Toast.makeText(context, "Server not reachable",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        //setting the click listener for the refuse button
        holder.refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //object representing the state of the request
                //binding view properties to it and setting the accepted
                //flag to false
                ThirdPartyRequest sentReq = new ThirdPartyRequest();
                sentReq.setSender(request.getSender());
                sentReq.setReceiver(request.getReceiver());
                sentReq.setAccepted(false);

                //retrieving the token and the username from the shared preferences
                SharedPreferences preferences = context.getSharedPreferences("myPrefs", MODE_PRIVATE);
                String token = "Bearer " + preferences.getString("token", "");

                Call call = rClient.getApi().receiveRequestResponse(sentReq, token);

                //calling the server
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        //successful response obtained
                        if (response.isSuccessful()) {

                            //request successfully refused, removing it from the view
                            removeAt(position);
                            //notification of success
                            Toast.makeText(context, "Request refused",
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            //server made some mess while handling data
                            Log.i("Response message: ", response.message() + " " + response.code());

                            //notification of error
                            Toast.makeText(context, "Some mess happened",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }

                    //something went wrong when calling the server
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(context, "Server not reachable",
                                Toast.LENGTH_SHORT).show();
                    }
                });


            }

        });

    }

    /**
     * Method used to remove the request view element
     * @param position position of the view element to be removed
     */
    private void removeAt(int position) {

        //removing the element and notify to the adapter that the view changed
        requests.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, requests.size());
    }

    /**
     * {@inheritDoc}
     * @return how many elements are displayed in the recycler view
     */
    @Override
    public int getItemCount(){
        return requests.size();
    }

    /**
     * Class needed to bind the view elements with their corresponding attributes
     */
    class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * Text representing the third party's username associated to the request
         */
        TextView tpUsernameText;

        /**
         * Button associated to the accept action
         */
        MaterialButton acceptButton;

        /**
         * Button associated to the refuse action
         */
        MaterialButton refuseButton;

        /**
         * Background of the request view, needed to change its color for
         * accepted request
         */
        CardView card;

        ViewHolder(@NonNull View itemView) {

            //constructing the view
            super(itemView);

            //binding the view
            tpUsernameText =  itemView.findViewById(R.id.tpusername);
            acceptButton= itemView.findViewById(R.id.accept_button);
            refuseButton = itemView.findViewById(R.id.refuse_button);


            card = itemView.findViewById(R.id.req_card);

        }

    }



}
