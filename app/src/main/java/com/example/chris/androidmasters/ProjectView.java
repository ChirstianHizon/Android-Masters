package com.example.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.androidmasters.Adapters.ProjectContactsAdapter;
import com.example.chris.androidmasters.Functions.ElapsedTime;
import com.example.chris.androidmasters.Objects.Contacts;
import com.example.chris.androidmasters.Objects.Details;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectView extends AppCompatActivity {

    private static String id;
    private Activity context = this;
    private FirebaseFirestore db;
    private String TAG = "PROJECT_VIEW";
    private String title;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private boolean isShow = false;
    private int scrollRange = -1;
    private Button btndonate;
    private TextView tvviewevents;

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

        btndonate = (Button)findViewById(R.id.btn_donate);
        btndonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProjectView.this, DonateActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });
        tvviewevents = (TextView)findViewById(R.id.tv_viewevents);
        tvviewevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });

        db = FirebaseFirestore.getInstance();

//        --------- GETS AND SETS ALL DATA NEEDED -----------

        getProjectContacts();
        getProjectDetails();
        getProjectInfo();



//        ----------- Click to Share ------------------------
        ImageButton btnshare =
                (ImageButton)findViewById(R.id.btn_share);
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Help us in this Project";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Choose Where to Share the Project"));
            }
        });


