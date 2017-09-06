package com.yumscore.yumscorerestaurantmenu.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 2/20/17.
 */

public class MenuSection implements Serializable{

    private static final long serialVersionUID = 1L;

    private String name;
    private String restaurantId;
    private String sectionId;
    private String sectionNumber;
    private String sectionSubtext;
    private List<Dish> dishes;

    public MenuSection(String name, String restaurantId, String sectionId, String sectionNumber, String sectionSubtext){
        this.name = name;
        this.restaurantId = restaurantId;
        this.sectionId = sectionId;
        this.sectionNumber = sectionNumber;
        this.sectionSubtext = sectionSubtext;
        this.dishes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSectionSubtext() {
        return sectionSubtext;
    }

    public void setSectionSubtext(String sectionSubtext) {
        this.sectionSubtext = sectionSubtext;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
