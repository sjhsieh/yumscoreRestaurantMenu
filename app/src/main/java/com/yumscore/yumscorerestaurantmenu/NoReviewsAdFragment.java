package com.yumscore.yumscorerestaurantmenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by steve on 5/11/17.
 */

public class NoReviewsAdFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_no_reviews_ad, container, false);
        super.onCreate(savedInstanceState);
        return view;
    }
}
