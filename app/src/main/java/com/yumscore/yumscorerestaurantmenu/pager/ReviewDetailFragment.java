package com.yumscore.yumscorerestaurantmenu.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.yumscore.yumscorerestaurantmenu.DateTool;
import com.yumscore.yumscorerestaurantmenu.R;

/**
 * Created by steve on 2/21/17.
 */

public class ReviewDetailFragment extends Fragment {

    private ImageView reviewDetailPhoto;
    private ImageView reviewDetailProfilePhoto;
    private TextView reviewDetailUsername;
    //private TextView reviewDetailDishName;
    private TextView reviewDetailReviewText;
    private TextView reviewDetailRecommended;
    private TextView reviewDetailDate;
    private Review currentReview;
    private TextView reviewDetailNumViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_review_detail, container, false);
        super.onCreate(savedInstanceState);
        currentReview = (Review) getArguments().getSerializable(ReviewPagerAdapter.BUNDLE_KEY_REVIEW);
        reviewDetailPhoto = (ImageView) view.findViewById(R.id.reviewDetailPhoto);
        Picasso.with(getContext()).load(currentReview.getPhotoUrl()).error(R.drawable.detail_photo_placeholder).placeholder(R.drawable.detail_photo_placeholder).into(reviewDetailPhoto);
        reviewDetailProfilePhoto = (ImageView) view.findViewById(R.id.reviewDetailProfilePhoto);
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(100)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(currentReview.getReviewProfileThumbnailUrl()).fit().transform(transformation).error(R.drawable.profile_photo_placeholder).into(reviewDetailProfilePhoto);
        reviewDetailDate = (TextView) view.findViewById(R.id.reviewDetailDate);
        reviewDetailDate.setText(DateTool.getDateString(currentReview.getCompleted()));
        reviewDetailUsername = (TextView) view.findViewById(R.id.reviewDetailUsername);
        reviewDetailUsername.setText(currentReview.getUsername());
        //reviewDetailDishName = (TextView) view.findViewById(R.id.reviewDetailDishName);
        //reviewDetailDishName.setText(currentReview.getDishName());
        reviewDetailReviewText = (TextView) view.findViewById(R.id.reviewDetailReviewText);
        reviewDetailReviewText.setText(currentReview.getReviewText());
        reviewDetailRecommended = (TextView) view.findViewById(R.id.reviewDetailRecommended);
        if(currentReview.isRecommended()){
            reviewDetailRecommended.setText("RECOMMENDED");
            reviewDetailRecommended.setBackgroundColor(getResources().getColor(R.color.orange));
        }else{
            reviewDetailRecommended.setText("NOT RECOMMENDED");
            reviewDetailRecommended.setBackgroundColor(getResources().getColor(R.color.divider));
        }
        reviewDetailNumViews = (TextView) view.findViewById(R.id.reviewDetailNumViews);
        reviewDetailNumViews.setText(currentReview.getNumViews() + " Views");
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
