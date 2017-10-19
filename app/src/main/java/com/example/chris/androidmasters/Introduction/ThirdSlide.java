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

public class ThirdSlide extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_slide3, container, false);

        IntroActivity activity =(IntroActivity)getActivity();

        activity.setBarColor(Color.parseColor("#304b78"));
        activity.setSeparatorColor(Color.parseColor("#304b78"));

        return v;
    }
}
