package com.example.chris.androidmasters.Events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chris.androidmasters.Functions.ElapsedTime;
import com.example.chris.androidmasters.Objects.Events;
import com.example.chris.androidmasters.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class EventViewActivity extends AppCompatActivity {

    private Activity context = this;
    private String id;
    private Events events;
    private String TAG = "EVENT_VIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        getSupportActionBar().setTitle("Events");
        //        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        getEvent(db.collection("Events").document(id));


    }

    private void getEvent(DocumentReference query) {
        query.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("EVENTS_VIEW", String.valueOf(documentSnapshot.getData()));
                final Events events = documentSnapshot.toObject(Events.class);
                Log.d("EVENTS_VIEW",events.getName());

                TextView eventname = (TextView)findViewById(R.id.tv_event);
                TextView eventdesc = (TextView)findViewById(R.id.tv_desc);
                TextView eventlocation = (TextView)findViewById(R.id.tv_location);
                TextView eventdate = (TextView)findViewById(R.id.tv_date);
                TextView eventtime = (TextView)findViewById(R.id.tv_time);
                TextView eventremain = (TextView)findViewById(R.id.tv_remain);

                Button btnvolunteer = (Button)findViewById(R.id.btn_volunteer);

                eventname.setText(events.getName());
                eventdesc.setText(events.getDescription());
                eventlocation.setText(events.getLocation());


                Date now = new Date();
                Date completion = events.getDate_event();

                String day          = (String) DateFormat.format("dd",   completion);
                String monthString  = (String) DateFormat.format("MMM",  completion);
                String year         = (String) DateFormat.format("yyyy", completion);
                String hour         = (String) DateFormat.format("hh", completion);
                String min         = (String) DateFormat.format("mm", completion);

                eventdate.setText(monthString +" "+day+", "+year);
                eventtime.setText(hour+" : "+min);

                if(events.getVolunteers()){
                    btnvolunteer.setVisibility(View.VISIBLE);
                }else{
                    btnvolunteer.setVisibility(View.GONE);
                }

                ElapsedTime elapsed = new ElapsedTime(now,completion);

                if(elapsed.getDay() != 0 ){
                    eventremain.setText(elapsed.getDay() + " days remaining");
                }else if(elapsed.getHour()!= 0 ){
                    eventremain.setText(elapsed.getHour() + " hours remaining");
                }else if(elapsed.getMinute() != 0 ){
                    eventremain.setText(elapsed.getMinute() + " mins remaining");
                }else{
                    eventremain.setText("Finished");
                    btnvolunteer.setVisibility(View.GONE);
                }

                Button btnCalendar = (Button)findViewById(R.id.btn_addEvent);
                btnCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addtoCalendar(events);
                    }
                });


            }
        });
    }

    public void addtoCalendar(Events events) {

        Calendar dtend = Calendar.getInstance();
        dtend.setTime( events.getDate_event());

        Calendar dtstart = Calendar.getInstance();
        dtstart.setTime(new Date());

        Log.d("CALENDAR DATES", String.valueOf(dtend.getTime()));
        Log.d("CALENDAR DATES", String.valueOf(dtstart.getTime()));

//      TODO: Fix the date start and end
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.DTSTART, dtstart.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.DTEND, dtend.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.TITLE, events.getName());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, events.getDescription());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, events.getLocation());
        startActivity(intent);
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
