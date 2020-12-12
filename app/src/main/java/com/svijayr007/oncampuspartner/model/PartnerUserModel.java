package com.svijayr007.oncampuspartner.model;

public class PartnerUserModel {
    private String uid,name,phone,restaurant,campusId,email;
    private boolean active;
    private long lastVisited;

    public PartnerUserModel() {
    }

    public PartnerUserModel(String uid, String name, String phone, String restaurant, String campusId, String email, boolean active, long lastVisited) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.restaurant = restaurant;
        this.campusId = campusId;
        this.email = email;
        this.active = active;
        this.lastVisited = lastVisited;
    }



    public long getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(long lastVisited) {
        this.lastVisited = lastVisited;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
