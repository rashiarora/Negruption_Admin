package com.codeogic.negruption_admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener {
    EditText name,phone,email,username,password,password1;
    Button register;
    RadioButton male,female;

    Admin admin,uIntent;
    RequestQueue requestQueue;

    RadioGroup radioGroupGender;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    ProgressDialog progressDialog;


    public boolean isNetworkConnected(){

        connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        networkInfo=connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }


    public void init() {

        name = (EditText) findViewById(R.id.registerName);
        phone = (EditText) findViewById(R.id.registerPhone);
        email = (EditText) findViewById(R.id.registerEmail);
        username = (EditText) findViewById(R.id.registerUsername);
        password = (EditText) findViewById(R.id.registerPassword);
        password1 = (EditText) findViewById(R.id.registerPassword1);
        male = (RadioButton) findViewById(R.id.rbMale);
        female = (RadioButton) findViewById(R.id.rbFemale);
        register = (Button) findViewById(R.id.btnRegister1);
        radioGroupGender = (RadioGroup) findViewById(R.id.radioGroup);
        requestQueue = Volley.newRequestQueue(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        male.setOnCheckedChangeListener(this);
        female.setOnCheckedChangeListener(this);
        register.setOnClickListener(this);

        admin=new Admin();
        sharedPreferences=getSharedPreferences(Util.PREFS_NAME,MODE_PRIVATE);
        editor=sharedPreferences.edit();

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.btnRegister1){

            admin.setName(name.getText().toString().trim());
            admin.setPhone(phone.getText().toString().trim());
            admin.setEmail(email.getText().toString().trim());
            admin.setUsername(username.getText().toString().trim());
            admin.setPassword(password.getText().toString().trim());

            if (validateFields()){
                if (isNetworkConnected()){
                    insertIntoCloud();
                }
                else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setTitle("No Network");
                    builder.setMessage(" Please Turn On The Internet ");

                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            RegisterActivity.this.startActivity(new Intent(Settings.ACTION_SETTINGS));

                            Toast.makeText(RegisterActivity.this,"Clicked Okay",Toast.LENGTH_LONG).show();


                        }
                    });


                    builder.create().show();

                }}
            else {

                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Invalid Inputs");
                builder.setMessage(" Please Enter Correct Details for Registration ");

                builder.setNeutralButton("Got It", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                builder.create().show();

            }


        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id=buttonView.getId();
        if (isChecked){
            if (id==R.id.rbMale){
                admin.setGender("male");

            }
            else
            {
                admin.setGender("female");

            }
        }

    }

    public void clear(){

        name.setText("");
        phone.setText("");
        email.setText("");
        male.setChecked(false);
        female.setChecked(false);
        username.setText("");
        password.setText("");


    }

    public boolean validateFields(){

        boolean flag=true;


        if (admin.getName().isEmpty()){
            flag=false;
            name.setError(" Name Cannot Be Empty ");
            name.requestFocus();

        }

        if (admin.getPhone().isEmpty()){

            flag=false;
            phone.setError(" Phone Number Cannot Be Empty ");
            phone.requestFocus();
        }else if (admin.getPhone().length()<10){

            flag=false;
            phone.setError(" Please Enter 10 digits Phone Number ");
            phone.requestFocus();
        }
        else if (admin.getPhone().contains(" ")){
            flag=false;
            phone.setError("No Spaces Allowed");
            phone.requestFocus();
        }

        if (admin.getEmail().isEmpty()){
            flag=false;
            email.setError("Email Cannot Be Empty");
            email.requestFocus();
        }
        else if (!(admin.getEmail().contains("@")&& admin.getEmail().contains("."))){
            flag=false;
            email.setError("Please Enter Valid Email");
            email.requestFocus();

        }
        else if (admin.getEmail().contains(" ")){
            flag=false;
            email.setError("No spaces allowed");
            email.requestFocus();
        }

        if (admin.getUsername().isEmpty()){

            flag=false;
            username.setError(" Username Cannot Be Empty ");
            username.requestFocus();
        }else if (admin.getUsername().length()<5){

            flag=false;
            username.setError(" Username Should Be Minimum 5 characters long");
            username.requestFocus();
        }
        else if (admin.getUsername().contains(" ")){
            flag=false;
            username.setError("No Spaces Allowed");
            username.requestFocus();
        }

        if(admin.getPassword().isEmpty()){
            flag=false;
            password.setError("Password Cannot Be Empty");
            password.requestFocus();

        }else if (admin.password.length()<6){
            flag=false;
            password.setError("Choose A Strong Password Of Minimum Length 6");
            password.requestFocus();

        }
        else if(!(password1.getText().toString().trim()).equals(admin.getPassword())){

            flag=false;
            password1.setError("The Password Does not Match , Please Re-Enter");
            password1.requestFocus();
        }
        else if (admin.getPassword().contains(" ")){

            flag=false;
            password.setError("No Spaces Allowed");
            password.requestFocus();
        }

        if (radioGroupGender.getCheckedRadioButtonId() == -1){
            flag=false;

            male.setError("No Gender Selected");
            female.setError("No Gender Selected");

            Toast.makeText(this,"Please Select Gender",Toast.LENGTH_LONG).show();


        }

        return flag;
    }

    void insertIntoCloud(){
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.INSERT_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success=jsonObject.getInt("success");
                    String message=jsonObject.getString("message");
                    int id = jsonObject.getInt("insertedId");


                    if (success==1){
                        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();


                            editor.putInt(Util.PREFS_KEYUSERID, id);
                            editor.putString(Util.PREFS_KEYUSERNAME, admin.getUsername());
                            editor.putString(Util.PREFS_KEYPASSWORD, admin.getPassword());

                            editor.commit();

                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            //intent.putExtra("currentUser",user);
                            startActivity(intent);
                            finish();

                    }
                    else {
                        Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this,"Oops! Exception happened : "+e,Toast.LENGTH_LONG).show();
                }


                progressDialog.dismiss();
                //Toast.makeText(RegisterActivity.this,"Response :"+response,Toast.LENGTH_LONG).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("error",error.toString());
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,"Some Error "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();

                map.put("name1",admin.getName());
                map.put("phone1",admin.getPhone());
                map.put("email1",admin.getEmail());
                map.put("gender1",admin.getGender());
                map.put("username1",admin.getUsername());
                map.put("password1",admin.getPassword());


                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
        clear();

    }
}
