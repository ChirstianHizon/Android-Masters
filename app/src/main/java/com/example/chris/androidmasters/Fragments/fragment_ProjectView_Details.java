package com.example.chris.androidmasters.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.chris.androidmasters.DonateActivity;
import com.example.chris.androidmasters.ProjectView;
import com.example.chris.androidmasters.R;


public class fragment_ProjectView_Details extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_projectview_details, container, false);

        Button btndonate = (Button)view.findViewById(R.id.btn_donate);
        btndonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String id = ProjectView.getProjectId();

                Intent intent = new Intent(getContext(), DonateActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        return view;
    }

}
