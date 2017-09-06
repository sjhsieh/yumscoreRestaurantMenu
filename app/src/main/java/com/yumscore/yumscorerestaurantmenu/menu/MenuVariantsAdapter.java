package com.yumscore.yumscorerestaurantmenu.menu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yumscore.yumscorerestaurantmenu.R;

import java.util.List;

/**
 * Created by steve on 5/2/17.
 */

public class MenuVariantsAdapter extends ArrayAdapter {
    private static final String TAG = "MenuVariantsAdapter";

    private final int layoutResource;
    private final LayoutInflater layoutInflator;
    private List<MenuVariant> variants;

    public MenuVariantsAdapter(Context context, int resource, List<MenuVariant> variants) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflator = LayoutInflater.from(context);
        this.variants = variants;
    }

    @Override
    public int getCount() {
        return variants.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView: called");
        ViewHolder viewHolder;
        MenuVariant currentVariant = variants.get(position);
        if(convertView == null){
            convertView = layoutInflator.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.menuVariantName.setText(currentVariant.getName());
        return convertView;
    }

    public MenuVariant getVariant(int position){
        return ((variants != null) && (variants.size() != 0) ? variants.get(position) : null);
    }

    private class ViewHolder {
        final TextView menuVariantName;

        ViewHolder(View v){
            this.menuVariantName = (TextView) v.findViewById(R.id.menuVariantName);
        }
    }
}
