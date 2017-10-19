package com.example.chris.androidmasters.Introduction;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chris.androidmasters.R;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

/**
 * Created by chris on 19/10/2017.
 */

public class FirstSlide extends Fragment implements ISlideBackgroundColorHolder {

    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.intro_slide1, container, false);

//        IntroActivity activity =(IntroActivity)getActivity();
//        activity.setBarColor(Color.parseColor("#FFFFFF"));
//        activity.setSeparatorColor(Color.parseColor("#FFFFFF"));
        return v;
    }

    @Override
    public int getDefaultBackgroundColor() {
        return Color.parseColor("#FFFFFF");
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {

        if (v != null) {
            v.setBackgroundColor(backgroundColor);
        }
    }
}