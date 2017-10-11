package com.example.chris.mainactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chris.mainactivity.Objects.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DonateActivity extends AppCompatActivity {

    private Activity context = this;
    public static final int PAYPAL_REQUEST_CODE = 484;
    private EditText edtamount;
    private String amount,id;
    private FirebaseFirestore db;
    private String TAG = "DONATE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);


        db = FirebaseFirestore.getInstance();

        edtamount = (EditText)findViewById(R.id.edt_amount);
        Button btnamount = (Button) findViewById(R.id.btn_donate);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();

        btnamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                amount = edtamount.getText().toString();
                if(!amount.equals("") && amount != null){
                  getPayment(amount);
                }else{
                    Toast.makeText(context, "Set Desired Amount First", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void getPayment(String amount) {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "PHP", "Donation Amount",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, Constants.PaypalConfig);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {

                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        JSONObject details = new JSONObject(paymentDetails);

                        JSONObject response = new JSONObject(details.getString("response"));

                        String state = response.getString("state");

                        if(state != null && state.equals("approved")){
                            Toast.makeText(context, "Thank you for your Donation of P"+amount, Toast.LENGTH_SHORT).show();

                            String time = response.getString("create_time");
                            String trans_id = response.getString("id");

                            Map<String, Object> transaction = new HashMap<>();
                            transaction.put("time", time);
                            transaction.put("id", trans_id);
                            transaction.put("amount", amount);

                            db.collection("transaction_"+id).add(transaction)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DATA_ADDED TO FS" + documentReference.getId());
                                    UpdateProgress(amount);
                                    finish();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void UpdateProgress(final String amount){
        // Gets the Current Amount then adds  the latest Donation
        DocumentReference docRef = db.collection("Projects").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String current = task.getResult().getString("current");

                        BigDecimal updatedval = BigDecimal.valueOf(Long.parseLong(current) + Long.parseLong(amount));

                        // Updates the Current Document with the latest Update
                        Map<String, Object> update = new HashMap<>();
                        update.put("current", String.valueOf(updatedval));


                        db.collection("Projects").document(id).set(update, SetOptions.merge());
                    } else {
                        Log.d(TAG, "FS Update Failed", task.getException());
                    }
                }else{
                    Log.d(TAG, "FS RETRIEVE Failed", task.getException());
                }
            }

        });
    }
}
