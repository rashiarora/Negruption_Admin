package com.codeogic.negruption_admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

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

    @InjectView(R.id.sVideoView_approve)
    VideoView videoView;

    @InjectView(R.id.btnVideoPlay_approve)
    ImageButton videoPlay;

    @InjectView(R.id.btnAccept)
            Button btnAccept;

    @InjectView(R.id.btnReject)
            Button btnReject;

    MediaPlayer mediaPlayer;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_story);
        ButterKnife.inject(this);

        Intent rcv = getIntent();
        StoryBean story = (StoryBean)rcv.getSerializableExtra("keyApproveStory");

        txtUserName.setText(story.getUsername());
        txtStoryTitle.setText(story.getStoryTitle());
        txtDepartment.setText(story.getDepartment());
        txtPlace.setText(story.getPlace());
        txtStoryDesc.setText(story.getStoryDesc());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");



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

        if (!(story.getVideoProof().equals("null"))){
            videoView.setVisibility(View.VISIBLE);
            videoPlay.setVisibility(View.VISIBLE);
            videoView.setMediaController(new MediaController(this));
            videoView.setVideoURI(Uri.parse(story.getVideoProof()));
            videoView.requestFocus();
            progressDialog.dismiss();

        }
    }

    public void btnVideoPlay(View view){
        videoView.start();
        videoPlay.setVisibility(View.GONE);

    }


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

        if (videoView!=null){
            videoView.stopPlayback();
            videoView = null;
        }
    }

    public void clickAccept(View view){
        acceptStory();

    }

    public void clickReject(View view){
        rejectStory();
    }

    void acceptStory(){

    }

    void rejectStory(){

    }
}
