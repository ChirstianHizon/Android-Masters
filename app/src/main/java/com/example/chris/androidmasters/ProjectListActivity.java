package com.example.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
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
        getInitialProjects(queryRef);

//        ------------------------------------------------------------------------------------- //

        pullToRefresh();

    }

    private void getInitialProjects(Query queryRef){

        queryRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {


                        DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                                .get(documentSnapshots.size() -1);

                        createRecyclerView(documentSnapshots);

                        if(documentSnapshots.size() < page ){
                            isloading = false;
                        }else{
                            // This Function gets the very first document but does not display it
                            lastDocument = lastVisible;
                            Log.d("isLoading_LastDocument", lastDocument.getId());
                            addScrollListener(lastDocument);

                        }
                    }
                });

    }

    private void getProjects(Query queryRef){

        queryRef.get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                if(documentSnapshots.isEmpty()){

                    return;
                }
                DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                        .get(documentSnapshots.size() -1);

                Log.d("isLoading_last", lastDocument.getId());
                Log.d("isLoading_visible", lastVisible.getId());
                isloading = false;

                if(!lastDocument.getId().equals(lastVisible.getId()) ){
                    createRecyclerView(documentSnapshots);
                    lastDocument = lastVisible;
                    isloading = true;
                }
            }
        });

    }

    private void createRecyclerView( QuerySnapshot documentSnap){
        int counter = 0;
        for (DocumentSnapshot docx : documentSnap) {
            if(docx.getString("name") != null && docx.getString("organization") != null
                    && docx.getString("image") != null && docx.getString("logo") != null
                    && docx.getString("goal") != null && docx.getString("current") != null
                    && docx.getDate("completion_date") != null){

                if(!docx.getString("name").equalsIgnoreCase("") && !docx.getString("organization").equalsIgnoreCase("")
                        && !docx.getString("image").equalsIgnoreCase("") && !docx.getString("logo").equalsIgnoreCase("")
                        && !docx.getString("goal").equalsIgnoreCase("") && !docx.getString("current").equalsIgnoreCase("")){

                    Date date = docx.getDate("completion_date");

                    Project project = docx.toObject(Project.class);
                    project.setId(docx.getId());
                    projectlist.add(project);
                }
            }
            counter++;
        }
        adapter.notifyDataSetChanged();

//        Log.d("isLoading_counter", String.valueOf(counter));
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
                            Log.d("RECSCORLL","Last Item");

                            Query queryRef = db.collection("Projects").startAfter(lastVisible).limit(page);
                            getProjects(queryRef);
                            isloading = false;

                        }
                    }
                }
            }
        });
    }

    private void pullToRefresh(){
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();

                Query queryRef = db.collection("Projects").limit(page);
                getInitialProjects(queryRef);
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
                Intent intent = new Intent(ProjectListActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_settings:
                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
