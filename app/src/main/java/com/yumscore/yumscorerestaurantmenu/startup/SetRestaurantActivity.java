package com.yumscore.yumscorerestaurantmenu.startup;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yumscore.yumscorerestaurantmenu.menu.Menu;
import com.yumscore.yumscorerestaurantmenu.menu.MenuSection;
import com.yumscore.yumscorerestaurantmenu.menu.MenuVariantsActivity;
import com.yumscore.yumscorerestaurantmenu.R;
import com.yumscore.yumscorerestaurantmenu.api.aws.AWSManager;
import com.yumscore.yumscorerestaurantmenu.request.SendAndParseQuickMenuRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SetRestaurantActivity extends AppCompatActivity implements SendAndParseQuickMenuRequest.QuickMenuListener {
    private static final String TAG = "SetRestaurantActivity";

    public final static int REQUEST_CODE = 9999;

    final List<Target> targets = new ArrayList<>();
    public static final String EXTRA_KEY_MENU = "menu";
    public static final String SHARED_PREFERENCES_RESTAURANT_SET = "restaurantSet";
    public static final String SNS_PROTOCOL = "application";

    private Button setRestaurantSelectButton;
    private EditText setRestaurantQuickId;

    private String quickIdUsed;

    @Override
    protected void onResume() {
        super.onResume();
        setRestaurantSelectButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_set_restaurant);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_toolbar);
        setRestaurantSelectButton = (Button) findViewById(R.id.setRestaurantSelectButton);
        setRestaurantQuickId = (EditText) findViewById(R.id.setRestaurantQuickId);
        setRestaurantSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRestaurantSelectButton.setEnabled(false);
                quickIdUsed = setRestaurantQuickId.getText().toString().toLowerCase();
                findMenu(quickIdUsed);
            }
        });
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkDrawOverlayPermission();
        }
    }

    private void findMenu(String quickId){
        SendAndParseQuickMenuRequest sendAndParseQuickMenuRequest = new SendAndParseQuickMenuRequest(this, true);
        sendAndParseQuickMenuRequest.execute(quickId);
    }

    @Override
    public void onQuickMenuFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setRestaurantSelectButton.setEnabled(true);
    }

    @Override
    public void onQuickMenuParsed(Menu menu, String rawMenu) {
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onQuickMenuParsed: firebase token:" + firebaseToken);
        CreatePlatformEndpointResult createPlatformEndpointResult = AWSManager.getSNSClient().createPlatformEndpoint(new CreatePlatformEndpointRequest()
                .withPlatformApplicationArn("arn:aws:sns:us-west-2:483592860808:app/GCM/Yumscore_Menu")
                .withToken(firebaseToken));
        SubscribeResult subscribeResult = AWSManager.getSNSClient().subscribe(menu.getSnsTopic(), SNS_PROTOCOL, createPlatformEndpointResult.getEndpointArn());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.d(TAG, "onQuickMenuParsed: rawMenu: " + rawMenu);
        sharedPreferences.edit().putString(SplashActivity.SHARED_PREFERENCES_QUICKID, quickIdUsed).putString(SplashActivity.SHARED_PREFERENCES_OFFLINE_MENU, rawMenu).putBoolean(SplashActivity.SHARED_PREFERENCES_ISOFFLINE, false).apply();
        Target logoTarget = picassoImageTarget(getApplicationContext(), "logo", "logo.png");
        targets.add(logoTarget);
        Picasso.with(this).load(menu.getLogo()).into(targets.get(0));
        for(int k = 0; k < menu.getMenuVariants().size(); k++){
            for(int i = 0; i < menu.getMenuVariants().get(k).getSections().size(); i++){
                MenuSection currentSection = menu.getMenuVariants().get(k).getSections().get(i);
                for(int j = 0; j < currentSection.getDishes().size(); j++){
                    Target target = picassoImageTarget(getApplicationContext(), "dishThumbnails", currentSection.getDishes().get(j).getDishId() + ".png");
                    targets.add(target);
                    Picasso.with(this).load(currentSection.getDishes().get(j).getPhotoThumbnailUrl()).into(targets.get(targets.size() - 1));
                    Target targetImage = picassoImageTarget(getApplicationContext(), "dishImages", currentSection.getDishes().get(j).getDishId()+".png");
                    targets.add(targetImage);
                    Picasso.with(this).load(currentSection.getDishes().get(j).getPhotoUrl()).into(targets.get(targets.size() - 1));
                }
            }
        }
        sharedPreferences.edit().putBoolean(SHARED_PREFERENCES_RESTAURANT_SET, true).apply();
        Intent intent = new Intent(this, MenuVariantsActivity.class);
        intent.putExtra(EXTRA_KEY_MENU, menu);
        startActivity(intent);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** check if we already  have permission to draw over other apps */
            if (!Settings.canDrawOverlays(this)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        Log.d(TAG, "onActivityResult: got draw permission result");
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
       /** if so check once again if we have permission */
            if (!Settings.canDrawOverlays(this)) {
                checkDrawOverlayPermission();
            }
        }
    }

    public static Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        Log.d(TAG, "picassoImageTarget: path: " + directory.getAbsoluteFile());
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        Log.d(TAG, "run: path: " + myImageFile.getAbsoluteFile());
                        FileOutputStream fos = null;
                        try {
                            Log.d(TAG, "run: starting try block");
                            fos = new FileOutputStream(myImageFile);
                            Log.d(TAG, "run: created filstream");
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            Log.d(TAG, "run: finished compress");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                                Log.d(TAG, "run: closed filestream");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }

    public static void preventStatusBarExpansion(Context context) {
        WindowManager manager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        Activity activity = (Activity)context;
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        int resId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = activity.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        CustomViewGroup view = new CustomViewGroup(context);

        manager.addView(view, localLayoutParams);
    }

    public static class CustomViewGroup extends ViewGroup {

        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("customViewGroup", "**********Intercepted");
            return true;
        }
    }
}
