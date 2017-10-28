package com.example.chris.androidmasters.Events;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chris.androidmasters.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VolunteerActivity extends AppCompatActivity {

    private FirebaseUser User;
    private Activity context = this;
    private String id;
    private String TAG = "VOLUNTEER_ACT";
    private boolean registered;
    private ProgressDialog progress;
    private FirebaseFirestore db;
    private EditText name;
    private EditText age;
    private EditText contact;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        final Intent intent = getIntent();
        id = intent.getStringExtra("id");

        getSupportActionBar().setTitle("Volunteer");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Button btnvolunteer = (Button)findViewById(R.id.btn_volunteer);
        name = (EditText)findViewById(R.id.edt_name);
        age = (EditText)findViewById(R.id.edt_age);
        contact = (EditText)findViewById(R.id.edt_contact);
        email = (EditText)findViewById(R.id.edt_email);


        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();

        if (User == null) {
            Toast.makeText(context, "Login to volunteer", Toast.LENGTH_SHORT).show();
        }else if(!User.isAnonymous()){
            email.setText(User.getEmail());
            name.setText(User.getDisplayName());
            contact.setText(User.getPhoneNumber());
        }else{
            Toast.makeText(context, "Login to Volunteer", Toast.LENGTH_SHORT).show();
        }


        btnvolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = new ProgressDialog(context);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setCancelable(false);
                progress.setIndeterminate(true);
                progress.setTitle("Checking Servers...");
                progress.setMessage("Checking if registered...");
                progress.show();

                registered = true;

                db.collection("Volunteers")
                        .whereEqualTo("email",email.getText().toString())
                        .whereEqualTo("project",id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            registered = false;
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        if(registered){
                            inserttoFB();
                        }else{
                            Toast.makeText(context, "Account already registered", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }
                });
            }
        });

    }

    private void inserttoFB(){

        Map<String, Object> volunteer = new HashMap<>();
        volunteer.put("date", new Date());
        volunteer.put("email", email.getText().toString());
        volunteer.put("name", name.getText().toString());
        volunteer.put("age", age.getText().toString());
        volunteer.put("contact", contact.getText().toString());
        volunteer.put("project", id);

        db.collection("Volunteers")
        .add(volunteer)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progress.setMessage("Registration Complete...");
                Toast.makeText(context, "You Have successfully Volunteered", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                progress.dismiss();
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
