package com.example.chris.androidmasters;

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
import android.widget.Button;
import android.widget.ImageView;

import com.example.chris.androidmasters.Adapters.TabsPageAdapter;
import com.example.chris.androidmasters.Fragments.fragment_ProjectView_Contact;
import com.example.chris.androidmasters.Fragments.fragment_ProjectView_Details;
import com.example.chris.androidmasters.Fragments.fragment_ProjectView_Progress;
import com.squareup.picasso.Picasso;

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

        toolbar.setVisibility(View.GONE);

        Button btndonate = (Button)findViewById(R.id.btn_donate);
        btndonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProjectView.this, DonateActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

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

    public void changeDisplayImage(String image){
        ImageView display = (ImageView)findViewById(R.id.app_bar_image);

        if(image != null && !image.equals("")){
            Picasso.with(this)
                    .load(image)
                    .resize(800,800)
                    .error(R.mipmap.ic_launcher)
                    .into(display);
        }
    }

}
