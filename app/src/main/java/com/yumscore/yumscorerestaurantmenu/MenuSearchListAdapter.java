package com.yumscore.yumscorerestaurantmenu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by steve on 4/17/17.
 */

public class MenuSearchListAdapter extends ArrayAdapter {

    private static final String TAG = "UserListAdapter";

    private final int layoutResource;
    private final LayoutInflater layoutInflator;
    private List<Dish> dishes;

    public MenuSearchListAdapter(Context context, int resource, List<Dish> dishes) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflator = LayoutInflater.from(context);
        this.dishes = dishes;
    }

    @Override
    public int getCount() {
        return dishes.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: called");
        ViewHolder viewHolder;
        Dish currentDish = dishes.get(position);
        if(convertView == null){
            convertView = layoutInflator.inflate(R.layout.menu_dish, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.menuDishYumscore.setText("- %");
        viewHolder.menuDishNumReviews.setText("- REVIEWS");
        Picasso.with(getContext()).load(currentDish.getPhotoThumbnailUrl()).error(R.drawable.default_image).placeholder(R.drawable.default_image).into(viewHolder.menuDishPhoto);
        viewHolder.menuDishName.setText(currentDish.getName());
//        if(currentDish.getYumscore() == null || "".equals(currentDish.getYumscore()) || "null".equals(currentDish.getYumscore())){
//            viewHolder.menuDishYumscore.setText("- % RECOMMENDED");
//        }else{
//            viewHolder.menuDishYumscore.setText(currentDish.getYumscore() + "% RECOMMENDED");
//        }
//        viewHolder.menuDishNumReviews.setText(currentDish.getNumReviews() + " REVIEWS");
        SendAndParseYumscoreRequest sendAndParseYumscoreRequest = new SendAndParseYumscoreRequest(viewHolder.menuDishYumscore);
        sendAndParseYumscoreRequest.execute(currentDish.getRestaurantId(), currentDish.getDishId());
        SendAndParseReviewCountRequest sendAndParseReviewCountRequest = new SendAndParseReviewCountRequest(viewHolder.menuDishNumReviews);
        sendAndParseReviewCountRequest.execute(currentDish.getRestaurantId(), currentDish.getDishId());
        viewHolder.menuDishPrice.setText(currentDish.getPrice());
        viewHolder.menuDishDescription.setText(currentDish.getDescription());
        return convertView;
    }

    public Dish getDish(int position){
        return ((dishes != null) && (dishes.size() != 0) ? dishes.get(position) : null);
    }

    void loadNewData(List<Dish> newDishes){
        dishes = newDishes;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        final ImageView menuDishPhoto;
        final TextView menuDishName;
        final TextView menuDishYumscore;
        final TextView menuDishNumReviews;
        final TextView menuDishPrice;
        final TextView menuDishDescription;

        ViewHolder(View v){
            this.menuDishPhoto = (ImageView) v.findViewById(R.id.menuDishPhoto);
            this.menuDishName = (TextView) v.findViewById(R.id.menuDishName);
            this.menuDishYumscore = (TextView) v.findViewById(R.id.menuDishYumscore);
            this.menuDishNumReviews = (TextView) v.findViewById(R.id.menuDishNumReviews);
            this.menuDishPrice = (TextView) v.findViewById(R.id.menuDishPrice);
            this.menuDishDescription = (TextView) v.findViewById(R.id.menuDishDescription);
        }
    }
}
