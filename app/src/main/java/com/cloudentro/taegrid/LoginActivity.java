package com.cloudentro.taegrid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button buttonLogin;
   // CheckBox checkBoxRememberOrgId;
    EditText editTextOrgId,editTextUsername,editTextPasswod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide titlebar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        //Hide titlebar complete

        //change status bar color
        Window window= LoginActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this,R.color.orangy));
        }
        //change status bar color

        //if org id exists then direcctly move to dashboard
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("Create_Class",MODE_PRIVATE);
        if (sharedPreferences.contains("schoolId")){
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();
        }
        //if org id exists then direcctly move to dashboard

        setContentView(R.layout.activity_login);

        //intital view
        initView();
        //intital view

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextOrgId.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please add Organization ID",Toast.LENGTH_LONG).show();
                }else {
                    SharedPreferences sharedPreferencesid=getApplicationContext().getSharedPreferences("Create_Class",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferencesid.edit();
                    editor.putString("schoolId",editTextOrgId.getText().toString().trim());
                    editor.commit();

                    startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                    finish();

                }
            }
        });

    }

    private void initView() {
        buttonLogin=findViewById(R.id.btnLogin);
        //checkBoxRememberOrgId=findViewById(R.id.checkboxOrgID);
        editTextOrgId=findViewById(R.id.txtOrgID);
       /* editTextUsername=findViewById(R.id.txtEmail);
        editTextPasswod=findViewById(R.id.txtPassword);*/
    }
}
