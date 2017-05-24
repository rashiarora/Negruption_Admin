package com.codeogic.negruption_admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    ImageView imageViewSplash;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageViewSplash=(ImageView)findViewById(R.id.imageViewSplash) ;


        Animation animation= AnimationUtils.loadAnimation(this,R.anim.frame_animation);
        imageViewSplash.startAnimation(animation);

        AnimationDrawable animationDrawable=(AnimationDrawable)imageViewSplash.getBackground();
        animationDrawable.start();


        sharedPreferences = getSharedPreferences(Util.PREFS_NAME, MODE_PRIVATE);

        boolean isLoginOrRegistered = sharedPreferences.contains(Util.PREFS_KEYUSERNAME);

        if (isLoginOrRegistered) {
            handler.sendEmptyMessageDelayed(100, 3000);
        } else {
            handler.sendEmptyMessageDelayed(200,3000);
        }
    }



    Handler handler=new Handler(){
        public void handleMessage(Message message){

            if (message.what==100){

                Intent i =new Intent(SplashActivity.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
            else if (message.what==200){

                Intent i =new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }

        }


    };
    }

