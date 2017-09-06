package com.yumscore.yumscorerestaurantmenu.pager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.yumscore.yumscorerestaurantmenu.menu.Dish;
import com.yumscore.yumscorerestaurantmenu.menu.DishDetailFragment;

import java.util.List;

/**
 * Created by steve on 2/28/17.
 */

public class ReviewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "ReviewPagerAdapter";

    public static final String BUNDLE_KEY_REVIEW = "review";
    public static final String BUNDLE_KEY_DISH = "dish";
    public static final String BUNDLE_KEY_REVIEWS_FAILED = "reviewsFailed";

    private int viewCount = 0;
    private List<Review> reviews;
    private Dish dish;
    private boolean reviewsFailed;

    public ReviewPagerAdapter(FragmentManager fm, List<Review> reviews, Dish dish, boolean reviewsFailed) {
        super(fm);
        this.reviews = reviews;
        this.dish = dish;
        this.reviewsFailed = reviewsFailed;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: viewing review");
        Review review = reviews.get(position);
        if(review.getType() == Review.TYPE_NO_REVIEWS_AD){
            NoReviewsAdFragment noReviewsAdFragment = new NoReviewsAdFragment();
            return noReviewsAdFragment;
        }else if(review.getType() == Review.TYPE_DOWNLOAD_AD){
            DownloadAdFragment downloadAdFragment = new DownloadAdFragment();
            return downloadAdFragment;
        }else if(review.getType() == Review.TYPE_DISH_DETAIL){
            DishDetailFragment dishDetailFragment = new DishDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(BUNDLE_KEY_DISH, dish);
            arguments.putBoolean(BUNDLE_KEY_REVIEWS_FAILED, reviewsFailed);
            dishDetailFragment.setArguments(arguments);
            return dishDetailFragment;
        }else{
            ReviewDetailFragment reviewDetailFragment = new ReviewDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(BUNDLE_KEY_REVIEW, review);
            reviewDetailFragment.setArguments(arguments);
            return reviewDetailFragment;
        }
    }

    public void addNewData(List<Review> newReviews){
        reviews.addAll(newReviews);
        notifyDataSetChanged();
    }

    public Review getReviewAtPosition(int position){
        return reviews.get(position);
    }

    @Override
    public int getCount() {
        return reviews.size();
    }
}
