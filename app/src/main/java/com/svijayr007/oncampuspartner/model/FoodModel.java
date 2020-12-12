package com.svijayr007.oncampuspartner.model;

public class FoodModel {
    private String key;
    private String name,image,id,description;
    private String menuId;
    private Long price;
    private boolean available;
    private  int isVeg;



    public FoodModel() {}

    public FoodModel(String key, String name, String image, String id, String description, String menuId, Long price, boolean available, int isVeg) {
        this.key = key;
        this.name = name;
        this.image = image;
        this.id = id;
        this.description = description;
        this.menuId = menuId;
        this.price = price;
        this.available = available;
        this.isVeg = isVeg;
    }

    public int getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(int isVeg) {
        this.isVeg = isVeg;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
