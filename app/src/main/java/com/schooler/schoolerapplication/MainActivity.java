package com.schooler.schoolerapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends NfcActivityWarrper {
    private DetectScrollingScrollView mainScrollView;
    private FloatingActionButton fab;
    private View searchBar;
    private TabLayout tabLayout;
    private View prevTab, bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        settingScrollView();
    }

    private void initialize() {
        bottomNavigation = findViewById(R.id.bottom_nav);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.setTabTextColors(getResources().getColorStateList(R.color.icon));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ((ImageView) view.findViewById(R.id.iv_icon)).setColorFilter(getResources().getColor(R.color.colorPrimary));
                ((TextView) view.findViewById(R.id.tv_title)).setTextColor(getResources().getColor(R.color.colorPrimary));
                if (prevTab != null) {
                    ((ImageView) prevTab.findViewById(R.id.iv_icon)).setColorFilter(getResources().getColor(R.color.icon));
                    ((TextView) prevTab.findViewById(R.id.tv_title)).setTextColor(getResources().getColor(R.color.icon));
                }
                prevTab = view;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTab("Tab 1", R.drawable.ic_more_vert_black_24dp)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTab("Tab 2", R.drawable.ic_more_vert_black_24dp)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTab("Tab 3", R.drawable.ic_more_vert_black_24dp)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(createTab("Tab 4", R.drawable.ic_more_vert_black_24dp)));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void settingScrollView() {
        searchBar = findViewById(R.id.search_bar);
        mainScrollView = (DetectScrollingScrollView) findViewById(R.id.main_scrollview);
        mainScrollView.setHandler(new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DetectScrollingScrollView.UP:
                        show();
                        break;
                    case DetectScrollingScrollView.DOWN:
                        if (msg.arg1 >= searchBar.getHeight()) {
                            hide();
                        }
                        break;
                }
            }
        });
    }

    private void hide(){
        mainScrollView.setHide(true);
        searchBar.animate().translationY(-(searchBar.getHeight()+getResources().getDimension(R.dimen.search_bar_top_margin)));
        bottomNavigation.animate().translationY(tabLayout.getHeight());
        fab.animate().translationY(tabLayout.getHeight());
    }

    private void show(){
        mainScrollView.setHide(false);
        searchBar.animate().translationY(0);
        bottomNavigation.animate().translationY(0);
        fab.animate().translationY(0);
    }

    private View createTab(String title, int iconId) {
        View tab = LayoutInflater.from(this).inflate(R.layout.tab, null);
        ((TextView) tab.findViewById(R.id.tv_title)).setText(title);
        ((ImageView) tab.findViewById(R.id.iv_icon)).setImageDrawable(getResources().getDrawable(iconId));
        return tab;
    }

}
