package com.example.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private Activity constant =  this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ImageView imgLogo = (ImageView)findViewById(R.id.img_logo);

        AnimatedVectorDrawable d = (AnimatedVectorDrawable) getDrawable(R.drawable.vector_logo_anim); // Insert your AnimatedVectorDrawable resource identifier
        imgLogo.setImageDrawable(d);
        d.start();

        Thread splashThread = new Thread(){
            @Override
            public void run(){
                try{
                    int waited = 0;
                    while(waited < 30000){
                        sleep(100);
                        waited += 100;
                    }
                }catch(InterruptedException e){
                    //do nothing
                }finally{
                    startActivity(new Intent(constant,ProjectListActivity.class));
                    finish();
                }
            }
        };

        splashThread.start();
    }

}
