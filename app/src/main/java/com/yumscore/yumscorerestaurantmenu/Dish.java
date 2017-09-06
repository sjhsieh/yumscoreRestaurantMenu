package com.yumscore.yumscorerestaurantmenu;

import java.io.Serializable;

/**
 * Created by steve on 2/20/17.
 */

public class Dish implements Serializable{

    private static final long serialVersionUID = 1L;

    private String restaurantId;
    private String sectionId;
    private String sectionNumber;
    private String dishId;
    private String name;
    private String description;
    private String price;
    private String photoUrl;
    private String photoThumbnailUrl;

    public Dish(String restaurantId, String sectionId, String sectionNumber, String dishId, String name, String description, String price, String photoUrl, String photoThumbnailUrl) {
        this.restaurantId = restaurantId;
        this.sectionId = sectionId;
        this.sectionNumber = sectionNumber;
        this.dishId = dishId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photoUrl = photoUrl;
        this.photoThumbnailUrl = photoThumbnailUrl;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoThumbnailUrl() {
        return photoThumbnailUrl;
    }

    public void setPhotoThumbnailUrl(String photoThumbnailUrl) {
        this.photoThumbnailUrl = photoThumbnailUrl;
    }
}
