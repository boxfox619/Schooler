package com.schooler.schoolerapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.schooler.schoolerapplication.datamodel.MyInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.logging.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SplashActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 200;

    private CallbackManager callbackManager = null;
    private AccessTokenTracker accessTokenTracker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getPermission();
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.NFC) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.NFC, Manifest.permission.INTERNET}, REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.NFC, Manifest.permission.INTERNET}, REQUEST_CODE);
                }
            } else {
                next();
            }
        } else {
            next();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                next();
            } else {
                AlertDialog.Builder ab = new AlertDialog.Builder(SplashActivity.this);
                ab.setMessage("펴미션을 허용하지 않으면 Schooler를 사용하실 수 없습니다!");
                ab.setPositiveButton("확인", null);
                ab.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                ab.show();
            }
        }
    }

    private void next() {
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder(SplashActivity.this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        MyInfo myInfo = realm.where(MyInfo.class).findFirst();
        if (myInfo == null) {
            login();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void login() {
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                Log.i("eNuri", "Current Token : " + currentAccessToken);
            }
        };
        accessTokenTracker.startTracking();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "user_friends");
        loginButton.registerCallback(callbackManager, callback);
        loginButton.animate().translationY(-getResources().getDimension(R.dimen.up_login_button));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            final AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            AQuery aq = new AQuery(getApplicationContext());
            aq.ajax("http://iwin247.net:3000/fb/token?access_token=" + accessToken.getToken(), String.class, new AjaxCallback<String>() {
                @Override
                public void callback(String url, String object, AjaxStatus status) {
                    Log.d("dudco", url);
                    Log.d("dudco", status.getCode() + "    " + status.getMessage());
                    Log.d("dudco", object.toString());
                    try {
                        JSONObject obj = new JSONObject(object.toString());
                        RealmConfiguration realmConfig = new RealmConfiguration
                                .Builder(SplashActivity.this)
                                .deleteRealmIfMigrationNeeded()
                                .build();
                        Realm.setDefaultConfiguration(realmConfig);
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        MyInfo info = realm.createObject(MyInfo.class);
                        info.setName(obj.getString("name"));
                        info.setSessionKey(obj.getString("token"));
                        info.setProfileImage(obj.getString("picture"));
                        realm.commitTransaction();
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };
}
