package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chris.androidmasters.Adapters.ProjectCRUDImageAdapter;
import com.example.chris.androidmasters.R;

import java.util.ArrayList;

public class AddProjectImagesActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    public static Activity act;
    private Activity context = this;
    private ArrayList<Uri> imageList;
    private ArrayList<String> stringimageList;
    private ProjectCRUDImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_images);

        act = this;

        getSupportActionBar().setTitle("Add Images");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RecyclerView recmain = (RecyclerView) findViewById(R.id.rec_main);
        imageList = new ArrayList<Uri>();
        adapter = new ProjectCRUDImageAdapter(context, imageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recmain.setHasFixedSize(true);
        recmain.setLayoutManager(layoutManager);
        recmain.setItemAnimator(new DefaultItemAnimator());
        recmain.setNestedScrollingEnabled(false);
        recmain.setAdapter(adapter);

        stringimageList = new ArrayList<>();

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");
        final String desc = intent.getStringExtra("desc");
        final String org = intent.getStringExtra("org");
        final String date = intent.getStringExtra("date");
        final String goal = intent.getStringExtra("goal");
        final String image = intent.getStringExtra("image");
        final ArrayList objectives = intent.getStringArrayListExtra("objectives");
        final ArrayList person = intent.getStringArrayListExtra("person");
        final ArrayList contacts = intent.getStringArrayListExtra("contact");
        final ArrayList position = intent.getStringArrayListExtra("position");

        Button btnclear = (Button) findViewById(R.id.btn_clear);
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                adapter.notifyDataSetChanged();

                stringimageList = new ArrayList<>();

            }
        });

        Button btnadd = (Button) findViewById(R.id.btn_add);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);

            }
        });

        Button btnnext = (Button) findViewById(R.id.btn_next);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (stringimageList.size() > 0) {
                    Intent intent = new Intent(context, AddProjectReviewActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("desc", desc);
                    intent.putExtra("org", org);
                    intent.putExtra("date", date);
                    intent.putExtra("goal", goal);
                    intent.putExtra("image", image);
                    intent.putStringArrayListExtra("objectives", objectives);
                    intent.putStringArrayListExtra("person", person);
                    intent.putStringArrayListExtra("position", position);
                    intent.putStringArrayListExtra("contact", contacts);
                    intent.putStringArrayListExtra("imagesArray", stringimageList);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Select atleast 1 image", Toast.LENGTH_SHORT).show();
                }


//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//                Map<String, Object> devices = new HashMap<>();
//                devices.put("objectives", objectives);
//
//                db.collection("Sample").add(devices);

            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            if (data != null) {

                Uri imageUri = data.getData();
                if (imageUri != null) {
                    imageList.add(imageUri);
                    adapter.notifyDataSetChanged();

                    stringimageList.add(imageUri.toString());
                }
            }

        }
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
