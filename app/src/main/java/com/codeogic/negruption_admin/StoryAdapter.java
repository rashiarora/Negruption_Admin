package com.codeogic.negruption_admin;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 24-05-2017.
 */

public class StoryAdapter extends ArrayAdapter<StoryBean> {
    Context context;
    int resource,views=0,newView=0;
    ArrayList<StoryBean> storyList;
    RequestQueue requestQueue;
    StoryBean story;
    public StoryAdapter( Context context,  int resource,  ArrayList<StoryBean> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        storyList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(resource,parent,false);
        requestQueue = Volley.newRequestQueue(getContext());


        TextView txtName = (TextView)view.findViewById(R.id.textViewName);
        TextView txtTitle = (TextView)view.findViewById(R.id.textViewStoryTitle);
        TextView txtDescription = (TextView)view.findViewById(R.id.textViewStoryDesc);
        TextView txtViews = (TextView)view.findViewById(R.id.textViewViews);

        TextView txtReadMore = (TextView)view.findViewById(R.id.textViewReadMore);

        story = storyList.get(position);
        txtName.setText(story.getUsername());
        txtTitle.setText(story.getStoryTitle());
        txtDescription.setText(story.getStoryDesc());
        txtViews.setText(String.valueOf(story.getViews()));
        txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.textViewReadMore){
                    StoryBean storyBean = storyList.get(position);
                    Log.i("RStory",story.toString());
                    Toast.makeText(getContext(),"Read More",Toast.LENGTH_LONG).show();
                    views = story.getViews();
                    newView = views+1;
                    Intent intent = new Intent(context,StoryActivity.class);
                    intent.putExtra("keyStory",storyBean);
                    context.startActivity(intent);
                    Task t = new Task();
                    t.execute();
                }
            }
        });
        return view;
    }

    class Task extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            StringRequest request = new StringRequest(Request.Method.POST, Util.UPDATE_VIEWS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        int success = jsonObject.getInt("success");
                        String message = jsonObject.getString("message");

                        if(success == 1){

                            //Toast.makeText(ManageAccountActivity.this,message,Toast.LENGTH_LONG).show();
                            //Intent i = new Intent(ManageAccountActivity.this,SplashActivity.class);
                            //startActivity(i);
                            // finish();
                            Log.i("success",message);
                        }else{
                            // Toast.makeText(ManageAccountActivity.this,message,Toast.LENGTH_LONG).show();
                        }
                        // progressDialog.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();

                        Log.i("exception",e.getMessage());
                        //progressDialog.dismiss();
                        //Toast.makeText(ManageAccountActivity.this,"Some Exception"+e,Toast.LENGTH_LONG).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("error",error.getMessage());
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put("views",String.valueOf(newView));
                    map.put("sid",String.valueOf(story.getStoryId()));

                    return map;
                }
            };
            requestQueue.add(request);request.setRetryPolicy(new DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
            return null;
        }
    }
}
