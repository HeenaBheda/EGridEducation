package com.cloudentro.taegrid;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Creat_New_Class_Fragment extends Fragment {

    View view;
    Button buttonAddClass;
    EditText editTextSubject,editTextStandard,editTextStudents;
    TextView textViewDate,textViewTime;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    String id;
    String subject;
    String standard;
    String dateTime;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    RelativeLayout relativeLayout;


    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.create_new_class_fragment,container,false);
        initView();


        buttonAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextSubject.getText().toString().equals("") || editTextStandard.getText().toString().equals("") || editTextStudents.getText().toString().equals("")){
                    if (editTextSubject.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Please add subject",Toast.LENGTH_LONG).show();
                    }
                    if (editTextStandard.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Please add standard",Toast.LENGTH_LONG).show();
                    }
                    if (editTextStudents.getText().toString().equals("")){
                        Toast.makeText(getContext(),"Please add students",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Create_Class", Context.MODE_PRIVATE);
                    if (sharedPreferences.contains("schoolId")){
                        id=sharedPreferences.getString("schoolId","");
                        //Toast.makeText(getContext()," Hello "+id,Toast.LENGTH_LONG).show();
                    }

                    subject=editTextSubject.getText().toString().trim();
                    standard=editTextStandard.getText().toString().trim();
                    dateTime=textViewDate.getText().toString().trim()+" "+textViewTime.getText().toString().trim();

                    stringRequest=new StringRequest(Request.Method.POST, URLs.CreatNewClassUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response!=null){

                                        try {
                                            JSONObject jsonObject=new JSONObject(response);
                                            //Log.e("Response",String.valueOf(jsonObject));
                                            if (!jsonObject.getBoolean("error")){
                                                Toast.makeText(getContext(),"Message  "+ jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                                //getting the user from the response
                                                //JSONObject userJson = jsonObject.getJSONObject("message");
                                            }else {
                                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                            }
                                        }catch (Exception e){
                                            //Toast.makeText(getContext()," Exception : "+e,Toast.LENGTH_LONG).show();
                                            Log.e("Exception is : ",String.valueOf(e));
                                        }

                                    }else {
                                        Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext()," "+error,Toast.LENGTH_LONG).show();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Log.e("Id",id);
                            Log.e("subject",subject);
                            Log.e("standard",standard);
                            Log.e("active",String.valueOf(1));
                            Log.e("time",dateTime);
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("school_id",id);
                            params.put("subject",subject);
                            params.put("standard",standard);
                            params.put("active",String.valueOf(1));
                            params.put("time",dateTime);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                    editTextSubject.getText().clear();
                    editTextStandard.getText().clear();
                    editTextStudents.getText().clear();
                    textViewDate.setText(getResources().getString(R.string.setDat));
                    textViewTime.setText(getResources().getString(R.string.setTime));
                    Toast.makeText(getContext(),"Class created successfully !",Toast.LENGTH_LONG).show();
                }
            }
        });


        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        textViewTime.setText(hourOfDay + ": "+minute);
                    }
                },hour,minute,true);
                timePickerDialog.show();

            }
        });

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

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

    private void initView() {
        buttonAddClass=view.findViewById(R.id.buttonAddclass);
        editTextSubject=view.findViewById(R.id.txtSubject);
        editTextStandard=view.findViewById(R.id.txtStandard);
        editTextStudents=view.findViewById(R.id.txtStudents);
        textViewDate=view.findViewById(R.id.textView4);
        textViewTime=view.findViewById(R.id.textView5);
        requestQueue= Volley.newRequestQueue(getContext());
        bottomNavigationView=(BottomNavigationView) view.findViewById(R.id.bottomNavigationCreateClass);
        frameLayout=(FrameLayout)view.findViewById(R.id.frameCreateclass);
        relativeLayout=(RelativeLayout)view.findViewById(R.id.rlCreateclass);
    }

    private void loadFragment(Fragment fragment){
        frameLayout.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.replace(R.id.frameCreateclass,fragment);
        fragmentTransaction.commit();
    }


}
