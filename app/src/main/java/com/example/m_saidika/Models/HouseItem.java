package com.example.m_saidika.Models;

public class HouseItem {
    public String apartmentName;
    public String apartmentPic;
    public String description;
    public String houseId;
    public String location;
    public String owner;
    public String phoneNumber;
    public String rent;

    public HouseItem(String apartmentName, String apartmentPic, String description, String houseId, String location, String owner, String phoneNumber, String rent) {
        this.apartmentName = apartmentName;
        this.apartmentPic = apartmentPic;
        this.description = description;
        this.houseId = houseId;
        this.location = location;
        this.owner = owner;
        this.phoneNumber = phoneNumber;
        this.rent = rent;
    }

    public HouseItem() {
    }

    public String getApartmentName() {
        return apartmentName;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getApartmentPic() {
        return apartmentPic;
    }

    public void setApartmentPic(String apartmentPic) {
        this.apartmentPic = apartmentPic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }
}

