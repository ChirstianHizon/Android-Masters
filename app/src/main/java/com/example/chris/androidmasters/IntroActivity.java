package com.example.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.chris.androidmasters.Introduction.FirstSlide;
import com.example.chris.androidmasters.Introduction.FourthSlide;
import com.example.chris.androidmasters.Introduction.SecondSlide;
import com.example.chris.androidmasters.Introduction.ThirdSlide;
import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by chris on 19/10/2017.
 */

public class IntroActivity extends AppIntro {

    private Activity context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new FirstSlide());
        addSlide(new SecondSlide());
        addSlide(new ThirdSlide());
        addSlide(new FourthSlide());

//        setBarColor(Color.TRANSPARENT);
//        setSeparatorColor(Color.TRANSPARENT);


//        setCustomTransformer(new ZoomOutPageTransformer());

        showSkipButton(true);

        //Choose your animation
//        setFadeAnimation();
//        setZoomAnimation();
        setFlowAnimation();
//        setSlideOverAnimation();
//        setDepthAnimation();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(context,ProjectListActivity.class));
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        startActivity(new Intent(context,ProjectListActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        Log.d("INTRO", String.valueOf(oldFragment));
        Log.d("INTRO", String.valueOf(newFragment.getTag()));

        switch (newFragment.getTag()){
            case "android:switcher:2131558592:0":
                break;
            case "android:switcher:2131558592:1":
                break;
            case "android:switcher:2131558592:2":
                break;
            case "android:switcher:2131558592:3":
                break;

        }
    }
}
