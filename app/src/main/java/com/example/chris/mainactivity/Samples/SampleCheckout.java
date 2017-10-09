package com.example.chris.mainactivity.Samples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.mainactivity.Objects.Constants;
import com.example.chris.mainactivity.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class SampleCheckout extends AppCompatActivity{

    private Activity context = this;
    private TextView tvstatus;
    private String amount;
    private EditText edtdonation;

    public static final int PAYPAL_REQUEST_CODE = 484;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constants.PaypalKey);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_checkout);

        Button btncheckout = (Button)findViewById(R.id.btn_checkout);
        edtdonation = (EditText)findViewById(R.id.edt_dontaion);
        tvstatus = (TextView)findViewById(R.id.tv_status);

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = edtdonation.getText().toString();

                if((Integer.valueOf(amount) > 0) || (amount != null)){
                    getPayment(amount);
                }else{
                    Toast.makeText(context, "Invalid Donation Amount", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Calls the Paypal Service
        Intent intent = new Intent(context, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

    }

    private void getPayment(String amount){

        //Creating a paypalpayment

        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "PHP", "Donation Amount",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

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
                            tvstatus.setText("Thank You " +
                                    "For Donating P "+ amount +" To Our Cause");
                            edtdonation.setText("0");
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



}
