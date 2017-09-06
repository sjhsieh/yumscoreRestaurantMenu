package com.yumscore.yumscorerestaurantmenu;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static com.yumscore.yumscorerestaurantmenu.MenuExpandableActivity.EXTRA_KEY_DISH;
import static com.yumscore.yumscorerestaurantmenu.MenuVariantsActivity.SHARED_PREFERENCES_IS_APP;
import static com.yumscore.yumscorerestaurantmenu.MenuVariantsActivity.blockedKeys;
import static com.yumscore.yumscorerestaurantmenu.MenuVariantsActivity.checkIsAppFlag;
import static com.yumscore.yumscorerestaurantmenu.MenuVariantsActivity.clearIsAppFlag;
import static com.yumscore.yumscorerestaurantmenu.SetRestaurantActivity.preventStatusBarExpansion;

/**
 * Created by steve on 4/15/17.
 */

public class ReviewPagerActivity extends BaseActivity implements SendAndParseReviewsForDishRequest.ReviewsForDishListener {
    private static final String TAG = "ReviewPagerActivity";

    private SendAndParseReviewsForDishRequest loadReviewsForDishRequest;

    private ReviewPagerAdapter reviewPagerAdapter;
    private ViewPager reviewsPager;

    private Dish currentDish = null;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preventStatusBarExpansion(this);
        clearIsAppFlag(this);
        setContentView(R.layout.reviews_pager);
        currentDish = (Dish) getIntent().getExtras().getSerializable(EXTRA_KEY_DISH);
        setTitle(currentDish.getName());
        SendLogDishViewRequest sendLogDishViewRequest = new SendLogDishViewRequest();
        sendLogDishViewRequest.execute(currentDish.getRestaurantId(), currentDish.getSectionId(), currentDish.getDishId());
        // Instantiate a ViewPager and a PagerAdapter.
        reviewsPager = (ViewPager) findViewById(R.id.reviewsPager);
        reviewsPager.setOffscreenPageLimit(3);
        loadReviewsForDish(currentDish.getRestaurantId(), currentDish.getDishId());
    }

    private void loadReviewsForDish(String restaurantId, String dishId){
        loadReviewsForDishRequest = new SendAndParseReviewsForDishRequest(this);
        loadReviewsForDishRequest.execute(restaurantId, dishId);
    }

    @Override
    public void onReviewsForDishesFailed() {
        Log.d(TAG, "onReviewsForDishesFailed: review loads failed");
        ArrayList<Review> reviews = new ArrayList<>();
        reviews.add(0, new Review(Review.TYPE_DISH_DETAIL, "none", 0L));
        reviewPagerAdapter = new ReviewPagerAdapter(this.getSupportFragmentManager(), reviews, currentDish, true);
        reviewsPager.setAdapter(reviewPagerAdapter);
        Toast.makeText(this, "Reviews failed to load. No Internet Connection. Please try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReviewsForDishesParsed(ArrayList<Review> reviews, boolean firstPage) {
        if(firstPage){
            Log.d(TAG, "onReviewsForDishesParsed: got first page");
            if(reviews.isEmpty()) {
                reviews.add(new Review(Review.TYPE_NO_REVIEWS_AD, "none", 0L));
            }else{
                Review lastReview = reviews.get(reviews.size() -1);
                reviews.add(new Review(Review.TYPE_DOWNLOAD_AD, lastReview.getUsertoken(), lastReview.getCompleted()));
            }
            reviews.add(0, new Review(Review.TYPE_DISH_DETAIL, "none", 0L));
            reviewPagerAdapter = new ReviewPagerAdapter(this.getSupportFragmentManager(), reviews, currentDish, false);
            reviewsPager.setAdapter(reviewPagerAdapter);
            reviewsPager.addOnPageChangeListener(new ReviewPagerPageChangeListener(this, reviewPagerAdapter, currentDish.getRestaurantId(), currentDish.getDishId()));
        }else{
            Log.d(TAG, "onReviewsForDishesParsed: got next page");
            if(!reviews.isEmpty()){
                Log.d(TAG, "onReviewsForDishesParsed: non empty non-first page");
                if(reviewPagerAdapter != null){
                    Review lastReview = reviewPagerAdapter.getReviewAtPosition(reviewPagerAdapter.getCount() - 1);
                    reviews.add(new Review(Review.TYPE_DOWNLOAD_AD, lastReview.getUsertoken(), lastReview.getCompleted()));
                    reviewPagerAdapter.addNewData(reviews);
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadReviewsForDishRequest != null){
            loadReviewsForDishRequest.cancel(true);
        }
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
}
