package com.yumscore.yumscorerestaurantmenu.startup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import com.squareup.picasso.Target;
import com.yumscore.yumscorerestaurantmenu.menu.Menu;
import com.yumscore.yumscorerestaurantmenu.menu.MenuVariantsActivity;
import com.yumscore.yumscorerestaurantmenu.request.SendAndParseQuickMenuRequest;

import java.util.ArrayList;
import java.util.List;

import static com.yumscore.yumscorerestaurantmenu.menu.MenuVariantsActivity.blockedKeys;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    public static final String SHARED_PREFERENCES_QUICKID = "quickId";
    public static final String SHARED_PREFERENCES_OFFLINE_MENU = "offlineMenu";

    public static final String SHARED_PREFERENCES_ISOFFLINE = "isOffline";

    final List<Target> targets = new ArrayList<>();

    private String savedQuickId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        savedQuickId = sharedPreferences.getString(SHARED_PREFERENCES_QUICKID, null);
        if(savedQuickId != null) {
            SetRestaurantActivity.preventStatusBarExpansion(this);
            String savedMenu = sharedPreferences.getString(SHARED_PREFERENCES_OFFLINE_MENU, null);
            Menu parsedMenu = SendAndParseQuickMenuRequest.parseMenu(savedMenu, true);
            Intent intent = new Intent(this, MenuVariantsActivity.class);
            intent.putExtra(SetRestaurantActivity.EXTRA_KEY_MENU, parsedMenu);
            startActivity(intent);
            finish();
//          findMenu(savedQuickId);
        }else{
            Intent intent = new Intent(this, SetRestaurantActivity.class);
            startActivity(intent);
            finish();
        }
    }

//    private void findMenu(String quickId){
//        SendAndParseQuickMenuRequest sendAndParseQuickMenuRequest = new SendAndParseQuickMenuRequest(this, true);
//        sendAndParseQuickMenuRequest.execute(quickId);
//    }

//    @Override
//    public void onQuickMenuFailed(String message) {
//        Log.d(TAG, "onQuickMenuFailed: starts");
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String savedMenu = sharedPreferences.getString(SHARED_PREFERENCES_OFFLINE_MENU, null);
//        Log.d(TAG, "onQuickMenuFailed: saved menu: " + savedMenu);
//        if(savedMenu != null){
//            sharedPreferences.edit().putBoolean(SHARED_PREFERENCES_ISOFFLINE, true).apply();
//            Menu parsedMenu = SendAndParseQuickMenuRequest.parseMenu(savedMenu, true);
//            Intent intent = new Intent(this, MenuVariantsActivity.class);
//            intent.putExtra(EXTRA_KEY_MENU, parsedMenu);
//            startActivity(intent);
//            finish();
//        }else{
//            Toast.makeText(this, "Error loading menu.", Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    public void onQuickMenuParsed(Menu menu, String rawMenu) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        String savedMenu = sharedPreferences.getString(SHARED_PREFERENCES_OFFLINE_MENU, null);
//        if(savedMenu == null || !savedMenu.equals(rawMenu)){
//            sharedPreferences.edit().putString(SHARED_PREFERENCES_OFFLINE_MENU, rawMenu).putBoolean(SHARED_PREFERENCES_ISOFFLINE, false).apply();
//            Target logoTarget = picassoImageTarget(getApplicationContext(), "logo", "logo.png");
//            targets.add(logoTarget);
//            Picasso.with(this).load(menu.getLogo()).into(targets.get(0));
//            for(int k = 0; k < menu.getMenuVariants().size(); k++){
//                for(int i = 0; i < menu.getMenuVariants().get(k).getSections().size(); i++){
//                    MenuSection currentSection = menu.getMenuVariants().get(k).getSections().get(i);
//                    for(int j = 0; j < currentSection.getDishes().size(); j++){
//                        Target targetThumbnail = picassoImageTarget(getApplicationContext(), "dishThumbnails", currentSection.getDishes().get(j).getDishId() + ".png");
//                        targets.add(targetThumbnail);
//                        Picasso.with(this).load(currentSection.getDishes().get(j).getPhotoThumbnailUrl()).into(targets.get(targets.size() - 1));
//                        Target targetImage = picassoImageTarget(getApplicationContext(), "dishImages", currentSection.getDishes().get(j).getDishId()+".png");
//                        targets.add(targetImage);
//                        Picasso.with(this).load(currentSection.getDishes().get(j).getPhotoUrl()).into(targets.get(targets.size() - 1));
//                    }
//                }
//            }
//        }
//        Intent intent = new Intent(this, MenuVariantsActivity.class);
//        intent.putExtra(EXTRA_KEY_MENU, menu);
//        startActivity(intent);
//        finish();
//    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}