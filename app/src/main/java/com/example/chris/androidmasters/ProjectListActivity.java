package com.example.chris.androidmasters;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import java.util.List;

public class ProjectListActivity extends AppCompatActivity{

    private Activity context = this;
    private RecyclerView recmain;
    private List<Project> projectlist;
    private List<String> projectnames;
    private ProjectListAdapter adapter;
    private String TAG = "PROJECT_LIST";
    private FirebaseFirestore db;
    private LinearLayoutManager layoutManager;
    private ListenerRegistration listener;
    private SearchView searchView;

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

        Query queryRef = db.collection("Projects").orderBy("added_date" , Query.Direction.DESCENDING);
        getInitialProjects(queryRef);

//        ------------------------------------------------------------------------------------- //

        pullToRefresh();

    }

    private void getInitialProjects(Query queryRef){
        listener = queryRef.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }



                if(snapshot.isEmpty()){
                    Toast.makeText(context, "No Project Found", Toast.LENGTH_SHORT).show();
                }else{
                    createRecyclerView(snapshot);
                }


            }
        });

    }

    private void createRecyclerView( QuerySnapshot documentSnap){
        adapter.clear();
        projectnames = new ArrayList<>();
        for (DocumentSnapshot docx : documentSnap) {
            if(docx.getString("name") != null && docx.getString("organization") != null
                    && docx.getString("image") != null && docx.getString("logo") != null
                    && docx.getString("goal") != null && docx.getString("current") != null
                    && docx.getDate("completion_date") != null){

                if(!docx.getString("name").equalsIgnoreCase("") && !docx.getString("organization").equalsIgnoreCase("")
                        && !docx.getString("image").equalsIgnoreCase("") && !docx.getString("logo").equalsIgnoreCase("")
                        && !docx.getString("goal").equalsIgnoreCase("") && !docx.getString("current").equalsIgnoreCase("")){

                    Project project = docx.toObject(Project.class);
                    project.setId(docx.getId());
                    projectlist.add(project);
                    projectnames.add(docx.getString("name").toLowerCase());
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void pullToRefresh(){
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();

                if(listener != null){
                    listener.remove();
                }
                Query queryRef = db.collection("Projects").orderBy("added_date" , Query.Direction.DESCENDING);
                getInitialProjects(queryRef);

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) context.getSystemService(SEARCH_SERVICE);

        searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(context.getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                boolean isThere = projectnames.contains(query.toLowerCase());
                if(isThere){
                    for(int x=0;x<projectnames.size();x++){
                        if(projectnames.get(x).equals(query.toLowerCase())){
                            query = projectnames.get(x);

                            Query queryRef = db.collection("Projects")
                                    .whereEqualTo("name",query);
                            getInitialProjects(queryRef);
                        }
                    }
                }else{
                    Toast.makeText(context, "No Project Found", Toast.LENGTH_SHORT).show();
                }

                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

}
