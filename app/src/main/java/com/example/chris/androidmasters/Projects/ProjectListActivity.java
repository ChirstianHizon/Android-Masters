package com.example.chris.androidmasters.Projects;

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
import com.example.chris.androidmasters.Options.AboutActivity;
import com.example.chris.androidmasters.Options.Settings;
import com.example.chris.androidmasters.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private ProjectListAdapter adapter;
    private String TAG = "PROJECT_LIST";
    private FirebaseFirestore db;
    private LinearLayoutManager layoutManager;
    private ListenerRegistration listener;
    private SearchView searchView;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        db = FirebaseFirestore.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();

        if(User == null){
            Toast.makeText(context, "Unable to Authenticate User", Toast.LENGTH_LONG).show();
            finish();
        }

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
        for (DocumentSnapshot docx : documentSnap) {
            Project project = docx.toObject(Project.class);
            project.setId(docx.getId());
            projectlist.add(project);
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
                Query queryRef = db.collection("Projects")
                        .whereEqualTo("search",query);
                getInitialProjects(queryRef);

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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_aboutus:
                intent = new Intent(ProjectListActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_settings:
                intent = new Intent(ProjectListActivity.this, Settings.class);
                startActivity(intent);
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
