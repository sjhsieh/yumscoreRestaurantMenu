package com.yumscore.yumscorerestaurantmenu.pager;

import java.io.Serializable;

/**
 * Created by steve on 2/21/17.
 */

public class Review implements Serializable {

    public static final int TYPE_REVIEW = 0;
    public static final int TYPE_NO_REVIEWS_AD = 1;
    public static final int TYPE_DOWNLOAD_AD = 2;
    public static final int TYPE_DISH_DETAIL = 3;

    private static final long serialVersionUID = 1L;

    private String usertoken;
    private String username;
    private String restaurantId;
    private String restaurantName;
    private Integer yumscore;
    private String reviewId;
    private String photoThumbnailUrl;
    private String photoUrl;
    private String reviewProfileThumbnailUrl;
    private String dishName;
    private String reviewText;
    private boolean recommended;
    private long completed;
    private String firstIcon;
    private String secondIcon;
    private String thirdIcon;
    private String fourthIcon;
    private String fifthIcon;
    private Integer numViews;
    private int type = TYPE_REVIEW;

    public Review(String usertoken, String username, String restaurantId, String restaurantName, Integer yumscore, String reviewId, String photoThumbnailUrl,
                  String photoUrl, String reviewProfileThumbnailUrl, String dishName, String reviewText, boolean recommended, long completed,
                  String firstIcon, String secondIcon, String thirdIcon, String fourthIcon, String fifthIcon, Integer numViews) {
        this.usertoken = usertoken;
        this.username = username;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.yumscore = yumscore;
        this.reviewId = reviewId;
        this.photoThumbnailUrl = photoThumbnailUrl;
        this.photoUrl = photoUrl;
        this.reviewProfileThumbnailUrl = reviewProfileThumbnailUrl;
        this.dishName = dishName;
        this.reviewText = reviewText;
        this.recommended = recommended;
        this.completed = completed;
        this.firstIcon = firstIcon;
        this.secondIcon = secondIcon;
        this.thirdIcon = thirdIcon;
        this.fourthIcon = fourthIcon;
        this.fifthIcon = fifthIcon;
        this.numViews = numViews;
    }

    public Review(int type, String usertoken, long completed){
        this.type = type;
        this.usertoken = usertoken;
        this.completed = completed;
    }

    public String getUsertoken() {
        return usertoken;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Integer getYumscore() {
        return yumscore;
    }

    public void setYumscore(Integer yumscore) {
        this.yumscore = yumscore;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getPhotoThumbnailUrl() {
        return photoThumbnailUrl;
    }

    public void setPhotoThumbnailUrl(String photoThumbnailUrl) {
        this.photoThumbnailUrl = photoThumbnailUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getReviewProfileThumbnailUrl() {
        return reviewProfileThumbnailUrl;
    }

    public void setReviewProfileThumbnailUrl(String reviewProfileThumbnailUrl) {
        this.reviewProfileThumbnailUrl = reviewProfileThumbnailUrl;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public String getFirstIcon() {
        return firstIcon;
    }

    public void setFirstIcon(String firstIcon) {
        this.firstIcon = firstIcon;
    }

    public String getSecondIcon() {
        return secondIcon;
    }

    public void setSecondIcon(String secondIcon) {
        this.secondIcon = secondIcon;
    }

    public String getThirdIcon() {
        return thirdIcon;
    }

    public void setThirdIcon(String thirdIcon) {
        this.thirdIcon = thirdIcon;
    }

    public String getFourthIcon() {
        return fourthIcon;
    }

    public void setFourthIcon(String fourthIcon) {
        this.fourthIcon = fourthIcon;
    }

    public String getFifthIcon() {
        return fifthIcon;
    }

    public void setFifthIcon(String fifthIcon) {
        this.fifthIcon = fifthIcon;
    }

    public Integer getNumViews() {
        return numViews;
    }

    public void setNumViews(Integer numViews) {
        this.numViews = numViews;
    }

    public int getType() {
        return type;
    }
}