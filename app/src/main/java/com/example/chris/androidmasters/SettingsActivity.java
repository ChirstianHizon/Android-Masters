package com.example.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.chris.androidmasters.CRUD.Create.AddProjectDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SettingsActivity extends AppCompatActivity {

    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();

        if(User == null){
            finish();
        }else {
            View aboutPage = new AboutPage(this)
                    .setDescription("User Account")
                    .isRTL(false)
                    .setImage(R.drawable.images)
                    .addGroup("User Key")
                    .addItem(new Element().setTitle(User.getUid()))
                    .addItem(getAdmin())
                    .create();
            setContentView(aboutPage);
        }
        getSupportActionBar().setTitle("Settings");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    Element getAdmin() {
        Element copyRightsElement = new Element();
        copyRightsElement.setTitle("Login As Administrator");
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddProjectDetailsActivity.class);
                startActivity(intent);
            }
        });
        return copyRightsElement;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
