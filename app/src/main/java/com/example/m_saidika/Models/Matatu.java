package com.example.m_saidika.Models;

public class Matatu {
    private String numberPlate,destination,capacity,farePrice,departureTime,matatuId;

    public Matatu(String numberPlate, String destination, String capacity, String farePrice, String departureTime,String matatuId) {
        this.numberPlate = numberPlate;
        this.destination = destination;
        this.capacity = capacity;
        this.farePrice = farePrice;
        this.departureTime = departureTime;
        this.matatuId=matatuId;
    }

    public Matatu() {

    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getFarePrice() {
        return farePrice;
    }

    public void setFarePrice(String farePrice) {
        this.farePrice = farePrice;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getMatatuId() {
        return matatuId;
    }

    public void setMatatuId(String matatuId) {
        this.matatuId = matatuId;
    }
}
