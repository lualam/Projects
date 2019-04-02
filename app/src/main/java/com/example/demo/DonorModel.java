package com.example.demo;

public class DonorModel {
    String name, bloodgroup, email, phone;

    public DonorModel(String name, String bloodgroup, String email, String phone) {
        this.name = name;
        this.bloodgroup = bloodgroup;
        this.email = email;
        this.phone = phone;
    }

    public DonorModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}