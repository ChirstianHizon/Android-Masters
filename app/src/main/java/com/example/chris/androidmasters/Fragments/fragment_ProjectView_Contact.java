package com.example.chris.androidmasters.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.androidmasters.Adapters.ProjectContactsAdapter;
import com.example.chris.androidmasters.Objects.Contacts;
import com.example.chris.androidmasters.ProjectView;
import com.example.chris.androidmasters.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class fragment_ProjectView_Contact extends android.support.v4.app.Fragment {

    private View view;
    private FirebaseFirestore db;
    private String id;
    private String TAG ="FRAG_PROJCONTACT";
    private List<Contacts> contactList;
    private ProjectContactsAdapter adapter;
    private RecyclerView recmain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        id = ProjectView.getProjectId();
        view = inflater.inflate(R.layout.fragment_projectview_contact, container, false);

        RecyclerView recmain = (RecyclerView) view.findViewById(R.id.rec_main);
        contactList = new ArrayList<Contacts>();
        adapter = new ProjectContactsAdapter(getContext(),contactList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recmain.setHasFixedSize(true);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
        recmain.setAdapter(adapter);

        getContacts();
        return view;
    }

    private void getContacts(){

        DocumentReference docRef = db.collection("Contacts").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {

                    Log.d(TAG,snapshot.getData().toString());
                    Log.d(TAG, String.valueOf(snapshot.getData().size()));

                    int size = snapshot.getData().size();

                    JSONObject obj = new JSONObject(snapshot.getData());

                    try {
                        JSONArray names = new JSONArray(obj.getString("Names"));
                        JSONArray contacts = new JSONArray(obj.getString("Contacts"));
                        JSONArray positions = new JSONArray(obj.getString("Positions"));

                        adapter.clear();
                        for(int x = 0;x<size;x++){
                            Log.d(TAG,names.getString(x));
                            contactList.add(
                                    new Contacts(
                                            names.getString(x),
                                            positions.getString(x),
                                            contacts.getString(x)
                                    )
                            );

                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                }

            }
        });
    }


}
