package com.example.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chris.androidmasters.Objects.Details;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class ProjectView extends AppCompatActivity {

    private static String id;
    private Activity context = this;
    private FirebaseFirestore db;
    private String TAG = "PROJECT_VIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

//        ----------- CREATE A BACK BUTTON ------------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


//        ----------- GET ID FROM PROJECT LIST --------------

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
//        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();


        toolbar.setVisibility(View.GONE);

        Button btndonate = (Button)findViewById(R.id.btn_donate);
        btndonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProjectView.this, DonateActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });

        db = FirebaseFirestore.getInstance();
        getProjectDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getProjectId(){
        return id;
    }

    public void changeDisplayImage(String image){
        ImageView display = (ImageView)findViewById(R.id.app_bar_image);

        if(image != null && !image.equals("")){
            Picasso.with(this)
                    .load(image)
                    .resize(800,800)
                    .error(R.mipmap.ic_launcher)
                    .into(display);
        }
    }

    private void getProjectDetails(){
        Log.d(TAG,"ID: "+id);
        DocumentReference docRef = db.collection("Details").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {

                    // GETS AND TRANSFER DATA TO CLASS
                    //TODO: CREATE VALIDATION TO CHECK IF FIELDS ARE PRESENT
                    Details details = snapshot.toObject(Details.class);
                    setDetails(details);

                }else{
                    Log.d(TAG, "Current data: null");
                }


            }
        });

    }

    private void setDetails(Details details){

        TextView Title = (TextView)findViewById(R.id.tv_project_title);
        TextView Organization = (TextView)findViewById(R.id.tv_organization);
        TextView shortdesc = (TextView)findViewById(R.id.tv_project_description);

        LinearLayout llimagedisplay = (LinearLayout)findViewById(R.id.ll_image_display);

        Title.setText(details.getTitle());
        Organization.setText("by " + details.getOrganization());
        shortdesc.setText(details.getShort_description());

        llimagedisplay.removeAllViews();

        changeDisplayImage(details.getDisplay_image());

        int size = details.getImagesSize();
        for(int x = 0;x < size;x++){

            if(details.getSelectedImages(x) != null && !details.getSelectedImages(x).equals("")){
                ImageView myImage = new ImageView(this);
                myImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                myImage.setImageResource(R.mipmap.ic_launcher);
                llimagedisplay.addView(myImage);

                Picasso.with(this)
                        .load(details.getSelectedImages(x))
                        .resize(0,500)
                        .error(R.mipmap.ic_launcher)
                        .into(myImage);
            }
        }

    }

}
