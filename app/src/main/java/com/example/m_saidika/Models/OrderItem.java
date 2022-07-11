package com.example.m_saidika.Models;

public class OrderItem {
    private String paymentId;
    private String orderId;
    private String userId;
    private String name;
    private String price;
    private String time;
    private String status;
    private String destination;

    public OrderItem(String paymentId, String orderId, String name, String price, String time, String status,String userId,String destination) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.time = time;
        this.status = status;
        this.userId=userId;
        this.destination=destination;
    }

    public OrderItem() {
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
