package com.projectboost.chris.androidmasters.Events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.projectboost.chris.androidmasters.Adapters.EventListAdapter;
import com.projectboost.chris.androidmasters.Objects.Events;
import com.projectboost.chris.androidmasters.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    private Activity context = this;
    private String id;
    private FirebaseFirestore db;
    private RecyclerView recmain;
    private List<Events> eventlist;
    private EventListAdapter adapter;
    private String TAG = "EVENT_LIST";
    private ProgressBar progbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        Log.d(TAG, id);
        getSupportActionBar().setTitle("Events");
        //        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = FirebaseFirestore.getInstance();
        progbar = (ProgressBar)findViewById(R.id.pb_main);
        recmain = (RecyclerView) findViewById(R.id.rec_main);
        eventlist = new ArrayList<Events>();
        adapter = new EventListAdapter(context, eventlist);

        recmain.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
        recmain.setAdapter(adapter);

        Query queryRef = db.collection("Events")
                .whereEqualTo("project", id)
                .orderBy("date_added", Query.Direction.DESCENDING);
        gatherEvents(queryRef);

    }

    private void gatherEvents(Query query) {
        progbar.setVisibility(View.VISIBLE);
        recmain.setVisibility(View.GONE);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot.isEmpty()) {
                    Toast.makeText(context, "No Events Found", Toast.LENGTH_SHORT).show();
                    progbar.setVisibility(View.GONE);
                    recmain.setVisibility(View.VISIBLE);
                } else {
                    createRecyclerView(snapshot);
                }

            }
        });
    }

    private void createRecyclerView(QuerySnapshot documentSnap) {
        adapter.clear();
        for (DocumentSnapshot docx : documentSnap) {
            Events events = docx.toObject(Events.class);
            events.setId(docx.getId());
            eventlist.add(events);
        }
        progbar.setVisibility(View.GONE);
        recmain.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
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
