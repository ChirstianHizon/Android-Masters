package com.projectboost.chris.androidmasters.CRUD;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.projectboost.chris.androidmasters.Adapters.CRUDProjectListAdapter;
import com.projectboost.chris.androidmasters.Objects.Project;
import com.projectboost.chris.androidmasters.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class  CRUDProjectListActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ArrayList<Project> projectlist;
    private CRUDProjectListAdapter adapter;
    private Activity context = this;
    private String TAG = "CRUD_ProjectList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_project_list);

        db = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("Projects");
        //        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RecyclerView recmain = (RecyclerView) findViewById(R.id.rec_main);
        projectlist = new ArrayList<Project>();
        adapter = new CRUDProjectListAdapter(context, projectlist);

        recmain.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
        recmain.setAdapter(adapter);

        Query queryRef = db.collection("Projects").orderBy("added_date", Query.Direction.DESCENDING);
        getInitialProjects(queryRef);
    }

    private void getInitialProjects(Query query){
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }


                if (snapshot.isEmpty()) {
                    Toast.makeText(context, "No Project Found", Toast.LENGTH_SHORT).show();
                } else {
                    createRecyclerView(snapshot);
                }
            }
        });
    }
    private void createRecyclerView(QuerySnapshot documentSnap) {
        adapter.clear();
        for (DocumentSnapshot docx : documentSnap) {
            Project project = docx.toObject(Project.class);
            project.setId(docx.getId());
            projectlist.add(project);
        }
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
