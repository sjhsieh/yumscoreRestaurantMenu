package com.yumscore.yumscorerestaurantmenu;

import android.support.v4.view.ViewPager;
import android.util.Log;

/**
 * Created by steve on 2/28/17.
 */

public class ReviewPagerPageChangeListener implements ViewPager.OnPageChangeListener{
    private static final String TAG = "ReviewPagerPageChangeLi";

    private int visibleThreshold = 2;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;

    private SendAndParseReviewsForDishRequest.ReviewsForDishListener getReviewsListener;
    private ReviewPagerAdapter reviewPagerAdapter;
    private String restaurantId;
    private String dishId;

    public ReviewPagerPageChangeListener(SendAndParseReviewsForDishRequest.ReviewsForDishListener getReviewsListener, ReviewPagerAdapter reviewPagerAdapter, String restaurantId, String dishId) {
        this.getReviewsListener = getReviewsListener;
        this.reviewPagerAdapter = reviewPagerAdapter;
        this.restaurantId = restaurantId;
        this.dishId = dishId;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Review currentReview = reviewPagerAdapter.getReviewAtPosition(position);
        if(Review.TYPE_REVIEW == currentReview.getType()){
            SendLogReviewViewRequest sendLogReviewViewRequest = new SendLogReviewViewRequest();
            sendLogReviewViewRequest.execute(currentReview.getUsertoken(), currentReview.getReviewId());
        }
        if (loading) {
            if (reviewPagerAdapter.getCount() > previousTotal) {
                loading = false;
                previousTotal = reviewPagerAdapter.getCount();
                currentPage++;
            }
        }
        if (!loading && position >= previousTotal - 2) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
            Review lastReview = reviewPagerAdapter.getReviewAtPosition(previousTotal - 1);
            new SendAndParseReviewsForDishRequest(getReviewsListener).execute(restaurantId, dishId, lastReview.getUsertoken(), Long.toString(lastReview.getCompleted()));
            loading = true;
            //socialFeedProgressLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
