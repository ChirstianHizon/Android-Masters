package com.projectboost.chris.androidmasters.Introduction;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projectboost.chris.androidmasters.R;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

/**
 * Created by Lenovo on 19/10/2017.
 */

public class SecondSlide extends Fragment implements ISlideBackgroundColorHolder {
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.intro_slide2, container, false);

//        IntroActivity activity =(IntroActivity)getActivity();
//        activity.setBarColor(Color.parseColor("#2494a2"));
//        activity.setSeparatorColor(Color.parseColor("#2494a2"));

        return v;
    }

    @Override
    public int getDefaultBackgroundColor() {
        return Color.parseColor("#2494a2");
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {

        if (v != null) {
            v.setBackgroundColor(backgroundColor);
        }
    }
}
