package com.svijayr007.oncampuspartner.model;

public class RestaurantModel {
    private String key,name, campus,imageUrl,phone,campusId,partner;
    private double rating;
    private Long ratingCount;
    private  int prepTime, priceForTwoPeople;
    private String foodCategories;
    private String isOpened;

    public RestaurantModel() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getPriceForTwoPeople() {
        return priceForTwoPeople;
    }

    public void setPriceForTwoPeople(int priceForTwoPeople) {
        this.priceForTwoPeople = priceForTwoPeople;
    }

    public String getFoodCategories() {
        return foodCategories;
    }

    public void setFoodCategories(String foodCategories) {
        this.foodCategories = foodCategories;
    }

    public String getIsOpened() {
        return isOpened;
    }

    public void setIsOpened(String isOpened) {
        this.isOpened = isOpened;
    }
}
