package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.data4help.trackme.R;

import java.util.List;

import model.ThirdPartyRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofitclient.RetrofitClient;

import static android.content.Context.MODE_PRIVATE;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    List<ThirdPartyRequest> requests;

    RetrofitClient rClient;

    private Context context;

    public RequestAdapter(List<ThirdPartyRequest> requests, Context context, RetrofitClient rClient){

        this.requests = requests;

        this.context = context;

        this.rClient = rClient;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ThirdPartyRequest request = requests.get(position);


        holder.tpUsernameText.setText(request.getSender());

        if(request.isAccepted()){
            holder.acceptButton.setVisibility(View.GONE);
        }else {
            holder.acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ThirdPartyRequest sentReq = new ThirdPartyRequest();

                    sentReq.setSender(request.getSender());
                    sentReq.setReceiver(request.getReceiver());
                    sentReq.setAccepted(true);

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

                                holder.acceptButton.setVisibility(View.GONE);

                                //notification of success
                                Toast.makeText(context, "Request accepted",
                                        Toast.LENGTH_SHORT).show();

                            } else {

                                //server made some mess while handling data
                                Log.i("Response message: ", response.message() + " " + response.code());

                                //notification of success
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

        holder.refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


                            removeAt(position);
                            //notification of success
                            Toast.makeText(context, "Request refused",
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            //server made some mess while handling data
                            Log.i("Response message: ", response.message() + " " + response.code());

                            //notification of success
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

    public void removeAt(int position) {
        requests.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, requests.size());
    }

    @Override
    public int getItemCount(){
        return requests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView tpUsernameText;

        MaterialButton acceptButton;

        MaterialButton refuseButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tpUsernameText =  itemView.findViewById(R.id.tpusername);

            acceptButton= itemView.findViewById(R.id.accept_button);

            refuseButton = itemView.findViewById(R.id.refuse_button);

        }

    }
}
