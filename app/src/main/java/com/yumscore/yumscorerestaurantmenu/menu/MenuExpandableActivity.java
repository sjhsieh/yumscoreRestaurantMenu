package com.yumscore.yumscorerestaurantmenu.menu;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yumscore.yumscorerestaurantmenu.AboutActivity;
import com.yumscore.yumscorerestaurantmenu.R;
import com.yumscore.yumscorerestaurantmenu.pager.ReviewPagerActivity;
import com.yumscore.yumscorerestaurantmenu.request.SendLogVariantViewRequest;

import java.io.File;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static com.yumscore.yumscorerestaurantmenu.startup.SetRestaurantActivity.preventStatusBarExpansion;

public class MenuExpandableActivity extends AppCompatActivity {
    private static final String TAG = "MenuExpandableActivity";

    public static final String EXTRA_KEY_DISH = "dish";

    private ExpandableListView menuListViewExpandable;
    private LinearLayout menuSearchProgressLinearLayout;
    private SearchView menuSearchView;
    private ListView menuSearchListView;
    private MenuSearchListAdapter menuSearchListAdapter;
    private ArrayList<MenuSection> menuSections;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: MenuExpandableActivity Resumed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preventStatusBarExpansion(this);
        MenuVariantsActivity.clearIsAppFlag(this);
        Log.d(TAG, "onCreate: MenuExpandableActivity Created");
        setContentView(R.layout.activity_menu_expandable);
        setSupportActionBar((Toolbar) findViewById(R.id.menuToolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MenuVariant menuVariant = (MenuVariant) getIntent().getExtras().getSerializable(MenuVariantsActivity.EXTRA_KEY_VARIANT);
        menuSections = menuVariant.getSections();
        String logoString = (String) getIntent().getExtras().getString(MenuVariantsActivity.EXTRA_KEY_LOGO);
        LinearLayout menuExpandableLinearLayout = (LinearLayout) findViewById(R.id.menuExpandableLinearLayout);
        menuExpandableLinearLayout.requestFocus();
        SendLogVariantViewRequest sendLogVariantViewRequest = new SendLogVariantViewRequest();
        sendLogVariantViewRequest.execute(menuVariant.getRestaurantId(), menuVariant.getVariantId());
        menuListViewExpandable = (ExpandableListView) findViewById(R.id.menuListViewExpandable);
        setDimens();
        menuListViewExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(v.getContext(), ReviewPagerActivity.class);
                intent.putExtra(EXTRA_KEY_DISH, menuSections.get(groupPosition).getDishes().get(childPosition));
                MenuVariantsActivity.setIsAppFlag(getApplicationContext());
                startActivity(intent);
                return true;
            }
        });
        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getDir("logo", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, "logo.png");
        ImageView logo = (ImageView) findViewById(R.id.imageView2);
        Picasso.with(this).load(myImageFile).error(R.drawable.yumscorelogoblack).into(logo);
        TextView menuPowered = (TextView) findViewById(R.id.menuPowered);
        String menuPoweredText = "menu powered by yumscore";
        Spannable spannable = new SpannableString(menuPoweredText);
        spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 16, menuPoweredText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuPowered.setText(spannable);
        menuPowered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutActivity.class);
                MenuVariantsActivity.setIsAppFlag(getApplicationContext());
                startActivity(intent);
            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutActivity.class);
                MenuVariantsActivity.setIsAppFlag(getApplicationContext());
                startActivity(intent);
            }
        });
        MenuAdapterExpandable menuSectionAdapter = new MenuAdapterExpandable(this, menuSections, menuListViewExpandable);
        menuListViewExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Implement this method to scroll to the correct position as this doesn't
                // happen automatically if we override onGroupExpand() as above
                parent.smoothScrollToPosition(groupPosition);

                // Need default behaviour here otherwise group does not get expanded/collapsed
                // on click
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                } else {
                    parent.expandGroup(groupPosition);
                }

                return true;
            }
        });
        menuListViewExpandable.setAdapter(menuSectionAdapter);
        menuSearchProgressLinearLayout = (LinearLayout) findViewById(R.id.menuSearchProgressLinearLayout);
        menuSearchListView = (ListView) findViewById(R.id.menuSearchListView);
        menuSearchListAdapter = new MenuSearchListAdapter(this, R.layout.menu_dish, new ArrayList<Dish>());
        menuSearchListView.setAdapter(menuSearchListAdapter);
        menuSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ReviewPagerActivity.class);
                intent.putExtra(EXTRA_KEY_DISH, menuSearchListAdapter.getDish(position));
                MenuVariantsActivity.setIsAppFlag(getApplicationContext());
                startActivity(intent);
            }
        });
        menuSearchView = (SearchView) findViewById(R.id.menuSearchView);
        menuSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                if(newText == null || "".equals(newText)){
                    Log.d(TAG, "onQueryTextChange: empty new text");
                    menuSearchListView.setVisibility(View.GONE);
                }else{
                    menuSearchListView.setVisibility(View.VISIBLE);
                    menuSearchProgressLinearLayout.setVisibility(View.VISIBLE);
                    ArrayList<Dish> matchingDishes = new ArrayList<>();
                    for(int i = 0; i < menuSections.size(); i++){
                        MenuSection currentSection = menuSections.get(i);
                        for(int j = 0; j < currentSection.getDishes().size(); j++){
                            Dish currentDish = currentSection.getDishes().get(j);
                            if(currentDish.getName().toLowerCase().contains(newText.toLowerCase())){
                                matchingDishes.add(currentDish);
                            }
                        }
                    }
                    menuSearchListAdapter.loadNewData(matchingDishes);
                    menuSearchProgressLinearLayout.setVisibility(View.GONE);
                }
//                if(emptyList != null){
//                    emptyList.setVisibility(View.INVISIBLE);
//                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        });
    }

    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public void setDimens()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            menuListViewExpandable.setIndicatorBounds(width - GetPixelFromDips(50), width - 0);
        } else {
            menuListViewExpandable.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width);
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
        if(!MenuVariantsActivity.checkIsAppFlag(getApplicationContext())){
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
        if (MenuVariantsActivity.blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}
