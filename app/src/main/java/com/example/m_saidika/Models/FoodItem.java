package com.example.m_saidika.Models;

public class FoodItem {
    private String photo;
    private String name;
    private String price;

    public FoodItem(String photo, String name, String price) {
        this.photo = photo;
        this.name = name;
        this.price = price;
    }

    public FoodItem() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
}
