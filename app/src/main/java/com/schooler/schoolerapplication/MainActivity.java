package com.schooler.schoolerapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private TabLayout tabLayout;
    private View prevTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        initialize();
    }

    private void initialize() {
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
    }

    private View createTab(String title, int iconId) {
        View tab = LayoutInflater.from(this).inflate(R.layout.tab, null);
        ((TextView) tab.findViewById(R.id.tv_title)).setText(title);
        ((ImageView) tab.findViewById(R.id.iv_icon)).setImageDrawable(getResources().getDrawable(iconId));
        return tab;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            final byte[] tagId = tag.getId();
            toHexString(tagId);
        }
    }

    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
                    .append(CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }
}