//        ------ REMOVES TEXT WHEN COLLAPSE BAR IS COLLAPSED --------
        setTextVisibility();


    }


    private void getProjectDetails(){
        DocumentReference docRef = db.collection("Details").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null) {

                        Details details = snapshot.toObject(Details.class);
                        setDetails(details);

                    }
                }
            }
        });
    }

    private void getProjectInfo(){

        final ProgressBar pbprogress = (ProgressBar) findViewById(R.id.pb_progress);
        final TextView tvcurrent = (TextView) findViewById(R.id.tv_current);
        final TextView tvgoal = (TextView) findViewById(R.id.tv_goal);
        final TextView compDate = (TextView)findViewById(R.id.tv_completion_date);
        final TextView dateRemain = (TextView)findViewById(R.id.tv_days_remain);

        DocumentReference docRef = db.collection("Projects").document(id);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if(snapshot != null){

                    Date now = new Date();
                    Date completion = snapshot.getDate("completion_date");

                    ElapsedTime elapsed = new ElapsedTime(now,completion);

                    String day          = (String) DateFormat.format("dd",   completion);
                    String monthString  = (String) DateFormat.format("MMM",  completion);
                    String year         = (String) DateFormat.format("yyyy", completion);

                    compDate.setText(monthString +" "+day+", "+year);

                    if(elapsed.getDay() > 0 ){
                        dateRemain.setText(elapsed.getDay() + " days remaining");
                    }else if(elapsed.getHour() > 0 ){
                        dateRemain.setText(elapsed.getHour() + "hours remaining");
                    }else if(elapsed.getMinute() > 0 ){
                        dateRemain.setText(elapsed.getMinute() + "minutes remaining");
                    }else{
                        dateRemain.setText("Project has Ended");
                        btndonate.setVisibility(View.GONE);
                    }


                    Double goal = Double.valueOf(snapshot.getString("goal"));
                    Double current = Double.valueOf(snapshot.getString("current"));

                    Double progress = Double.valueOf(snapshot.getString("current")) / Double.valueOf(snapshot.getString("goal")) * 100;

                    String currency = "₱";
                    tvgoal.setText(currency+""+ NumberFormat.getIntegerInstance().format(Integer.valueOf(snapshot.getString("goal"))));
                    tvcurrent.setText(currency+""+NumberFormat.getIntegerInstance().format(Integer.valueOf(snapshot.getString("current"))));

                    if(current > goal){
                        pbprogress.getProgressDrawable().setColorFilter(Color.parseColor("#FF00C853"), PorterDuff.Mode.SRC_IN);
                    }else if(current.equals(goal)){
                        pbprogress.getProgressDrawable().setColorFilter(Color.parseColor("#FF00C853"), PorterDuff.Mode.SRC_IN);
                    }else if(current.equals(0)){
                        pbprogress.getProgressDrawable().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.SRC_IN);
                    }else{
                        pbprogress.getProgressDrawable().setColorFilter(Color.parseColor("#FF9800"), PorterDuff.Mode.SRC_IN);
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pbprogress.setProgress(progress.intValue(),true);
                    }else{
                        pbprogress.setProgress(progress.intValue());
                    }

                }else{
                    Toast.makeText(context, "Can't Find Project", Toast.LENGTH_SHORT).show();
                    finish();
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
        recmain.setNestedScrollingEnabled(false);
        recmain.setAdapter(adapter);

        DocumentReference docRef = db.collection("Contacts").document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot != null) {

                        Log.d(TAG,snapshot.getData().toString());
                        Log.d(TAG, String.valueOf(snapshot.getData().size()));


                        try {
                            JSONObject obj = new JSONObject(snapshot.getData());
                            JSONArray names = new JSONArray(obj.getString("Names"));
                            JSONArray contacts = new JSONArray(obj.getString("Contacts"));
                            JSONArray positions = new JSONArray(obj.getString("Positions"));

                            int size = names.length();

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
            }
        });
    }

    private void setDetails(Details details){

        TextView Title = (TextView)findViewById(R.id.tv_project_title);
        TextView Organization = (TextView)findViewById(R.id.tv_organization);
        TextView shortdesc = (TextView)findViewById(R.id.tv_project_description);

        LinearLayout llimagedisplay = (LinearLayout)findViewById(R.id.ll_image_display);
        LinearLayout llobjectives = (LinearLayout)findViewById(R.id.ll_objectives);


        List<String> objectives;

        if( details.getObjectives() == null){
            TextView textview = new TextView(this);
            textview.setText("No Objective Set");
            llobjectives.addView(textview);
        }else{
            objectives = details.getObjectives();

            for(int x=0;x<objectives.size();x++){
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView bullet = new TextView(this);
                bullet.setText("\u2022"+ "  ");
                linearLayout.addView(bullet);

                TextView textview = new TextView(this);
                textview.setText(objectives.get(x));

                linearLayout.addView(textview);
                llobjectives.addView(linearLayout);
            }
        }


        Log.d("DETAILS", String.valueOf(details.getObjectives()));
        title = details.getTitle();

        Title.setText(details.getTitle());
        Organization.setText("by " + details.getOrganization());
        shortdesc.setText(details.getShort_description());

        llimagedisplay.removeAllViews();

        changeDisplayImage(details.getDisplay_image());

        int size = details.getImagesSize();
        for(int x = 0;x < size;x++){

            if(details.getSelectedImages(x) != null && !details.getSelectedImages(x).equals("")){
                final ImageView myImage = new ImageView(this);
                myImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                myImage.setImageResource(R.mipmap.ic_launcher);
                llimagedisplay.addView(myImage);

                if(!details.getSelectedImages(x).equals("") && details.getSelectedImages(x) != null){

                    Picasso.Builder builder = new Picasso.Builder(this);
                    builder.listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            myImage.setVisibility(View.GONE);
                        }
                    });
                    // Set to False to remove indicators on upper Left
//                    builder.indicatorsEnabled(true);
                    builder.build()
                            .load(details.getSelectedImages(x))
                            .resize(0,500)
                            .error(R.mipmap.ic_launcher)
                            .into(myImage);
                }else{
                    myImage.setVisibility(View.GONE);
                }

            }
        }

    }

    private void changeDisplayImage(String image){
        ImageView display = (ImageView)findViewById(R.id.app_bar_image);

        if(image != null && !image.equals("")){
            Picasso.with(this)
                    .load(image)
                    .resize(0,800)
                    .error(R.mipmap.ic_launcher)
                    .into(display);
        }
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

    private void isLoading(boolean status){
        ProgressBar pbmain = (ProgressBar)findViewById(R.id.pb_main);
        CardView cvmain = (CardView)findViewById(R.id.cv_main);
        AppBarLayout appbar = (AppBarLayout)findViewById(R.id.app_bar_layout);
        NestedScrollView nsvmain = (NestedScrollView)findViewById(R.id.nsv_main);

        if(status){
            pbmain.setVisibility(View.VISIBLE);
            cvmain.setVisibility(View.GONE);
            appbar.setExpanded(false);
        }else{
            pbmain.setVisibility(View.GONE);
            cvmain.setVisibility(View.VISIBLE);
            appbar.setExpanded(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProjectInfo();
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
