package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.chris.androidmasters.Adapters.ProjectCRUDImageAdapter;
import com.example.chris.androidmasters.R;

import java.util.ArrayList;

public class AddProjectImagesActivity extends AppCompatActivity {
    private Activity context = this;
    private ArrayList<String> imageList;
    private ProjectCRUDImageAdapter adapter;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_images);

        getSupportActionBar().setTitle("Add Contact Persons");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RecyclerView recmain = (RecyclerView)findViewById(R.id.rec_main);
        imageList = new ArrayList<String>();
        adapter = new ProjectCRUDImageAdapter(context, imageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recmain.setHasFixedSize(true);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
        recmain.setNestedScrollingEnabled(false);
        recmain.setAdapter(adapter);


        Button btnclear = (Button)findViewById(R.id.btn_clear);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                adapter.notifyDataSetChanged();

            }
        });

        Button btnadd = (Button)findViewById(R.id.btn_add);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);

            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            if (data != null){

                Uri imageUri = data.getData();
                final ImageView ivdisplay = (ImageView)findViewById(R.id.iv_display);

                if(imageUri != null){
                    imageList.add(imageUri.toString());
                }
            }

        }
    }
}
