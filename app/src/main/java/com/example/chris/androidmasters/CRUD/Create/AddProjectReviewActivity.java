package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.chris.androidmasters.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddProjectReviewActivity extends AppCompatActivity {
    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_review);

        getSupportActionBar().setTitle("Add Images");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String desc = intent.getStringExtra("desc");
        final String org =  intent.getStringExtra("org");
        final String date = intent.getStringExtra("date");
        final String goal = intent.getStringExtra("goal");
        final String image =  intent.getStringExtra("image");
        final ArrayList objectives = intent.getStringArrayListExtra("objectives");
        final ArrayList person = intent.getStringArrayListExtra("person");
        final ArrayList contacts = intent.getStringArrayListExtra("contact");
        final ArrayList position = intent.getStringArrayListExtra("position");
        final ArrayList imageArray =intent.getStringArrayListExtra("imagesArray");

        ImageView ivimage = (ImageView)findViewById(R.id.iv_image);

        Picasso.with(context)
                .load(Uri.parse(image))
                .placeholder(R.color.white)
                .error(R.color.colorAccent)
                .into(ivimage);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
