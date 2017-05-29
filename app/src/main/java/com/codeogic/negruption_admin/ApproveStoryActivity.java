package com.codeogic.negruption_admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ApproveStoryActivity extends AppCompatActivity  {
    @InjectView(R.id.textViewsName_approve)
    TextView txtUserName;

    @InjectView(R.id.textViewsStoryTitle_approve)
    TextView txtStoryTitle;

    @InjectView(R.id.textViewsStoryDesc_approve)
    TextView txtStoryDesc;

    @InjectView(R.id.textViewsDepartment_approve)
    TextView txtDepartment;

    @InjectView(R.id.textViewsPlace_approve)
    TextView txtPlace;

    @InjectView(R.id.sImageView_approve)
    ImageView imageView;

    @InjectView(R.id.btnsPlay_approve)
    Button btnPlay;

    @InjectView(R.id.btnsPause_approve)
    Button btnPause;

    @InjectView(R.id.btnsStop_approve)
    Button btnStop;

   /* @InjectView(R.id.sVideoView_approve)
    VideoView videoView;

    @InjectView(R.id.btnVideoPlay_approve)
    ImageButton videoPlay;
*/
    @InjectView(R.id.btnAccept)
            Button btnAccept;

    @InjectView(R.id.btnReject)
            Button btnReject;

    MediaPlayer mediaPlayer;

    ProgressDialog progressDialog;

    RequestQueue requestQueue;

    StoryBean story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_story);
        ButterKnife.inject(this);



        Intent rcv = getIntent();
        story = (StoryBean)rcv.getSerializableExtra("keyApproveStory");

        txtUserName.setText(story.getUsername());
        txtStoryTitle.setText(story.getStoryTitle());
        txtDepartment.setText(story.getDepartment());
        txtPlace.setText(story.getPlace());
        txtStoryDesc.setText(story.getStoryDesc());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        requestQueue = Volley.newRequestQueue(this);



        progressDialog.show();


        Log.i("info_approve",story.toString());
        Toast.makeText(this,story.getImageProof() + story.getAudioProof() + story.getVideoProof(),Toast.LENGTH_LONG).show();


        if (!story.getImageProof().equals("null")){
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(this).load(story.getImageProof()).into(imageView);
            progressDialog.dismiss();
        }

        if (!story.getAudioProof().equals("null")){
            btnPlay.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.VISIBLE);

            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(this, Uri.parse(story.getAudioProof()));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

       /* if (!(story.getVideoProof().equals("null"))){
            videoView.setVisibility(View.VISIBLE);
            videoPlay.setVisibility(View.VISIBLE);
            videoView.setMediaController(new MediaController(this));
            videoView.setVideoURI(Uri.parse(story.getVideoProof()));
            videoView.requestFocus();
            progressDialog.dismiss();

        }
*/
        if (story.getCategory().equals("Honest")){
            progressDialog.dismiss();
            imageView.setVisibility(View.GONE);
            btnPlay.setVisibility(View.GONE);
            btnPause.setVisibility(View.GONE);
            btnStop.setVisibility(View.GONE);
           /* videoView.setVisibility(View.GONE);
            videoPlay.setVisibility(View.GONE);*/

        }

        if (story.getImageProof().equals("null")&&story.getAudioProof().equals("null")){
            progressDialog.dismiss();
            imageView.setVisibility(View.GONE);
            btnPlay.setVisibility(View.GONE);
            btnPause.setVisibility(View.GONE);
            btnStop.setVisibility(View.GONE);

        }

    }



   /* public void btnVideoPlay(View view){
        videoView.start();
        videoPlay.setVisibility(View.GONE);

    }
*/

    public void clickPlay(View view){
        mediaPlayer.start();

    }

    public void clickPause(View view){
        mediaPlayer.pause();

    }

    public void clickStop(View view){
        mediaPlayer.stop();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }

      /*  if (videoView!=null){
            videoView.stopPlayback();
            videoView = null;
        }*/
    }

    public void clickAccept(View view){
        acceptStory();

    }

    public void clickReject(View view){
        rejectStory();
    }

    void acceptStory(){
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.APPROVE_STORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    if(success == 1){

                        Toast.makeText(ApproveStoryActivity.this,message,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(ApproveStoryActivity.this,ApproveStoryList.class);
                        startActivity(i);
                         finish();

                    }else{
                         Toast.makeText(ApproveStoryActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                     progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();

                    progressDialog.dismiss();
                    Toast.makeText(ApproveStoryActivity.this,"Some Exception"+e,Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ApproveStoryActivity.this,"Volley Error"+error.getMessage(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
                map.put("sid",String.valueOf(story.getStoryId()));

                return map;
            }
        };
        requestQueue.add(request);request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }



    void rejectStory(){

        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Util.REJECT_STORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String message = jsonObject.getString("message");

                    if(success == 1){

                        Toast.makeText(ApproveStoryActivity.this,message,Toast.LENGTH_LONG).show();
                       /* Intent i = new Intent(ApproveStoryActivity.this,ApproveStoryList.class);
                        startActivity(i);
                        finish();
*/
                    }else{
                        Toast.makeText(ApproveStoryActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                }catch (Exception e){
                    e.printStackTrace();

                    progressDialog.dismiss();
                    Toast.makeText(ApproveStoryActivity.this,"Some Exception"+e,Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ApproveStoryActivity.this,"Volley Error"+error.getMessage(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
                map.put("storyId",String.valueOf(story.getStoryId()));

                return map;
            }
        };
        requestQueue.add(request);request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);

    }
}
