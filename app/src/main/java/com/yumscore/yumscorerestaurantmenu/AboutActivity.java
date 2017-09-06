package com.yumscore.yumscorerestaurantmenu;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static com.yumscore.yumscorerestaurantmenu.menu.MenuVariantsActivity.blockedKeys;
import static com.yumscore.yumscorerestaurantmenu.menu.MenuVariantsActivity.checkIsAppFlag;
import static com.yumscore.yumscorerestaurantmenu.menu.MenuVariantsActivity.clearIsAppFlag;
import static com.yumscore.yumscorerestaurantmenu.menu.MenuVariantsActivity.setIsAppFlag;
import static com.yumscore.yumscorerestaurantmenu.startup.SetRestaurantActivity.preventStatusBarExpansion;

/**
 * Created by steve on 4/17/17.
 */

public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";

    private static final String SHARED_PREFERENCES_UNLOCKED1 = "unlocked1";
    private static final String SHARED_PREFERENCES_UNLOCKED2 = "unlocked2";

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preventStatusBarExpansion(this);
        clearIsAppFlag(this);
        setContentView(R.layout.activity_about);
        setTitle("About Yumscore");
        Button unlockButton1 = (Button) findViewById(R.id.unlockButton1);
        unlockButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIsUnlocked1(getApplicationContext())){
                    clearIsAppFlag(getApplicationContext());
                    lock1(getApplicationContext());
                    lock2(getApplicationContext());
                }else{
                    unlock1(getApplicationContext());
                }
            }
        });
        Button unlockButton3 = (Button) findViewById(R.id.unlockButton3);
        unlockButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIsUnlocked1(getApplicationContext())){
                    if(checkIsUnlocked2(getApplicationContext())){
                        clearIsAppFlag(getApplicationContext());
                        lock1(getApplicationContext());
                        lock2(getApplicationContext());
                    }else{
                        unlock2(getApplicationContext());
                    }
                }else{
                    clearIsAppFlag(getApplicationContext());
                    lock1(getApplicationContext());
                    lock2(getApplicationContext());
                }
            }
        });
        Button unlockButton2 = (Button) findViewById(R.id.unlockButton2);
        unlockButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkIsUnlocked1(getApplicationContext()) && checkIsUnlocked2(getApplicationContext())){
                    setIsAppFlag(getApplicationContext());
                }else{
                    clearIsAppFlag(getApplicationContext());
                    lock1(getApplicationContext());
                    lock2(getApplicationContext());
                }
            }
        });
    }

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
    protected void onUserLeaveHint()
    {
        super.onUserLeaveHint();
        if(!checkIsAppFlag(getApplicationContext())){
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(this, 0, getIntent().addFlags(FLAG_ACTIVITY_REORDER_TO_FRONT), 0);
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
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

    private void unlock1(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(SHARED_PREFERENCES_UNLOCKED1, true).apply();
    }

    public boolean checkIsUnlocked1(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_UNLOCKED1, false);
    }

    private void lock1(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(SHARED_PREFERENCES_UNLOCKED1).apply();
    }

    private void unlock2(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(SHARED_PREFERENCES_UNLOCKED2, true).apply();
    }

    public boolean checkIsUnlocked2(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_UNLOCKED2, false);
    }

    private void lock2(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(SHARED_PREFERENCES_UNLOCKED2).apply();
    }
}
