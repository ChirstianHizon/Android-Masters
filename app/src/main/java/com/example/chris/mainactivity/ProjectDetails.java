package com.example.chris.mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ProjectDetails extends AppCompatActivity {

    private String id;
    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }
}
