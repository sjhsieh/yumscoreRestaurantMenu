package com.yumscore.yumscorerestaurantmenu.menu;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yumscore.yumscorerestaurantmenu.AboutActivity;
import com.yumscore.yumscorerestaurantmenu.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static com.yumscore.yumscorerestaurantmenu.startup.SetRestaurantActivity.EXTRA_KEY_MENU;
import static com.yumscore.yumscorerestaurantmenu.startup.SetRestaurantActivity.preventStatusBarExpansion;

/**
 * Created by steve on 5/1/17.
 */

public class MenuVariantsActivity extends AppCompatActivity {
    private static final String TAG = "MenuVariantsActivity";

    public static final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));

    public static final String EXTRA_KEY_VARIANT = "variant";
    public static final String EXTRA_KEY_LOGO = "logo";
    public static final String SHARED_PREFERENCES_IS_APP = "isApp";

    private ListView menuVariantsListView;
    private MenuVariantsAdapter menuVariantsAdapter;
    private Menu menu;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: MenuVariantsActivity Resumed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearIsAppFlag(this);
        preventStatusBarExpansion(this);
        Log.d(TAG, "onCreate: MenuVariantsActivity Created");
        setContentView(R.layout.activity_menu_variants);
        setSupportActionBar((Toolbar) findViewById(R.id.variantsToolbar));
        menu = (Menu) getIntent().getExtras().getSerializable(EXTRA_KEY_MENU);
        ImageView logo = (ImageView) findViewById(R.id.imageView3);
        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getDir("logo", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, "logo.png");
        Picasso.with(this).load(myImageFile).error(R.drawable.yumscorelogoblack).into(logo);
        TextView menuPowered = (TextView) findViewById(R.id.variantsMenuPowered);
        String menuPoweredText = "menu powered by yumscore";
        Spannable spannable = new SpannableString(menuPoweredText);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 16, menuPoweredText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuPowered.setText(spannable);
        menuPowered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutActivity.class);
                setIsAppFlag(getApplicationContext());
                startActivity(intent);
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutActivity.class);
                setIsAppFlag(getApplicationContext());
                startActivity(intent);
            }
        });
        menuVariantsListView = (ListView) findViewById(R.id.menuVariantsListview);
        menuVariantsAdapter = new MenuVariantsAdapter(this, R.layout.menu_variant, menu.getMenuVariants());
        menuVariantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startMenuExpandableActivity(position, menu.getLogo());
            }
        });
        menuVariantsListView.setAdapter(menuVariantsAdapter);
    }

    private void startMenuExpandableActivity(int position, String logo){
        Intent intent = new Intent(this, MenuExpandableActivity.class);
        intent.putExtra(EXTRA_KEY_VARIANT, menuVariantsAdapter.getVariant(position));
        intent.putExtra(EXTRA_KEY_LOGO, logo);
        setIsAppFlag(this);
        startActivity(intent);
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
    public void onBackPressed() {
        // nothing to do here
        // … really
    }

    public static void setIsAppFlag(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(SHARED_PREFERENCES_IS_APP, true).apply();
    }

    public static boolean checkIsAppFlag(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_IS_APP, false);
    }

    public static void clearIsAppFlag(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(SHARED_PREFERENCES_IS_APP).apply();
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
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
