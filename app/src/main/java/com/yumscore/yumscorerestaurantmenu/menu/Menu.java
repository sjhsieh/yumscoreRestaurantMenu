package com.yumscore.yumscorerestaurantmenu.menu;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by steve on 4/20/17.
 */

public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    private String restaurantName;
    private String logo;
    private String snsTopic;
    private ArrayList<MenuVariant> menuVariants;

    public Menu(String restaurantName, String logo, String snsTopic, ArrayList<MenuVariant> menuVariants) {
        this.restaurantName = restaurantName;
        this.logo = logo;
        this.menuVariants = menuVariants;
        this.snsTopic = snsTopic;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ArrayList<MenuVariant> getMenuVariants() {
        return menuVariants;
    }

    public String getSnsTopic() {
        return snsTopic;
    }

    public void setSnsTopic(String snsTopic) {
        this.snsTopic = snsTopic;
    }
}
