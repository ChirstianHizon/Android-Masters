package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.androidmasters.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddProjectReviewActivity extends AppCompatActivity {
    private Activity context = this;
    private FirebaseFirestore db;
    private Date fbdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_review);


        db = FirebaseFirestore.getInstance();

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


        TextView tvname = (TextView)findViewById(R.id.tv_name);
        TextView tvdesc = (TextView)findViewById(R.id.tv_desc);
        TextView tvgoal = (TextView)findViewById(R.id.tv_goal);
        TextView tvdate = (TextView)findViewById(R.id.tv_date);


        ImageView ivimage = (ImageView)findViewById(R.id.iv_image);

        Picasso.with(context)
                .load(Uri.parse(image))
                .placeholder(R.color.white)
                .error(R.color.colorAccent)
                .into(ivimage);

                fbdate = new Date();
                try {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
                    fbdate = sdf.parse(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

        Button btnsubmit = (Button)findViewById(R.id.btn_submit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> details = new HashMap<>();
                details.put("added_date", new Date());
//                details.put("completion_date", fbdate);
//                details.put("current", "0");
//                details.put("goal", goal);
//                details.put("search", name.toLowerCase());
//                details.put("name", name);
//                details.put("organiztaion", org);



                db.collection("Sample").add(details)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(context, "SUCAKSESSSS", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FIREBASE", "Error adding document", e);
                                Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


    }

//    private void G

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
