package com.example.m_saidika.Models;

public class MpesaResponseItem {
    public String checkoutRequestID;
    public String merchantRequestID;
    public String resultCode;
    public String resultDesc;
    public String amount;
    public String receipt;
    public String date;
    public String phone;

    public MpesaResponseItem(String checkoutRequestID, String merchantRequestID, String resultCode, String resultDesc, String amount, String receipt, String date, String phone) {
        this.checkoutRequestID = checkoutRequestID;
        this.merchantRequestID = merchantRequestID;
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.amount = amount;
        this.receipt = receipt;
        this.date = date;
        this.phone = phone;
    }

    public MpesaResponseItem(String checkoutRequestID, String merchantRequestID, String resultCode, String resultDesc) {
        this.checkoutRequestID = checkoutRequestID;
        this.merchantRequestID = merchantRequestID;
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }

    public MpesaResponseItem() {
    }

    public String getCheckoutRequestID() {
        return checkoutRequestID;
    }

    public void setCheckoutRequestID(String checkoutRequestID) {
        this.checkoutRequestID = checkoutRequestID;
    }

    public String getMerchantRequestID() {
        return merchantRequestID;
    }

    public void setMerchantRequestID(String merchantRequestID) {
        this.merchantRequestID = merchantRequestID;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
