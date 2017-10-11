package com.example.chris.mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.chris.mainactivity.Adapters.TabsPageAdapter;
import com.example.chris.mainactivity.Fragments.fragment_ProjectView_Contact;
import com.example.chris.mainactivity.Fragments.fragment_ProjectView_Details;
import com.example.chris.mainactivity.Fragments.fragment_ProjectView_Progress;

public class ProjectView extends AppCompatActivity {

    private static String id;
    private Activity context = this;
    private TabsPageAdapter mTabsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

//        ----------- CREATE A BACK BUTTON ------------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


//        ----------- GET ID FROM PROJECT LIST --------------

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
//        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();


//        ------------ INSTANTIATE TABS ADAPTER AND PAGER ------

        mTabsPageAdapter = new TabsPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setupViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        TabsPageAdapter adapter = new TabsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_ProjectView_Progress(), "Progress");
        adapter.addFragment(new fragment_ProjectView_Details(), "Details");
        adapter.addFragment(new fragment_ProjectView_Contact(), "Contacts");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getProjectId(){
        return id;
    }

}
