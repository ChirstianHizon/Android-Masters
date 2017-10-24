package com.example.chris.androidmasters.CRUD.Create;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.androidmasters.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddProjectReviewActivity extends AppCompatActivity {
    private Activity context = this;
    private FirebaseFirestore db;
    private Date fbdate;
    private String TAG = "PROJECT_CRUD",name,desc,org,date,goal,image;
    private ProgressDialog progress;
    private FirebaseStorage sb;
    private StorageReference storageRef;
    private ArrayList<String> imageUrlList,imageArray,position,contacts,person,objectives;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_review);

//        ------------- Firebase Stuff -----------------

        db = FirebaseFirestore.getInstance();
        sb = FirebaseStorage.getInstance();

        storageRef = sb.getReference();


//        -----------  add back arrow to toolbar ------------
        getSupportActionBar().setTitle("Add Images");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        org =  intent.getStringExtra("org");
        date = intent.getStringExtra("date");
        goal = intent.getStringExtra("goal");
        image =  intent.getStringExtra("image");
        objectives = intent.getStringArrayListExtra("objectives");
        person = intent.getStringArrayListExtra("person");
        contacts = intent.getStringArrayListExtra("contact");
        position = intent.getStringArrayListExtra("position");
        imageArray = intent.getStringArrayListExtra("imagesArray");


        TextView tvname = (TextView)findViewById(R.id.tv_name);
        TextView tvdesc = (TextView)findViewById(R.id.tv_desc);
        TextView tvgoal = (TextView)findViewById(R.id.tv_goal);
        TextView tvdate = (TextView)findViewById(R.id.tv_date);


        ImageView ivimage = (ImageView)findViewById(R.id.iv_image);

        Picasso.with(context)
                .load(Uri.parse(image))
                .placeholder(R.color.white)
                .error(R.color.colorAccent)
                .into(ivimage);

        fbdate = new Date();
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            fbdate = sdf.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        imageUrlList = new ArrayList<>();

        Button btnsubmit = (Button)findViewById(R.id.btn_submit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress=new ProgressDialog(context);
                progress.setMessage("Uploading data to Server... ");
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.setProgress(0);
                progress.show();



//      ---------------- Projects ----------------------
              final Map<String, Object> project = new HashMap<>();
                project.put("added_date", new Date());
                project.put("completion_date", fbdate);
                project.put("current", "0");
                project.put("goal", goal);
                project.put("search", name.toLowerCase());
                project.put("name", name);
                project.put("organization", org);
                project.put("visible",false);


               db.collection("Projects")
                .add(project)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        progress.dismiss();
                        uploadDisplayImage(image,documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(context, "Unable to Upload to Server", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
            }
        });


    }

    private void uploadDisplayImage(String image, final String id){
        progress=new ProgressDialog(context);
        progress.setMessage("Uploading Display Image...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setProgress(0);
        progress.show();

        Uri file = Uri.parse(image);
        StorageReference imageRef = storageRef.child(id+"/display_image/"+id+file.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Failed to Upload Display Image", Toast.LENGTH_SHORT).show();
                db.collection("Projects").document(id).delete();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                Map<String, Object> project = new HashMap<>();
                project.put("image", downloadUrl.toString());
                db.collection("Projects").document(id).set(project ,SetOptions.merge());

                progress.dismiss();
                uploadProjectDetails(id,downloadUrl);


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Double uploadprog = (100.0 * taskSnapshot.getBytesTransferred());
                progress.setProgress(uploadprog.intValue());
            }
        });
    }

    private void uploadImageArray(final String id, final ArrayList<String> imageList){

        if(!imageList.isEmpty()){
            progress=new ProgressDialog(context);
            progress.setMessage("Uploading " + imageList.size() +" image(s)... ");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.setProgress(0);
            progress.show();

            for (int x=0;x<imageList.size();x++) {
                Uri file = Uri.parse(imageList.get(x));
                StorageReference imageRef = storageRef.child(id+"/images/"+id+file.getLastPathSegment());
                UploadTask uploadTask = imageRef.putFile(file);

                final int finalX = x;
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(context, "Failed to Upload Display Image", Toast.LENGTH_SHORT).show();
                        imageUrlList.clear();
                        db.collection("Details").document(id).delete();
                        progress.dismiss();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        imageUrlList.add(downloadUrl);

                        final Map<String, Object> project = new HashMap<>();
                        project.put("images",imageUrlList);
                        db.collection("Details").document(id).set(project ,SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Map<String, Object> project = new HashMap<>();
                                        project.put("visible", true);
                                        db.collection("Projects").document(id).set(project ,SetOptions.merge());
                                        if(finalX == (imageList.size()-1)){
                                            Toast.makeText(context, "Upload Complete", Toast.LENGTH_SHORT).show();
                                            progress.dismiss();

                                            AddProjectContactsActivity.act.finish();
                                            AddProjectDetailsActivity.act.finish();
                                            AddProjectImagesActivity.act.finish();
                                            AddProjectObjectivesActivity.act.finish();
                                            finish();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Upload Error", Toast.LENGTH_SHORT).show();
                                db.collection("Details").document(id).delete();
                                db.collection("Contacts").document(id).delete();
                                progress.dismiss();
                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        Double uploadprog = (100.0 * taskSnapshot.getBytesTransferred());
                        progress.setProgress(uploadprog.intValue());
                    }
                });
            }

        }
    }

    private void uploadProjectDetails(final String id,Uri image){

        progress=new ProgressDialog(context);
        progress.setMessage("Uploading Details to Server... ");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setProgress(0);
        progress.show();

//      ---------------- Details ----------------------
        Map<String, Object> details = new HashMap<>();
        details.put("display_image",image.toString());
        details.put("objectives",objectives);
        details.put("organization",org);
        details.put("short_description",desc);
        details.put("title",name);

//      ---------------- Contacts ----------------------
        final Map<String, Object> contact = new HashMap<>();
        contact.put("Contacts",contacts);
        contact.put("Names",person);
        contact.put("Positions",position);


        db.collection("Details").document(id)
            .set(details)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");

                    db.collection("Contacts").document(id)
                            .set(contact)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    progress.dismiss();
                                    uploadImageArray( id, imageArray);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    progress.dismiss();
                                }
                            });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });




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
