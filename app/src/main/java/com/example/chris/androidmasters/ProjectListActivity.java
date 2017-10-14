package com.example.chris.androidmasters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.chris.androidmasters.Adapters.ProjectListAdapter;
import com.example.chris.androidmasters.Objects.Project;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectListActivity extends AppCompatActivity {

    private Activity context = this;
    private RecyclerView recmain;
    private List<Project> projectlist;
    private ProjectListAdapter adapter;
    private RecyclerView recyclerView;
    private String TAG = "PROJECT_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        recmain = (RecyclerView)findViewById(R.id.rec_main);

        projectlist = new ArrayList<Project>();
        adapter = new ProjectListAdapter(context,projectlist);

        recmain.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
//        recmain.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recmain.setAdapter(adapter);


        CollectionReference colRef = db.collection("Projects");
        colRef
//                .whereEqualTo("state", "CA")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        adapter.clear();
                        for (DocumentSnapshot doc : value) {

                            Log.d("DOCUMENT", String.valueOf(doc.getData()));
//                          Project(String name,String desc,String date,String organization,String image,String logo)
                            if(doc.getString("name") != null && doc.getString("description") != null
                                    && doc.getString("date") != null && doc.getString("organization") != null
                                    && doc.getString("image") != null && doc.getString("logo") != null
                                    && doc.getString("goal") != null && doc.getString("current") != null
                                    && doc.getDate("completion_date") != null){

                                if(!doc.getString("name").equalsIgnoreCase("") && !doc.getString("description").equalsIgnoreCase("")
                                        && !doc.getString("date").equalsIgnoreCase("") && !doc.getString("organization").equalsIgnoreCase("")
                                        && !doc.getString("image").equalsIgnoreCase("") && !doc.getString("logo").equalsIgnoreCase("")
                                        && !doc.getString("goal").equalsIgnoreCase("") && !doc.getString("current").equalsIgnoreCase("")){

                                    Date date = doc.getDate("completion_date");
                                    Log.d("DATE_COMPLETION",date.toString());

                                    Project project = doc.toObject(Project.class);
                                    project.setId(doc.getId());
                                    projectlist.add(project);
                                    adapter.notifyDataSetChanged();


                                }

                            }


                        }
                    }
                });





    }


}
