package com.yumscore.yumscorerestaurantmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.yumscore.yumscorerestaurantmenu.SetRestaurantActivity.EXTRA_KEY_MENU;
import static com.yumscore.yumscorerestaurantmenu.SplashActivity.SHARED_PREFERENCES_OFFLINE_MENU;
import static com.yumscore.yumscorerestaurantmenu.SplashActivity.SHARED_PREFERENCES_QUICKID;

/**
 * Created by steve on 6/13/17.
 */

public class YumscoreFirebaseMessagingService extends FirebaseMessagingService implements SendAndParseQuickMenuRequest.QuickMenuListener {
    private static final String KEY_DOWNLOADIDS = "downloadIds";
    private static final String KEY_DELETEIDS = "deleteIds";

    final List<Target> targets = new ArrayList<>();
    List<String> downloadIdsList = new ArrayList<>();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Received Firebase Message From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String downloadIdsString = remoteMessage.getData().get(KEY_DOWNLOADIDS);
            if("".equals(downloadIdsString)){
                downloadIdsList = new ArrayList<>();
            }else {
                String[] downloadIds = downloadIdsString.split(",");
                downloadIdsList = Arrays.asList(downloadIds);
            }
            String deleteIdsString = remoteMessage.getData().get(KEY_DELETEIDS);
            String[] deleteIds = null;
            if(!"".equals(deleteIdsString)){
                deleteIds = deleteIdsString.split(",");
            }
            Log.d(TAG, "onMessageReceived: ids parsed");
            refreshMenu(deleteIds);
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void refreshMenu(String[] deleteIds){
        if(deleteIds != null) {
            DeleteImages deleteImages = new DeleteImages(getApplicationContext());
            deleteImages.execute(deleteIds);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String savedQuickId = sharedPreferences.getString(SHARED_PREFERENCES_QUICKID, null);
        SendAndParseQuickMenuRequest sendAndParseQuickMenuRequest = new SendAndParseQuickMenuRequest(this, true);
        sendAndParseQuickMenuRequest.execute(savedQuickId);
    }

    @Override
    public void onQuickMenuParsed(Menu menu, String rawMenu) {
        if(downloadIdsList.size() > 0){
            ArrayList<Dish> dishesToDownload = new ArrayList<>();
            for(MenuVariant variant : menu.getMenuVariants()){
                for(MenuSection section : variant.getSections()){
                    for(Dish dish : section.getDishes()){
                        if(downloadIdsList.contains(dish.getDishId())){
                            dishesToDownload.add(dish);
                        }
                    }
                }
            }
            for(Dish dish: dishesToDownload){
                Target target = SetRestaurantActivity.picassoImageTarget(getApplicationContext(), "dishThumbnails", dish.getDishId() + ".png");
                targets.add(target);
                Picasso.with(getApplicationContext()).load(dish.getPhotoThumbnailUrl()).into(targets.get(targets.size() - 1));
                Target targetImage = SetRestaurantActivity.picassoImageTarget(getApplicationContext(), "dishImages", dish.getDishId()+".png");
                targets.add(targetImage);
                Picasso.with(getApplicationContext()).load(dish.getPhotoUrl()).into(targets.get(targets.size() - 1));
            }
            targets.clear();
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putString(SHARED_PREFERENCES_OFFLINE_MENU, rawMenu).apply();
        Intent intent = new Intent();
        intent.setClass(this, MenuVariantsActivity.class);
        intent.putExtra(EXTRA_KEY_MENU, menu);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Toast.makeText(getApplicationContext(), "Menu Updated.", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    @Override
    public void onQuickMenuFailed(String message) {

    }
}
