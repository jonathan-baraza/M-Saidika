package com.example.m_saidika.Models;

public class ApplicationItem {
    private String companyName;
    private String logo;
    private String location;
    private String description;
    private String serviceType;
    private String permit;
    private String userId;
    private String verificationStatus;

    public ApplicationItem(String companyName, String logo, String location, String description, String serviceType, String permit, String userId, String verificationStatus) {
        this.companyName = companyName;
        this.logo = logo;
        this.location = location;
        this.description = description;
        this.serviceType = serviceType;
        this.permit = permit;
        this.userId = userId;
        this.verificationStatus = verificationStatus;
    }

    public ApplicationItem() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPermit() {
        return permit;
    }

    public void setPermit(String permit) {
        this.permit = permit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
