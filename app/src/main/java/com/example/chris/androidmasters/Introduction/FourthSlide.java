package com.example.chris.androidmasters.Introduction;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.androidmasters.IntroActivity;
import com.example.chris.androidmasters.R;

/**
 * Created by Lenovo on 19/10/2017.
 */

public class FourthSlide extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_slide2, container, false);

        IntroActivity activity =(IntroActivity)getActivity();

        activity.setBarColor(Color.parseColor("#2d3258"));
        activity.setSeparatorColor(Color.parseColor("#2d3258"));

        return v;
    }
}
