package com.example.chris.androidmasters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chris.androidmasters.Adapters.ProjectListAdapter;
import com.example.chris.androidmasters.Objects.Project;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
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
    private int visibleItemCount,notvisibleItemCount,totalItemCount;
    private boolean isloading = true;
    private FirebaseFirestore db;
    private String lastKey = "";
    private int page = 1;
    private LinearLayoutManager layoutManager;
    private ListenerRegistration firestoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        db = FirebaseFirestore.getInstance();

        recmain = (RecyclerView)findViewById(R.id.rec_main);

        projectlist = new ArrayList<Project>();
        adapter = new ProjectListAdapter(context,projectlist);

        recmain.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
        recmain.setAdapter(adapter);

//        ------------------------------------------------------------------------------------- //

        Query queryRef = db.collection("Projects").limit(page);
        getProjects(queryRef);

//        ------------------------------------------------------------------------------------- //


    }

    private void addScrollListener(final DocumentSnapshot lastVisible){
        recmain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0){

                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    notvisibleItemCount = layoutManager.findFirstVisibleItemPosition();

                    if(isloading){

                        if((visibleItemCount + notvisibleItemCount) >= totalItemCount){

                            isloading = false;
                            Toast.makeText(context, "Last Item", Toast.LENGTH_SHORT).show();
                            Log.d("RECSCORLL","Last Item");

                            firestoreListener.remove();
                            Query queryRef = db.collection("Projects").limit(page*2);
                            getProjects(queryRef);
                        }

                    }

                }

                Log.d("RECSCORLL","DX:"+dx+" | "+"DY:"+dy);

            }
        });
    }

    private void getProjects(Query queryRef){
        firestoreListener = queryRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
    @Override
    public void onEvent(@Nullable QuerySnapshot value,
                        @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "Listen failed.", e);
            return;
        }

        Toast.makeText(context, "Projects Updated", Toast.LENGTH_SHORT).show();

        DocumentSnapshot lastVisible = value.getDocuments()
                .get(value.size() - 1);

        Log.d("LAST KEY", String.valueOf(lastVisible));

        adapter.clear();
        for (DocumentSnapshot doc : value) {

            lastKey = doc.getId();
            Log.d("DOCUMENT", String.valueOf(doc.getData()));
            //              Project(String name,String desc,String date,String organization,String image,String logo)
            if(doc.getString("name") != null && doc.getString("organization") != null
                    && doc.getString("image") != null && doc.getString("logo") != null
                    && doc.getString("goal") != null && doc.getString("current") != null
                    && doc.getDate("completion_date") != null){

                if(!doc.getString("name").equalsIgnoreCase("") && !doc.getString("organization").equalsIgnoreCase("")
                        && !doc.getString("image").equalsIgnoreCase("") && !doc.getString("logo").equalsIgnoreCase("")
                        && !doc.getString("goal").equalsIgnoreCase("") && !doc.getString("current").equalsIgnoreCase("")){

                    Date date = doc.getDate("completion_date");
                    Log.d("DATE_COMPLETION",date.toString());

                    Project project = doc.toObject(Project.class);
                    project.setId(doc.getId());
                    projectlist.add(project);
                    adapter.notifyDataSetChanged();

                    addScrollListener(lastVisible);


                }

            }

        }
    }
});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_aboutus:
                Toast.makeText(context, "About Us", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_settings:
                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
