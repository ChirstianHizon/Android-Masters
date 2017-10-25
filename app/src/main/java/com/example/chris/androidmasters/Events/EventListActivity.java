package com.example.chris.androidmasters.Events;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chris.androidmasters.R;

public class EventListActivity extends AppCompatActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
    }

    private void gatherEvents(){



    }
}
