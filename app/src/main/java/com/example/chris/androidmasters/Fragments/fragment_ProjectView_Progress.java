package com.example.chris.androidmasters.Fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chris.androidmasters.ProjectView;
import com.example.chris.androidmasters.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class fragment_ProjectView_Progress extends android.support.v4.app.Fragment {

    private FirebaseFirestore db;
    private String TAG = "FRAG_PROJETVIEW_PROG";
    private ProgressBar pbprogress;
    private TextView tvcurrent;
    private TextView tvgoal;
    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_projectview_progress, container, false);

        id = ProjectView.getProjectId();
        Log.d(TAG, id);

        pbprogress = (ProgressBar)view.findViewById(R.id.pb_progress);
        tvcurrent = (TextView)view.findViewById(R.id.tv_current);
        tvgoal = (TextView)view.findViewById(R.id.tv_goal);

        db = FirebaseFirestore.getInstance();

        getProjectDetails();

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

                    tvgoal.setText("P "+snapshot.getString("goal"));
                    tvcurrent.setText("P "+snapshot.getString("current"));

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
        CollectionReference transRef = db.collection("transaction_"+id);

    }


}
