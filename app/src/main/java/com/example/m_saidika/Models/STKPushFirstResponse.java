package com.example.m_saidika.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class STKPushFirstResponse {
    @SerializedName("MerchantRequestID")
    @Expose
    public String merchantRequestID;

    @SerializedName("CheckoutRequestID")
    @Expose
    public String checkoutRequestID;

    @SerializedName("ResponseCode")
    @Expose
    public String responseCode;

    @SerializedName("ResponseDescription")
    @Expose
    public String responseDescription;

    @SerializedName("CustomerMessage")
    @Expose
    public String customerMessage;

    public STKPushFirstResponse(String merchantRequestID, String checkoutRequestID, String responseCode, String responseDescription, String customerMessage) {
        this.merchantRequestID = merchantRequestID;
        this.checkoutRequestID = checkoutRequestID;
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
        this.customerMessage = customerMessage;
    }

    public String getCheckoutRequestID() {
        return checkoutRequestID;
    }
}
