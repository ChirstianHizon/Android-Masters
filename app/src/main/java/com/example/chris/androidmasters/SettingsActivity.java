package com.example.chris.androidmasters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.chris.androidmasters.CRUD.Create.AddProjectDetailsActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class SettingsActivity extends AppCompatActivity {

    private Activity context = this;
    private FirebaseAuth mAuth;
    private String TAG = "SETTINGS_ACT";
    private int RC_SIGN_IN = 100;
    private GoogleSignInOptions gso;
    private FirebaseFirestore db;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser User;
    private View aboutPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser User = mAuth.getCurrentUser();


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if(User == null){
            finish();
        }else {
            aboutPage = new AboutPage(this)
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
        copyRightsElement.setTitle("Sign in");
        copyRightsElement.setIconDrawable(R.drawable.ic_supervisor);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signGoogleIn();
            }
        });
        return copyRightsElement;
    }

    private void enterAdmin(){
        Intent intent = new Intent(context, AddProjectDetailsActivity.class);
        startActivity(intent);
    }

    private void signGoogleIn() {
        Intent signInIntent =Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            User = mAuth.getCurrentUser();

                            if(User != null) {
//                                Map<String, Object> admin = new HashMap<>();
//                                admin.put("ID", User.getUid());
//                                admin.put("Name", User.getDisplayName());
//                                admin.put("Phone", User.getPhoneNumber());
//                                admin.put("Email", User.getEmail());
//                                admin.put("Provider", User.getProviderId());
//                                admin.put("FCM Token", FirebaseInstanceId.getInstance().getToken());
//                                admin.put("date", new Date());
//                                admin.put("status", true);
//
//                                db.collection("Administrators").document(User.getEmail()).set(admin, SetOptions.merge());

                                db.collection("Administrators").document(User.getEmail())
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            if(documentSnapshot.getBoolean("status")){
                                                Toast.makeText(context, "Welcome "+User.getDisplayName(), Toast.LENGTH_SHORT).show();
                                                enterAdmin();
                                            }else{
                                                Toast.makeText(context, "Privileges has been removed", Toast.LENGTH_SHORT).show();
                                            }

                                        }else{
                                            Toast.makeText(context, "Welcome "+User.getDisplayName(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
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
