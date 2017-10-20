package com.example.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class SplashActivity extends AppCompatActivity {

    private Activity context =  this;
    private FirebaseAuth mAuth;
    private String TAG = "SPLASH_PAGE";
    private FirebaseUser User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setPersistence();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();

        if(User == null){
            signInUser();
        }else{
            splashpageCounter();
        }
    }

    private void setPersistence(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (db != null) {
            db.setFirestoreSettings(settings);
        }
    }

    private void signInUser(){
        final LinearLayout lvprogress = (LinearLayout) findViewById(R.id.lv_progress);
        final TextView tvstatus = (TextView)findViewById(R.id.tv_status);
        final ProgressBar pbstatus = (ProgressBar)findViewById(R.id.pb_status);
        lvprogress.setVisibility(View.VISIBLE);
        mAuth.signInAnonymously()
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        lvprogress.setVisibility(View.GONE);
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success");
                        User = mAuth.getCurrentUser();
                        splashpageCounter();

                    } else {
                        pbstatus.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.getException());
                        tvstatus.setText("Unable to Authenticate User");

                    }
                }
            });
    }

    private void splashpageCounter(){
        ImageView imgLogo = (ImageView)findViewById(R.id.img_logo);

        AnimatedVectorDrawable d = (AnimatedVectorDrawable) getDrawable(R.drawable.avd_logo); // Insert your AnimatedVectorDrawable resource identifier
        imgLogo.setImageDrawable(d);
        d.start();

        Thread splashThread = new Thread(){
            @Override
            public void run(){
                try{
                    int waited = 0;
                    while(waited < 3500){
                        sleep(100);
                        waited += 100;
                    }
                }catch(InterruptedException e){
                    //do nothing
                }finally{
                    startActivity(new Intent(context,IntroActivity.class));
                    finish();
                }
            }
        };

        splashThread.start();
    }

}
