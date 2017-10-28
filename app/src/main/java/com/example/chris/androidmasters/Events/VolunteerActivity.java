package com.example.chris.androidmasters.Events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chris.androidmasters.Options.SettingsActivity;
import com.example.chris.androidmasters.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VolunteerActivity extends AppCompatActivity {

    private FirebaseUser User;
    private Activity context = this;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        getSupportActionBar().setTitle("Volunteer");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Button btnvolunteer = (Button)findViewById(R.id.btn_volunteer);
        final EditText name = (EditText)findViewById(R.id.edt_name);
        final EditText age = (EditText)findViewById(R.id.edt_age);
        final EditText contact = (EditText)findViewById(R.id.edt_contact);
        EditText email = (EditText)findViewById(R.id.edt_email);


        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();

        if (User == null) {
            Toast.makeText(context, "Login to volunteer", Toast.LENGTH_SHORT).show();
        }else if(!User.isAnonymous()){
            email.setText(User.getEmail());
            email.setEnabled(false);
            email.setFocusable(false);
            email.setInputType(InputType.TYPE_NULL);

            name.setText(User.getDisplayName());

            contact.setText(User.getPhoneNumber());

        }else{
            Toast.makeText(context, "Login to Volunteer", Toast.LENGTH_SHORT).show();
        }

        btnvolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(User != null && !User.isAnonymous()){
                    Map<String, Object> volunteer = new HashMap<>();
                    volunteer.put("date", new Date());
                    volunteer.put("key", User.getUid());
                    volunteer.put("email", User.getEmail());
                    volunteer.put("name", name.getText().toString());
                    volunteer.put("age", age.getText().toString());
                    volunteer.put("contact", contact.getText().toString());
                    volunteer.put("project", id);

                    db.collection("Volunteers")
                            .add(volunteer)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(context, "You Have successfully Volunteered", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(context, "Login to Volunteer", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SettingsActivity.class);
                    startActivity(intent);
                }


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
