package com.example.chris.androidmasters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private DocumentSnapshot lastDocument;
    private int page = 2;
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

    private void getProjects(Query queryRef){

        Log.d("isLoading_Stat", String.valueOf(isloading));
        queryRef.get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                // Get the last visible document
//                Log.d("isLoading_Size", String.valueOf(documentSnapshots.size()));
                if(documentSnapshots.size() == 0){
                    isloading = false;
                }else{
                    DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                            .get(documentSnapshots.size() -1);

                    addScrollListener(lastVisible);
                    pullToRefresh(lastVisible);

                    createRecyclerView(documentSnapshots,lastVisible);

                }

            }
        });

    }

    private void createRecyclerView( QuerySnapshot documentSnapshots,DocumentSnapshot lastVisible){

        int counter = 0;
        String lastid = "";
        for (DocumentSnapshot doc : documentSnapshots) {

            lastid = doc.getId();

            if(doc.getString("name") != null && doc.getString("organization") != null
                    && doc.getString("image") != null && doc.getString("logo") != null
                    && doc.getString("goal") != null && doc.getString("current") != null
                    && doc.getDate("completion_date") != null){

                if(!doc.getString("name").equalsIgnoreCase("") && !doc.getString("organization").equalsIgnoreCase("")
                        && !doc.getString("image").equalsIgnoreCase("") && !doc.getString("logo").equalsIgnoreCase("")
                        && !doc.getString("goal").equalsIgnoreCase("") && !doc.getString("current").equalsIgnoreCase("")){

                    Date date = doc.getDate("completion_date");

                    Project project = doc.toObject(Project.class);
                    project.setId(doc.getId());
                    projectlist.add(project);
                }
            }
            counter++;
        }
        adapter.notifyDataSetChanged();

        Log.d("isLoading_lastid", lastid);
        Log.d("isLoading_lastvisible", lastVisible.getId());

        if(lastid == lastVisible.getId()){
            isloading = false;
        }

        if(counter < page){
            isloading = false;
        }

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

                            Toast.makeText(context, "Last Item", Toast.LENGTH_SHORT).show();
                            Log.d("RECSCORLL","Last Item");

                            Query queryRef = db.collection("Projects").startAfter(lastVisible).limit(page);
                            getProjects(queryRef);
                        }
                    }
                }
            }
        });
    }

    private void pullToRefresh(final DocumentSnapshot lastVisible){
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();

                Query queryRef = db.collection("Projects").startAfter(lastVisible).limit(page);
                getProjects(queryRef);
                isloading = true;
                mSwipeRefreshLayout.setRefreshing(false);
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
