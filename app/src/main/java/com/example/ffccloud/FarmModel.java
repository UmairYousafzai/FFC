package com.example.ffccloud;

public class FarmModel {
    private String name;
    private String address;

    public FarmModel() {
    }

    public FarmModel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
