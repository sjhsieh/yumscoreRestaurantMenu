package com.yumscore.yumscorerestaurantmenu.menu;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yumscore.yumscorerestaurantmenu.R;
import com.yumscore.yumscorerestaurantmenu.request.SendAndParseReviewCountRequest;
import com.yumscore.yumscorerestaurantmenu.request.SendAndParseYumscoreRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 2/20/17.
 */

public class MenuAdapterExpandable extends BaseExpandableListAdapter {
    private static final String TAG = "MenuAdapterExpandable";

    private Context context;
    private final LayoutInflater layoutInflator;
    private List<MenuSection> menuSections;
    private ExpandableListView expandableListView;
    private int lastExpandedPosition = -1;

    public MenuAdapterExpandable(Context context, ArrayList<MenuSection> menuSections, ExpandableListView expandableListView) {
        this.context = context;
        this.layoutInflator = LayoutInflater.from(context);
        this.menuSections = menuSections;
        this.expandableListView = expandableListView;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.menuSections.get(listPosition).getDishes().get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final MenuAdapterExpandable.MenuDishViewHolder viewHolder;
        final Dish currentDish = menuSections.get(listPosition).getDishes().get(expandedListPosition);
        if(convertView == null){
            convertView = layoutInflator.inflate(R.layout.menu_dish, parent, false);
            viewHolder = new MenuAdapterExpandable.MenuDishViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (MenuAdapterExpandable.MenuDishViewHolder) convertView.getTag();
        }
        viewHolder.menuDishYumscore.setText("- %");
        viewHolder.menuDishNumReviews.setText("- REVIEWS");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        if(sharedPreferences.getBoolean(SHARED_PREFERENCES_ISOFFLINE, false)){
//            Log.d(TAG, "getChildView: loading image in offline mode");
//            ContextWrapper cw = new ContextWrapper(context);
//            File directory = cw.getDir("dishThumbnails", Context.MODE_PRIVATE);
//            File myImageFile = new File(directory, currentDish.getDishId() + ".png");
//            Picasso.with(context).load(myImageFile).error(R.drawable.default_image).placeholder(R.drawable.default_image).into(viewHolder.menuDishPhoto);
//        }else{
            Picasso.with(context).load(currentDish.getPhotoThumbnailUrl()).placeholder(R.drawable.default_image).into(viewHolder.menuDishPhoto, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Log.d(TAG, "getChildView: loading image online mode failed");
                    ContextWrapper cw = new ContextWrapper(context);
                    File directory = cw.getDir("dishThumbnails", Context.MODE_PRIVATE);
                    File myImageFile = new File(directory, currentDish.getDishId() + ".png");
                    Picasso.with(context).load(myImageFile).error(R.drawable.default_image).placeholder(R.drawable.default_image).into(viewHolder.menuDishPhoto);
                }
            });
            //Picasso.with(context).load(currentDish.getPhotoThumbnailUrl()).error(R.drawable.default_image).placeholder(R.drawable.default_image).into(viewHolder.menuDishPhoto);
//        }
        viewHolder.menuDishName.setText(currentDish.getName());
//        if(currentDish.getYumscore() == null || "".equals(currentDish.getYumscore()) || "null".equals(currentDish.getYumscore())){
//            viewHolder.menuDishYumscore.setText("- % RECOMMENDED");
//        }else{
//            viewHolder.menuDishYumscore.setText(currentDish.getYumscore() + "% RECOMMENDED");
//        }
        SendAndParseYumscoreRequest sendAndParseYumscoreRequest = new SendAndParseYumscoreRequest(viewHolder.menuDishYumscore);
        sendAndParseYumscoreRequest.execute(currentDish.getRestaurantId(), currentDish.getDishId());
        SendAndParseReviewCountRequest sendAndParseReviewCountRequest = new SendAndParseReviewCountRequest(viewHolder.menuDishNumReviews);
        sendAndParseReviewCountRequest.execute(currentDish.getRestaurantId(), currentDish.getDishId());
        viewHolder.menuDishPrice.setText(currentDish.getPrice());
        viewHolder.menuDishDescription.setText(currentDish.getDescription());
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.menuSections.get(listPosition).getDishes().size();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
            expandableListView.collapseGroup(lastExpandedPosition);
        }
        super.onGroupExpanded(groupPosition);
        //expandableListView.setSelectedGroup(groupPosition);
        lastExpandedPosition = groupPosition;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.menuSections.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.menuSections.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        MenuAdapterExpandable.MenuSectionViewHolder viewHolder;
        MenuSection menuSection = (MenuSection) getGroup(listPosition);
        if(convertView == null){
            convertView = layoutInflator.inflate(R.layout.menu_section_title, parent, false);
            viewHolder = new MenuAdapterExpandable.MenuSectionViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (MenuAdapterExpandable.MenuSectionViewHolder) convertView.getTag();
        }
        viewHolder.menuSectionTitle.setText(menuSection.getName());
        if(menuSection.getSectionSubtext() == null || menuSection.getSectionSubtext().equals("") || menuSection.getSectionSubtext().equals("null")){
            viewHolder.menuSectionSubtext.setVisibility(View.GONE);
        }else{
            viewHolder.menuSectionSubtext.setVisibility(View.VISIBLE);
            viewHolder.menuSectionSubtext.setText(menuSection.getSectionSubtext());
        }
        //ExpandableListView expandableListView = (ExpandableListView) parent;
        //expandableListView.expandGroup(listPosition);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    private class MenuSectionViewHolder {
        final TextView menuSectionTitle;
        final TextView menuSectionSubtext;

        MenuSectionViewHolder(View v){
            this.menuSectionTitle = (TextView) v.findViewById(R.id.menuSectionTitle);
            this.menuSectionSubtext = (TextView) v.findViewById(R.id.menuSectionSubtext);
        }
    }

    private class MenuDishViewHolder {
        final ImageView menuDishPhoto;
        final TextView menuDishName;
        final TextView menuDishYumscore;
        final TextView menuDishNumReviews;
        final TextView menuDishPrice;
        final TextView menuDishDescription;

        MenuDishViewHolder(View v){
            this.menuDishPhoto = (ImageView) v.findViewById(R.id.menuDishPhoto);
            this.menuDishName = (TextView) v.findViewById(R.id.menuDishName);
            this.menuDishYumscore = (TextView) v.findViewById(R.id.menuDishYumscore);
            this.menuDishNumReviews = (TextView) v.findViewById(R.id.menuDishNumReviews);
            this.menuDishPrice = (TextView) v.findViewById(R.id.menuDishPrice);
            this.menuDishDescription = (TextView) v.findViewById(R.id.menuDishDescription);
        }
    }
}
