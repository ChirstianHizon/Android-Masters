package com.example.chris.androidmasters.Events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.chris.androidmasters.Objects.Events;
import com.example.chris.androidmasters.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final Events events = task.getResult().toObject(Events.class);

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
