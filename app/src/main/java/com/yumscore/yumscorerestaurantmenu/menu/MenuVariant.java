package com.yumscore.yumscorerestaurantmenu.menu;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by steve on 5/1/17.
 */

public class MenuVariant implements Serializable {
    private static final long serialVersionUID = 1L;

    private String restaurantId;
    private String variantId;
    private String name;
    private ArrayList<MenuSection> sections;

    public MenuVariant(String restaurantId, String variantId, String name) {
        this.restaurantId = restaurantId;
        this.variantId = variantId;
        this.name = name;
        this.sections = new ArrayList<>();
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MenuSection> getSections() {
        return sections;
    }
}
