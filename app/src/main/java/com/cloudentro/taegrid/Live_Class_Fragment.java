package com.cloudentro.taegrid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Live_Class_Fragment extends Fragment {

    List<ListClass_Pojo> list;
    RecyclerView recyclerView;
    Adapter_List_Class adapterListClass;
    View view;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    String id;
    BottomNavigationView bottomNavigationView;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.live_class_fragment,container,false);
        initView();
        getData();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.liveclass:
                        loadFragment(new Live_Class_Fragment());
                        return true;
                    case R.id.createclass:
                        loadFragment(new Creat_New_Class_Fragment());
                        return true;
                }
                return false;
            }
        });

        return view;
    }

    private void getData() {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Fectching all live classes....");
        progressDialog.show();

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Create_Class", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("schoolId")){
            id=sharedPreferences.getString("schoolId","");
            //Toast.makeText(getContext()," Hello "+id,Toast.LENGTH_LONG).show();
        }else {
            id=String.valueOf(1);
        }

        stringRequest=new StringRequest(Request.Method.POST, URLs.listClassUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Rsponse",response);
                if (response!=null){

                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObjectNode =new JSONObject(response);
                        JSONArray jsonArray=jsonObjectNode.getJSONArray("message");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ListClass_Pojo listClass_pojo=new ListClass_Pojo();
                            listClass_pojo.setClassName(jsonObject.getString("name"));
                            listClass_pojo.setActive(Integer.parseInt(jsonObject.getString("active")));
                            listClass_pojo.setStandard(jsonObject.getString("standard"));
                            listClass_pojo.setSubject(jsonObject.getString("subject"));
                            listClass_pojo.setTime(jsonObject.getString("time"));
                            listClass_pojo.setMeetingUrl(jsonObject.getString("url"));

                            list.add(listClass_pojo);
                            // Log.e("List", String.valueOf(list.size()));
                        }
                        //Log.e("Array : ",String.valueOf(jsonArray));
                        //Log.e("Response",String.valueOf(response));
                    }catch (Exception e){
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Exception : "+e,Toast.LENGTH_LONG).show();
                        Log.e("Exception : ",String.valueOf(e));
                    }

                    adapterListClass.notifyDataSetChanged();

                }else {
                    Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Error :"+error,Toast.LENGTH_LONG).show();
                Log.e("Error ",String.valueOf(error));
                Toast.makeText(getContext(),"Please retry again !",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("school_id",id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void initView() {
        recyclerView=view.findViewById(R.id.rcViewClass);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        adapterListClass=new Adapter_List_Class(getContext(),list);
        recyclerView.setAdapter(adapterListClass);
        requestQueue= Volley.newRequestQueue(getContext());
        bottomNavigationView=(BottomNavigationView) view.findViewById(R.id.bottomNavigationLiveClass);
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }


}
