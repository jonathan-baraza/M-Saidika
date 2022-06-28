package com.example.m_saidika.Models;

public class JobItem {
    public String companyName;
    public String location;
    public String phone;
    public String description;
    public String requirements;
    public String owner;
    private String jobId;

    public JobItem(String companyName, String location, String phone, String description, String requirements, String owner,String jobId) {
        this.companyName = companyName;
        this.location = location;
        this.phone = phone;
        this.description = description;
        this.requirements = requirements;
        this.owner = owner;
        this.jobId=jobId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public JobItem() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
