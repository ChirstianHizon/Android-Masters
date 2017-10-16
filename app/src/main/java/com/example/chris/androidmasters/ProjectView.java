package com.example.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chris.androidmasters.Adapters.ProjectContactsAdapter;
import com.example.chris.androidmasters.Objects.Contacts;
import com.example.chris.androidmasters.Objects.Details;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.chris.androidmasters.Objects.Constants.getCurrencySymbol;

public class ProjectView extends AppCompatActivity {

    private static String id;
    private Activity context = this;
    private FirebaseFirestore db;
    private String TAG = "PROJECT_VIEW";
    private String title;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private boolean isShow = false;
    private int scrollRange = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

//        ----------- CREATE A BACK BUTTON ------------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


//        ----------- GET ID FROM PROJECT LIST --------------

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

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

//        --------- GETS AND SETS ALL DATA NEEDED -----------

        getProjectDetails();
        getProjectProgress();
        getProjectContacts();


//        ----------- Click to Share ------------------------
        Button btnshare = (Button)findViewById(R.id.btn_share);
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Text I want to share.";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Choose Where to Share the Project"));
            }
        });


//        ------ REMOVES TEXT WHEN COLLAPSE BAR IS COLLAPSED --------
        setTextVisibility();


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

    private void setDetails(Details details){

        TextView Title = (TextView)findViewById(R.id.tv_project_title);
        TextView Organization = (TextView)findViewById(R.id.tv_organization);
        TextView shortdesc = (TextView)findViewById(R.id.tv_project_description);

        LinearLayout llimagedisplay = (LinearLayout)findViewById(R.id.ll_image_display);

        title = details.getTitle();

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

    private void changeDisplayImage(String image){
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

    private void getProjectProgress(){

        final ProgressBar pbprogress = (ProgressBar) findViewById(R.id.pb_progress);
        final TextView tvcurrent = (TextView) findViewById(R.id.tv_current);
        final TextView tvgoal = (TextView) findViewById(R.id.tv_goal);

        DocumentReference docRef = db.collection("Projects").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());

                    Double goal = Double.valueOf(snapshot.getString("goal"));
                    Double current = Double.valueOf(snapshot.getString("current"));

                    Double progress = Double.valueOf(snapshot.getString("current")) / Double.valueOf(snapshot.getString("goal")) * 100;

                    String currency = getCurrencySymbol("PHP");
                    tvgoal.setText(currency+" "+snapshot.getString("goal"));
                    tvcurrent.setText(currency+" "+snapshot.getString("current"));

                    if(current > goal){
                        pbprogress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    }else if(current.equals(goal)){
                        pbprogress.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                    }else{
                        pbprogress.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pbprogress.setProgress(progress.intValue(),true);
                    }else{
                        pbprogress.setProgress(progress.intValue());
                    }

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }

    private void getProjectContacts(){

        RecyclerView recmain = (RecyclerView)findViewById(R.id.rec_main);
        final List<Contacts> contactList = new ArrayList<Contacts>();
        final ProjectContactsAdapter adapter = new ProjectContactsAdapter(context, contactList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recmain.setHasFixedSize(true);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
        recmain.setAdapter(adapter);

        DocumentReference docRef = db.collection("Contacts").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {

                    Log.d(TAG,snapshot.getData().toString());
                    Log.d(TAG, String.valueOf(snapshot.getData().size()));

                    int size = snapshot.getData().size();

                    JSONObject obj = new JSONObject(snapshot.getData());

                    try {
                        JSONArray names = new JSONArray(obj.getString("Names"));
                        JSONArray contacts = new JSONArray(obj.getString("Contacts"));
                        JSONArray positions = new JSONArray(obj.getString("Positions"));

                        adapter.clear();
                        for(int x = 0;x<size;x++){
                            Log.d(TAG,names.getString(x));
                            contactList.add(
                                    new Contacts(
                                            names.getString(x),
                                            positions.getString(x),
                                            contacts.getString(x)
                                    )
                            );

                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                }

            }
        });
    }

    private void setTextVisibility(){

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_main);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }


            }
        });

    }
}
