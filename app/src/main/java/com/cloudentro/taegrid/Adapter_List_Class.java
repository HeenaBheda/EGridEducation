package com.cloudentro.taegrid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_List_Class extends RecyclerView.Adapter<Adapter_List_Class.MyViewHolder> {

    Context context;
    List<ListClass_Pojo> classList;
    String meetingUrl;
    ListClass_Pojo listClass_pojo;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String classId;
    JsonObjectRequest jsonObjectRequest;

    public Adapter_List_Class(Context context, List<ListClass_Pojo> classList) {
        this.context = context;
        this.classList = classList;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_live_class_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {

        listClass_pojo=classList.get(position);
        Log.e("Pojo", String.valueOf(listClass_pojo));
        holder.textViewsubject.setText(listClass_pojo.getSubject());
        //holder.textViewClassname.append(listClass_pojo.getStandard());
        holder.textViewClassname.setText("Class "+listClass_pojo.getStandard());
        holder.textViewClassId.setText(listClass_pojo.getClassName());
        holder.textViewTime.setText("At "+listClass_pojo.getTime());
        meetingUrl=listClass_pojo.getMeetingUrl();

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(context,"Hiii",Toast.LENGTH_LONG).show();
                stringRequest=new StringRequest(Request.Method.POST, URLs.disableClassUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Class Response",response);
                                if (response!=null){
                                    classList.remove(listClass_pojo);
                                    //classList.remove(position);
                                   // notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                    notifyItemRangeChanged(position,classList.size());
                                }else {
                                    Toast.makeText(context,"Cannot delete!",Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error "+error,Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Log.e("CID",listClass_pojo.getClassName());
                        Map<String,String> params=new HashMap<String, String>();
                        params.put("cid",listClass_pojo.getClassName());
                        return params;
                    }
                };
                requestQueue= Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewsubject,textViewClassname,textViewClassId,textViewTime;
        public Button buttonDelete;
        public LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            textViewsubject=itemView.findViewById(R.id.tvSubject);
            textViewClassname=itemView.findViewById(R.id.className);
            textViewClassId=itemView.findViewById(R.id.classId);
            textViewTime=itemView.findViewById(R.id.tvTime);
            buttonDelete=itemView.findViewById(R.id.btnDeleteClass);
            linearLayout=itemView.findViewById(R.id.llaypoutLiveclass);


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    URL serverURL;
                    try {
                       // serverURL = new URL(meetingUrl+"/"+listClass_pojo.getClassName());
                        serverURL = new URL("https://meet/jit.si"+"/"+listClass_pojo.getClassName());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        throw new RuntimeException("Invalid server URL!");
                    }
                    JitsiMeetConferenceOptions defaultOptions
                            = new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverURL)
                            .setWelcomePageEnabled(false)
                            .build();
                    JitsiMeet.setDefaultConferenceOptions(defaultOptions);
                    if (listClass_pojo.getClassName()!=null){
                        // Build options object for joining the conference. The SDK will merge the default
                        // one we set earlier and this one when joining.
                        JitsiMeetConferenceOptions options
                                = new JitsiMeetConferenceOptions.Builder()
                                .setServerURL(serverURL)
                                //.setRoom(String.valueOf(serverURL)+listClass_pojo.getClassName())
                                .setRoom(listClass_pojo.getClassName())
                                .setSubject(listClass_pojo.getSubject())
                                .setAudioMuted(false)
                                .setVideoMuted(false)
                                .setAudioOnly(false)
                                .setWelcomePageEnabled(false)
                                .build();
                        // Launch the new activity with the given options. The launch() method takes care
                        // of creating the required Intent and passing the options.
                        JitsiMeetActivity.launch(context, options);

                    }

                }
            });
        }
    }

}
