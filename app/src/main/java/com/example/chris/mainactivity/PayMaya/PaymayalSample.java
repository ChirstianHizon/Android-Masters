package com.example.chris.mainactivity.PayMaya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chris.mainactivity.Objects.Constants;
import com.example.chris.mainactivity.R;
import com.paymaya.sdk.android.PayMayaConfig;
import com.paymaya.sdk.android.checkout.PayMayaCheckout;
import com.paymaya.sdk.android.checkout.PayMayaCheckoutCallback;
import com.paymaya.sdk.android.checkout.models.Address;
import com.paymaya.sdk.android.checkout.models.Buyer;
import com.paymaya.sdk.android.checkout.models.Checkout;
import com.paymaya.sdk.android.checkout.models.Contact;
import com.paymaya.sdk.android.checkout.models.Item;
import com.paymaya.sdk.android.checkout.models.RedirectUrl;
import com.paymaya.sdk.android.checkout.models.TotalAmount;
import com.paymaya.sdk.android.payment.PayMayaPayment;
import com.paymaya.sdk.android.payment.models.Card;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymayalSample extends AppCompatActivity implements PayMayaCheckoutCallback {

    private Activity context = this;
    private TextView tvstatus;
    private PayMayaCheckout mPayMayaCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymaya_sample);

        Button btncheckout = (Button)findViewById(R.id.btn_checkout);
        tvstatus = (TextView)findViewById(R.id.tv_status);


        PayMayaConfig.setEnvironment(PayMayaConfig.ENVIRONMENT_SANDBOX);

        mPayMayaCheckout = new PayMayaCheckout(Constants.PayMayaKey, this);

        //Set Up Card
        String cardNumber = "5424820004138093";
        String expiryMonth = "09";
        String expiryYear = "2021";
        String cvc = "483";
        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvc);

        Contact contact = new Contact("09983032084", "klingk98@gmail.com");
        Address address = new Address("146 Burgos Extension", "", "Talisay", "Negros Occidental", "6110", "63");
        Buyer buyer = new Buyer("Christian", "Gerard", "Hizon");
        buyer.setContact(contact);
        buyer.setBillingAddress(address);
        buyer.setShippingAddress(address);

        PayMayaPayment payMayaPayment = new PayMayaPayment(Constants.PayMayaKey, card);

        BigDecimal summaryTotal = BigDecimal.valueOf(0);
        List itemsList = new ArrayList<>();
        String currency = "PHP";

        BigDecimal item1Amount = BigDecimal.valueOf(100);
        summaryTotal.add(item1Amount);
        TotalAmount totalAmount = new TotalAmount(item1Amount, currency);
        int quantity = 10;
        Item item1 = new Item("Item 1 name", quantity, totalAmount);
        item1.setSkuCode("SKU code");
        item1.setDescription("bag");
        itemsList.add(item1);

        String successURL = "http://yourshop.com/success";
        String failedURL = "http://yourshop.com/failed";
        String canceledURL = "http://yourshop.com/canceled";

        RedirectUrl redirectUrl = new RedirectUrl(successURL, failedURL, canceledURL);

        String requestReference = "YourRequestReferenceCode";
        final Checkout checkout = new Checkout(totalAmount, buyer, itemsList, requestReference, redirectUrl);

        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeCheckout(checkout);
            }
        });

    }

    private void executeCheckout(Checkout payload) {
        mPayMayaCheckout.execute(context, payload);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPayMayaCheckout.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckoutSuccess() {

    }

    @Override
    public void onCheckoutCanceled() {

    }

    @Override
    public void onCheckoutFailure(String message) {
        tvstatus.setText(message);

    }
}
