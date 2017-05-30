package com.codeogic.negruption_admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApproveStoryList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView approveStories;
    ArrayList<StoryBean> stories;
    ApproveAdapter adapter;
    StoryBean storyBean;
    RequestQueue requestQueue;
   // ProgressDialog progressDialog;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_story_list);
        approveStories = (ListView)findViewById(R.id.approveListView);
       /* progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);*/
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = Volley.newRequestQueue(this);

       swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.approveSwipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveStories();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                retrieveStories();
            }
        });

        retrieveStories();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    void retrieveStories(){
       // progressDialog.show();
        stories = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.GET, Util.RETRIEVE_APPROVAL_STORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("stories");

                    int  sid=0,views =0,status = 0;
                    String username="",title="",description="",privacy="",dep = " ",pl = " ",u = "", img = " ", aud = " ", vid = " ", cat = " ";
                    stories.clear();
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);

                        username = jObj.getString("name");
                        sid = jObj.getInt("storyId");
                        title = jObj.getString("storyTitle");
                        description = jObj.getString("storyDesc");
                        dep = jObj.getString("department");
                        pl = jObj.getString("place");
                        privacy = jObj.getString("privacy");
                        img = jObj.getString("imageProof");
                        aud = jObj.getString("audioProof");
                        vid = jObj.getString("videoProof");
                        cat = jObj.getString("category");
                        views = jObj.getInt("views");
                        status = jObj.getInt("status");

                        if (privacy.equals("Anonymous"))
                            u = "Anonymous";
                        else
                            u = username;



                            stories.add(new StoryBean(0,sid,title,dep,pl,description,img,aud,vid,views,u,null,cat,status));

                    }

                    adapter = new ApproveAdapter(ApproveStoryList.this,R.layout.approve_list_item,stories);

                    approveStories.setAdapter(adapter);
                    approveStories.setOnItemClickListener(ApproveStoryList.this);
                   // progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);

                }catch (Exception e){
                    e.printStackTrace();
                    //progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                  //  Toast.makeText(ApproveStoryList.this,"Some Exception"+ e,Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
              //  Toast.makeText(ApproveStoryList.this,"Some Error"+error,Toast.LENGTH_LONG).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
        requestQueue.add(request);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        storyBean = stories.get(position);
       // Toast.makeText(ApproveStoryList.this,"You clicked"+storyBean.getUsername(),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ApproveStoryList.this,ApproveStoryActivity.class);
        intent.putExtra("keyApproveStory",storyBean);
        startActivity(intent);
    }
}
