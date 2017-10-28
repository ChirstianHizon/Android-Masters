package com.projectboost.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.projectboost.chris.androidmasters.Introduction.FirstSlide;
import com.projectboost.chris.androidmasters.Introduction.FourthSlide;
import com.projectboost.chris.androidmasters.Introduction.SecondSlide;
import com.projectboost.chris.androidmasters.Introduction.ThirdSlide;
import com.projectboost.chris.androidmasters.Projects.ProjectListActivity;
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
        setColorTransitionsEnabled(true);
        showSkipButton(true);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(context, ProjectListActivity.class));
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        startActivity(new Intent(context, ProjectListActivity.class));
        finish();
    }
}
