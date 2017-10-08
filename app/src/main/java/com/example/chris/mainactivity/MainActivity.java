package com.example.chris.mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.chris.mainactivity.Sample.SampleCheckout;

public class MainActivity extends AppCompatActivity {

    private Activity constant =  this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(constant,SampleCheckout.class));
        finish();
    }

}
