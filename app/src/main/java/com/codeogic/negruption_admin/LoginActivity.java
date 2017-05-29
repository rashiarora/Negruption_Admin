package com.codeogic.negruption_admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText uname,password;
    Button login,register;
    RequestQueue requestQueue;
    public static final int REQUEST_CODE=101;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    Admin admin;
    ProgressDialog progressDialog;

    public boolean validateLogin(){

        boolean flag =true;

        if (admin.getUsername().isEmpty()){

            flag=false;
            uname.setError(" Username Cannot Be Empty ");
            uname.requestFocus();
        }
        else if (admin.getUsername().contains(" ")){
            flag=false;
            uname.setError("No Spaces Allowed");
            uname.requestFocus();
        }

        if(admin.getPassword().isEmpty()){
            flag=false;
            password.setError("Password Cannot Be Empty");
            password.requestFocus();

        }
        else if (admin.getPassword().contains(" ")){

            flag=false;
            password.setError("No Spaces Allowed");
            password.requestFocus();
        }

        return flag;

    }




    public boolean isNetworkConnected(){

        connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo=connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }



    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public void init(){

        uname=(EditText)findViewById(R.id.loginUsername);
        password=(EditText)findViewById(R.id.loginPassword);
        login=(Button)findViewById(R.id.btnLogin);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();


        if (checkPermission()){

            // Toast.makeText(this,"Permissions Are Already Granted",Toast.LENGTH_LONG).show();
        }
        else {
            // Toast.makeText(this,"Permissions are not granted ",Toast.LENGTH_LONG).show();
            requestPermission();
        }

        login.setOnClickListener(this);

        admin= new Admin();


        requestQueue= Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        sharedPreferences=getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnLogin){

            admin.setUsername(uname.getText().toString().trim());
            admin.setPassword(password.getText().toString().trim());

            if (validateLogin()){
                if (isNetworkConnected()) {
                    loginIntoCloud();
                }else
                {

                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("No Network");
                    builder.setMessage(" Please Turn On The Internet ");

                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            LoginActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));

                            Toast.makeText(LoginActivity.this,"Clicked Okay",Toast.LENGTH_LONG).show();


                        }
                    });


                    builder.create().show();
                }}

            else {

                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Invalid Inputs");
                builder.setMessage(" Please Enter Correct Login Details ");

                builder.setNeutralButton("Got It", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                builder.create().show();
                //   Toast.makeText(this," The Inputs Are Not Valid",Toast.LENGTH_LONG).show();



            }

        }


    }
    private void requestPermission() {

        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE,RECORD_AUDIO}, REQUEST_CODE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean WritePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if (WritePermission && ReadPermission) {

                        Toast.makeText(LoginActivity.this, "Permissions Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Permissions Denied",Toast.LENGTH_LONG).show();

                        AlertDialog.Builder builder=new AlertDialog.Builder(this);
                        builder.setTitle("Permissions Required");
                        builder.setMessage("Kindly Grant The Permissions For Proper Working of Application ");

                        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                requestPermission();
                                Toast.makeText(LoginActivity.this,"Clicked Okay",Toast.LENGTH_LONG).show();


                            }
                        });


                        builder.create().show();

                        // finish();


                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);


        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED ;
    }

    void loginIntoCloud(){

        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.LOGIN_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    int success = jsonObject.getInt("success");
                    int userID = jsonObject.getInt("userID");

                    if(success==1){
                        editor.putInt(Util.PREFS_KEYUSERID,userID);
                        editor.putString(Util.PREFS_KEYUSERNAME,admin.getUsername());
                        editor.putString(Util.PREFS_KEYPASSWORD,admin.getPassword());
                        editor.commit();
                        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(i);
                        finish();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,"Response: "+response,Toast.LENGTH_LONG).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,"Some Error"+error,Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("username",admin.getUsername());
                map.put("password",admin.getPassword());
                Log.i("userName",admin.getUsername() + admin.getPassword());
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }

}
