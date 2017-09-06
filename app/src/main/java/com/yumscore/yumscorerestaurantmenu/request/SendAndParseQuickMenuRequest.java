package com.yumscore.yumscorerestaurantmenu.request;

import android.os.AsyncTask;
import android.util.Log;

import com.yumscore.yumscorerestaurantmenu.menu.Dish;
import com.yumscore.yumscorerestaurantmenu.menu.Menu;
import com.yumscore.yumscorerestaurantmenu.menu.MenuSection;
import com.yumscore.yumscorerestaurantmenu.menu.MenuVariant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by steve on 2/20/17.
 */

public class SendAndParseQuickMenuRequest extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetAndParseMenuSections";
    private static final String QUICK_MENU_URL = "http://raddish.yumscore.com/api/v1/getMenuWithQuickId?quickId=%s&hasMenuVariants=%s";
    private static final String GET_MENU_URL = "http://raddish.yumscore.com/api/v1/getMenu?restaurantId=%s";
    private static final String RESPONSE_PARAM_SECTIONNUMBER = "sectionnumber";
    private static final String RESPONSE_PARAM_NAME = "name";
    private static final String RESPONSE_PARAM_LOGO = "logo";
    private static final String RESPONSE_PARAM_SNSTOPIC = "snstopic";
    private static final String RESPONSE_PARAM_MENU = "menu";
    private static final String RESPONSE_PARAM_RESTAURANTID = "restaurantid";
    private static final String RESPONSE_PARAM_DISHES = "dishes";
    private static final String RESPONSE_PARAM_DISHID = "dishid";
    private static final String RESPONSE_PARAM_DESCRIPTION = "description";
    private static final String RESPONSE_PARAM_PRICE = "price";
    private static final String RESPONSE_PARAM_PHOTOURL = "photourl";
    private static final String RESPONSE_PARAM_PHOTOTHUMBNAILURL = "photothumbnailurl";
    private static final String RESPONSE_PARAM_STATUS = "status";
    private static final String RESPONSE_PARAM_MESSAGE = "message";
    private static final String RESPONSE_PARAM_SECTIONSUBTEXT = "sectionsubtext";
    private static final String RESPONSE_PARAM_SECTIONID = "sectionid";
    private static final String RESPONSE_PARAM_MENUVARIANTID = "menuvariantid";
    private static final String RESPONSE_PARAM_SECTIONS = "sections";
    private static final String MESSAGE_NO_RESTAURANT_FOUND = "no restaurant found";
    private static final String TOAST_NO_RESTAURANT_FOUND = "No menu found with this id.";
    private static final String ERROR_MESSAGE = "Error retrieving menu. Please try again.";

    private QuickMenuListener quickMenuCallback;
    private boolean withQuickId;

    public interface QuickMenuListener{
        void onQuickMenuParsed(Menu menu, String rawMenu);
        void onQuickMenuFailed(String message);
    }

    public SendAndParseQuickMenuRequest(QuickMenuListener quickMenuCallback, boolean withQuickId) {
        this.quickMenuCallback = quickMenuCallback;
        this.withQuickId = withQuickId;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = sendQuickMenuRequest(params[0]);
        return response;
    }

    private String sendQuickMenuRequest(String menuId){
        try{
            URL url = null;
            if(withQuickId){
                url = new URL(String.format(QUICK_MENU_URL, menuId, "1"));
            }else {
                url = new URL(String.format(GET_MENU_URL, menuId));
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);
            StringBuilder result = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while(null != (line = reader.readLine())){
                result.append(line).append("\n");
            }
            Log.d(TAG, "sendMenuSectionRequest: " + result);
            return result.toString();
        }catch(MalformedURLException e){
            Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
        }catch(IOException e){
            Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
        }catch(SecurityException e){
            Log.e(TAG, "downloadXML: Security Exception. Needs Permission? " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        if(response == null){
            Log.e(TAG, "onPostExecute: Error downloading");
            if(quickMenuCallback != null){
                quickMenuCallback.onQuickMenuFailed(ERROR_MESSAGE);
            }
            return;
        }
        parseAndSendResponse(response);
    }

    public static Menu parseMenu(String rawMenu, boolean withQuickId){
        try {
            ArrayList<MenuVariant> menuVariants = new ArrayList<>();
            JSONObject jsonResponse = new JSONObject(rawMenu);
            String restaurantName = null;
            String logo = null;
            String snsTopic = null;
            JSONArray jsonMenuVariants = jsonResponse.getJSONArray(RESPONSE_PARAM_MENU);
            for(int k = 0; k < jsonMenuVariants.length(); k++){
                JSONObject jsonMenuVariant = jsonMenuVariants.getJSONObject(k);
                String restaurantId = jsonMenuVariant.getString(RESPONSE_PARAM_RESTAURANTID);
                String menuVariantId = jsonMenuVariant.getString(RESPONSE_PARAM_MENUVARIANTID);
                String menuVariantName = jsonMenuVariant.getString(RESPONSE_PARAM_NAME);
                MenuVariant menuVariant = new MenuVariant(restaurantId, menuVariantId, menuVariantName);
                JSONArray jsonMenuSections = jsonMenuVariant.getJSONArray(RESPONSE_PARAM_SECTIONS);
                for(int i = 0; i < jsonMenuSections.length(); i++){
                    JSONObject jsonMenuSection = jsonMenuSections.getJSONObject(i);
                    String menuSectionRestaurantId = jsonMenuSection.getString(RESPONSE_PARAM_RESTAURANTID);
                    String menuSectionName = jsonMenuSection.getString(RESPONSE_PARAM_NAME);
                    String sectionSubtext = jsonMenuSection.getString(RESPONSE_PARAM_SECTIONSUBTEXT);
                    String sectionNumber = jsonMenuSection.getString(RESPONSE_PARAM_SECTIONNUMBER);
                    String sectionId = jsonMenuSection.getString(RESPONSE_PARAM_SECTIONID);
                    MenuSection menuSection = new MenuSection(menuSectionName, menuSectionRestaurantId, sectionId, sectionNumber, sectionSubtext);
                    JSONArray jsonDishes = jsonMenuSection.getJSONArray(RESPONSE_PARAM_DISHES);
                    for(int j = 0; j < jsonDishes.length(); j++){
                        JSONObject jsonDish = jsonDishes.getJSONObject(j);
                        String dishId = jsonDish.getString(RESPONSE_PARAM_DISHID);
                        String dishName = jsonDish.getString(RESPONSE_PARAM_NAME);
                        String dishDescription = jsonDish.getString(RESPONSE_PARAM_DESCRIPTION);
                        String dishPrice = jsonDish.getString(RESPONSE_PARAM_PRICE);
                        String dishPhotoUrl = jsonDish.getString(RESPONSE_PARAM_PHOTOURL);
                        String dishPhotoThumbnailUrl = jsonDish.getString(RESPONSE_PARAM_PHOTOTHUMBNAILURL);
                        Dish dish = new Dish(menuSectionRestaurantId, sectionId, sectionNumber, dishId, dishName, dishDescription, dishPrice, dishPhotoUrl, dishPhotoThumbnailUrl);
                        menuSection.getDishes().add(dish);
                    }
                    menuVariant.getSections().add(menuSection);
                }
                menuVariants.add(menuVariant);
            }
            if(withQuickId){
                restaurantName = jsonResponse.getString(RESPONSE_PARAM_NAME);
                logo = jsonResponse.getString(RESPONSE_PARAM_LOGO);
                snsTopic = jsonResponse.getString(RESPONSE_PARAM_SNSTOPIC);
            }
            Menu menu = new Menu(restaurantName, logo, snsTopic, menuVariants);
            return menu;
        }catch(JSONException e){
            e.printStackTrace();
            Log.e(TAG, "parseAndSendResponse: Error proecessing json data " + e.getMessage());
            return null;
        }
    }

    private void parseAndSendResponse(String response){
        try {
            ArrayList<MenuSection> menuSections = new ArrayList<>();
            JSONObject jsonResponse = new JSONObject(response);
            String status = jsonResponse.getString(RESPONSE_PARAM_STATUS);
            Integer intStatus = Integer.parseInt(status);
            if(intStatus == 200) {
                Menu parsedMenu = parseMenu(response, withQuickId);
                if(quickMenuCallback != null){
                    quickMenuCallback.onQuickMenuParsed(parsedMenu, response);
                }
            }else if(intStatus == 400){
                String message = jsonResponse.getString(RESPONSE_PARAM_MESSAGE);
                if(message.equals(MESSAGE_NO_RESTAURANT_FOUND)) {
                    if (quickMenuCallback != null) {
                        quickMenuCallback.onQuickMenuFailed(TOAST_NO_RESTAURANT_FOUND);
                    }
                }else{
                    if(quickMenuCallback != null){
                        quickMenuCallback.onQuickMenuFailed(ERROR_MESSAGE);
                    }
                }
            }else{
                if(quickMenuCallback != null){
                    quickMenuCallback.onQuickMenuFailed(ERROR_MESSAGE);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
            Log.e(TAG, "parseAndSendResponse: Error proecessing json data " + e.getMessage());
            if(quickMenuCallback != null){
                quickMenuCallback.onQuickMenuFailed(ERROR_MESSAGE);
            }
        }
    }
}
