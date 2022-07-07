package com.example.m_saidika.Models;

public class Passenger {
    private String paymentId,time,userId;

    public Passenger(String paymentId, String time, String userId) {
        this.paymentId = paymentId;
        this.time = time;
        this.userId = userId;
    }

    public Passenger() {
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
