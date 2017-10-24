package com.example.chris.androidmasters.Options;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.androidmasters.CRUD.Create.AddProjectDetailsActivity;
import com.example.chris.androidmasters.R;
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

public class NewSettings extends AppCompatActivity {

    private static final int RC_SIGN_IN = 800 ;
    private static final String TAG = "SETTINGS_ACT";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseUser User;
    private View btnsignin;
    private Button btnsignout;
    private Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_settings);

        getSupportActionBar().setTitle("Settings");
//        -----------  add back arrow to toolbar ------------
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser();


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        btnsignin = findViewById(R.id.btn_signin);
        btnsignout = (Button)findViewById(R.id.btn_signout);

        updateUI(User);

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signGoogleIn();
            }
        });

        btnsignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, updateUI UI with the signed-in user's information
                                    Log.d(TAG, "signInAnonymously:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }

                                // ...
                            }
                        });

            }
        });
    }

    private void updateUI(FirebaseUser user){

        if(user == null){
            finish();
        }else{

            TextView tvusername = (TextView)findViewById(R.id.tv_username);
            TextView tvemail = (TextView)findViewById(R.id.tv_email);
            TextView tvkey = (TextView)findViewById(R.id.tv_key);

            tvkey.setText(user.getUid());

            if(user.isAnonymous()){
                tvusername.setText(user.getDisplayName());
                tvemail.setText(user.getEmail());

                btnsignin.setVisibility(View.VISIBLE);
                btnsignout.setVisibility(View.GONE);
            }else{
                tvusername.setText(user.getDisplayName());
                tvemail.setText(user.getEmail());

                btnsignin.setVisibility(View.GONE);
                btnsignout.setVisibility(View.VISIBLE);
            }

        }
    }

    private void signGoogleIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
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
                // Google Sign In failed, updateUI UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.getCurrentUser().linkWithCredential(credential)
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

    private void enterAdmin(){
        Intent intent = new Intent(context, AddProjectDetailsActivity.class);
        startActivity(intent);
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
