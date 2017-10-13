package com.example.chris.androidmasters.Objects;

import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.Currency;

/**
 * Created by chris on 08/10/2017.
 */

public class Constants {

    public static String PayMayaKeyCheckout = "pk-iaioBC2pbY6d3BVRSebsJxghSHeJDW4n6navI7tYdrN";
    public static String PayMayaKeyPayment = "pk-N6TvoB4GP2kIgNz4OCchCTKYvY5kPQd2HDRSg8rPeQG";

    public static String PaypalKey = "AXYYXi1dM2t5X0j1OvdaXjaUwBHCSY10gMYKyFBz6N5qRKEm2x7IlXx9SFNhwYXyqzhsfK8X8oaltmiP";

    public static PayPalConfiguration PaypalConfig = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constants.PaypalKey);

    public static String getCurrencySymbol(String countryCode) {
        // Locale locale = new Locale("", countryCode);
        Currency currency = Currency.getInstance(countryCode);
        String symbol = currency.getSymbol();
        return symbol;
    }

}
