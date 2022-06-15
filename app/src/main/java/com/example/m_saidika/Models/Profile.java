package com.example.m_saidika.Models;

public class Profile {
    private String firstName;
    private String lastName;
    private String phone;
    private String admNo;
    private String idNo;
    private String photo;
    private String bio;
    private String userId;


    public Profile(String firstName, String lastName, String phone, String admNo, String idNo, String photo, String bio,String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.admNo = admNo;
        this.idNo = idNo;
        this.photo = photo;
        this.bio = bio;
        this.userId=userId;
    }

    public Profile() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdmNo() {
        return admNo;
    }

    public void setAdmNo(String admNo) {
        this.admNo = admNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
