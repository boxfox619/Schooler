package com.schooler.schoolerapplication;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.schooler.schoolerapplication.datamodel.MyInfo;
import com.schooler.schoolerapplication.datamodel.OtherInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public abstract class NfcActivityWarrper extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
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
            final String nfcKey = toHexString(tagId);
            AlertDialog.Builder ab = new AlertDialog.Builder(NfcActivityWarrper.this);
            ab.setMessage("명함이 인식되었습니다!");
            ab.setPositiveButton("검색", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AQuery aq = new AQuery(NfcActivityWarrper.this);
                    aq.ajax("http://iwin247.net:3000/get/nfc?nfc=" + nfcKey, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            RealmConfiguration realmConfig = new RealmConfiguration
                                    .Builder(NfcActivityWarrper.this)
                                    .deleteRealmIfMigrationNeeded()
                                    .build();
                            Realm.setDefaultConfiguration(realmConfig);
                            Realm realm = Realm.getDefaultInstance();
                            Log.d("dudco", url);
                            Log.d("dudco", status.getCode() + "    " + status.getMessage());
                            Log.d("dudco", object.toString());
                            try {
                                JSONObject obj = new JSONObject(object.toString());
                                if (obj.getString("token").equals(realm.where(MyInfo.class).findFirst().getSessionKey())) {
                                    Toast.makeText(NfcActivityWarrper.this, "자신의 명함은 검색할 수 없습니다!", Toast.LENGTH_SHORT).show();
                                } else {
                                    OtherInfo info = realm.where(OtherInfo.class).contains("nfc", nfcKey).findFirst();
                                    if (info != null) {
                                        Toast.makeText(NfcActivityWarrper.this, info.getName() + "의 명함은 이미 등록되었습니다!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        realm.beginTransaction();
                                        OtherInfo user = realm.createObject(OtherInfo.class);
                                        try {
                                            user.setBirthday(obj.getString("birth"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            user.setProfileImage(obj.getString("picture"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            user.setSchool(obj.getString("school"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            user.setNfc(obj.getString("nfc"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            user.setSubject(obj.getString("subject"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            user.setPhone(obj.getString("phone"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            user.setName(obj.getString("name"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        realm.commitTransaction();
                                        refreshMainContent();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            ab.setNegativeButton("등록", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RealmConfiguration realmConfig = new RealmConfiguration
                            .Builder(NfcActivityWarrper.this)
                            .deleteRealmIfMigrationNeeded()
                            .build();
                    Realm.setDefaultConfiguration(realmConfig);
                    Realm realm = Realm.getDefaultInstance();
                    MyInfo user = realm.where(MyInfo.class).findFirst();
                    AQuery aq = new AQuery(NfcActivityWarrper.this);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("token", user.getSessionKey());
                    map.put("nfc", nfcKey);
                    aq.ajax("http://iwin247.net:3000/edit/nfc", map, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String object, AjaxStatus status) {
                            Log.d("dudco", url);
                            Log.d("dudco", status.getCode() + "    " + status.getMessage());
                            Log.d("dudco", object.toString());
                        }
                    });
                }
            });
            ab.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ab.show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(this).setTitle("종료확인")
                    // .setIcon(R.drawable.warning)
                    .setMessage("종료하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                            finish();
                        }
                    })
                    .setNegativeButton("아니요", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public abstract void refreshMainContent();
}
