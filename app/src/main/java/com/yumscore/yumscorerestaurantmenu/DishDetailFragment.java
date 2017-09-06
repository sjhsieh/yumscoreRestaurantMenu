package com.yumscore.yumscorerestaurantmenu;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.yumscore.yumscorerestaurantmenu.R.id.reviewDetailPhoto;
import static com.yumscore.yumscorerestaurantmenu.R.id.reviewDetailProfilePhoto;
import static com.yumscore.yumscorerestaurantmenu.ReviewPagerAdapter.BUNDLE_KEY_DISH;

/**
 * Created by steve on 5/15/17.
 */

public class DishDetailFragment extends Fragment {
    private static final String TAG = "DishDetailFragment";

    private Dish dish;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_dish_detail, container, false);
        super.onCreate(savedInstanceState);
        dish = (Dish) getArguments().getSerializable(BUNDLE_KEY_DISH);
        final ImageView dishPhoto = (ImageView) view.findViewById(R.id.dishDetailPhoto);
        Picasso.with(getContext()).load(dish.getPhotoUrl()).placeholder(R.drawable.default_image).into(dishPhoto, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.d(TAG, "getChildView: loading image online mode failed");
                ContextWrapper cw = new ContextWrapper(getContext());
                File directory = cw.getDir("dishImages", Context.MODE_PRIVATE);
                File myImageFile = new File(directory, dish.getDishId() + ".png");
                Picasso.with(getContext()).load(myImageFile).error(R.drawable.default_image).placeholder(R.drawable.default_image).into(dishPhoto);
            }
        });
        return view;
    }
}
