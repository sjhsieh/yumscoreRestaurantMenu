package com.yumscore.yumscorerestaurantmenu;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by steve on 3/18/17.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    void activateToolbar(String title, boolean enableHome){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}
