package com.example.chris.androidmasters.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chris.androidmasters.DonateActivity;
import com.example.chris.androidmasters.ProjectView;
import com.example.chris.androidmasters.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class fragment_ProjectView_Details extends android.support.v4.app.Fragment {

    private FirebaseFirestore db;
    private String id;
    private String TAG = "FRAG_PROJVIEW";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        id = ProjectView.getProjectId();

        getProjectDetails();

        View view = inflater.inflate(R.layout.fragment_projectview_details, container, false);

        Button btndonate = (Button)view.findViewById(R.id.btn_donate);
        btndonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), DonateActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });
        return view;
    }

    private void getProjectDetails(){
        DocumentReference docRef = db.collection("Details").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG,snapshot.getData().toString());
                }else{
                    Log.d(TAG, "Current data: null");
                }


            }
        });

    }

}
