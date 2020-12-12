package com.svijayr007.oncampuspartner.model;

public class CampusModel {
    private String name,address,campusId,imageUrl;
    private  int number_of_restaurants;

    public CampusModel() {
    }

    public CampusModel(String name, String address, String campusId, String imageUrl, int number_of_restaurants) {
        this.name = name;
        this.address = address;
        this.campusId = campusId;
        this.imageUrl = imageUrl;
        this.number_of_restaurants = number_of_restaurants;
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

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNumber_of_restaurants() {
        return number_of_restaurants;
    }

    public void setNumber_of_restaurants(int number_of_restaurants) {
        this.number_of_restaurants = number_of_restaurants;
    }
}
