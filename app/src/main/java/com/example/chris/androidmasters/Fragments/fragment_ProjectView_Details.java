package com.example.chris.androidmasters.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chris.androidmasters.DonateActivity;
import com.example.chris.androidmasters.Objects.Details;
import com.example.chris.androidmasters.ProjectView;
import com.example.chris.androidmasters.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class fragment_ProjectView_Details extends android.support.v4.app.Fragment {

    private FirebaseFirestore db;
    private String id;
    private String TAG = "FRAG_PROJVIEW";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        id = ProjectView.getProjectId();

        getProjectDetails();


        view = inflater.inflate(R.layout.fragment_projectview_details, container, false);


        return view;
    }

    private void getProjectDetails(){
        Log.d(TAG,"ID: "+id);
        DocumentReference docRef = db.collection("Details").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {

                    // GETS AND TRANSFER DATA TO CLASS
                    //TODO: CREATE VALIDATION TO CHECK IF FIELDS ARE PRESENT
                    Details details = snapshot.toObject(Details.class);
                    setDetails(details);

                }else{
                    Log.d(TAG, "Current data: null");
                }


            }
        });

    }

    private void setDetails(Details details){

        TextView Title = (TextView)view.findViewById(R.id.tv_project_title);
        TextView Organization = (TextView)view.findViewById(R.id.tv_organization);
        TextView shortdesc = (TextView)view.findViewById(R.id.tv_project_description);

        LinearLayout llimagedisplay = (LinearLayout)view.findViewById(R.id.ll_image_display);

        Title.setText(details.getTitle());
        Organization.setText("by " + details.getOrganization());
        shortdesc.setText(details.getShort_description());

        llimagedisplay.removeAllViews();

        ProjectView projectview = (ProjectView)getActivity();
        projectview.changeDisplayImage(details.getDisplay_image());

        int size = details.getImagesSize();
        for(int x = 0;x < size;x++){

            if(details.getSelectedImages(x) != null && !details.getSelectedImages(x).equals("")){
                ImageView myImage = new ImageView(getContext());
                myImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                myImage.setImageResource(R.mipmap.ic_launcher);
                llimagedisplay.addView(myImage);

                Picasso.with(getContext())
                        .load(details.getSelectedImages(x))
                        .resize(0,500)
                        .error(R.mipmap.ic_launcher)
                        .into(myImage);
            }
        }

    }

}
