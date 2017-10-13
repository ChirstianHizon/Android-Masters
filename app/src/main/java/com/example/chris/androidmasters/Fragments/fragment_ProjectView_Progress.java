package com.example.chris.androidmasters.Fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chris.androidmasters.Adapters.ProjectTransactionAdapter;
import com.example.chris.androidmasters.Objects.Transactions;
import com.example.chris.androidmasters.ProjectView;
import com.example.chris.androidmasters.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.chris.androidmasters.Objects.Constants.getCurrencySymbol;


public class fragment_ProjectView_Progress extends android.support.v4.app.Fragment {

    private FirebaseFirestore db;
    private String TAG = "FRAG_PROJETVIEW_PROG";
    private ProgressBar pbprogress;
    private TextView tvcurrent;
    private TextView tvgoal;
    private String id;
    private RecyclerView recmain;
    private ProjectTransactionAdapter adapter;
    private List<Transactions> transactionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_projectview_progress, container, false);

        id = ProjectView.getProjectId();
        Log.d(TAG, id);

        pbprogress = (ProgressBar)view.findViewById(R.id.pb_progress);
        recmain = (RecyclerView)view.findViewById(R.id.rec_main);
        tvcurrent = (TextView)view.findViewById(R.id.tv_current);
        tvgoal = (TextView)view.findViewById(R.id.tv_goal);

        recmain.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());

        transactionList = new ArrayList<Transactions>();
        adapter = new ProjectTransactionAdapter(getContext(),transactionList);

        recmain.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        getProjectDetails();
        getTransactions();

        return view;
    }

    private void getProjectDetails(){
        DocumentReference docRef = db.collection("Projects").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());

                    Double goal = Double.valueOf(snapshot.getString("goal"));
                    Double current = Double.valueOf(snapshot.getString("current"));

                    Double progress = Double.valueOf(snapshot.getString("current")) / Double.valueOf(snapshot.getString("goal")) * 100;

                    String currency = getCurrencySymbol("PHP");
                    tvgoal.setText(currency+" "+snapshot.getString("goal"));
                    tvcurrent.setText(currency+" "+snapshot.getString("current"));

                    if(current > goal){
                        pbprogress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    }else if(current.equals(goal)){
                        pbprogress.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                    }else{
                        pbprogress.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pbprogress.setProgress(progress.intValue(),true);
                    }else{
                        pbprogress.setProgress(progress.intValue());
                    }

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void getTransactions(){
        DocumentReference docRef = db.collection("Transactions").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, source + " data: " + snapshot.getData());

                    adapter.clear();


                    JSONObject snap = new JSONObject(snapshot.getData());
                    Iterator keys = snap.keys();
                    Log.d(TAG,"SNAP LENGTH: "+snap.length());

                    while(keys.hasNext()) {
                        String currentKey = (String)keys.next();
                        try {
                            JSONObject trans = new JSONObject(snap.getString(currentKey));
                            Transactions transactions = new Transactions(trans.getString("id"),trans.getString("date"),trans.getString("amount"));
                            transactionList.add(transactions);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                    } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
    }


}
