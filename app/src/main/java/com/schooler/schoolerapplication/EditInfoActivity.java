package com.schooler.schoolerapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.schooler.schoolerapplication.datamodel.MyInfo;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EditInfoActivity extends AppCompatActivity {
    private static final String more = "을(를) 입력하세요";

    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        this.type = getIntent().getStringExtra("Type");
        String info = getIntent().getStringExtra("Info");
        init(type, info);

    }

    private void init(String type, String info) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(type + " 수정");
        ((TextView) findViewById(R.id.textView)).setText(type + more);
        ((TextView) findViewById(R.id.et_text)).setText(info);
        ((ImageView)findViewById(R.id.iv_erase)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) findViewById(R.id.et_text)).setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard();
                RealmConfiguration realmConfig = new RealmConfiguration
                        .Builder(getApplicationContext())
                        .deleteRealmIfMigrationNeeded()
                        .build();
                Realm.setDefaultConfiguration(realmConfig);
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                MyInfo myInfo = realm.where(MyInfo.class).findFirst();
                String info = ((TextView) findViewById(R.id.et_text)).getText().toString();
                String typeToUrl = null;
                switch (type){
                    case MyInfo.BIRTHDAY:
                        myInfo.setBirthday(info);
                        typeToUrl = "birthday";
                        break;
                    case MyInfo.SCHOOL:
                        myInfo.setSchool(info);
                        typeToUrl = "school";
                        break;
                    case MyInfo.PHONE:
                        myInfo.setPhone(info);
                        break;
                    case MyInfo.SUBJECT:
                        myInfo.setSubject(info);
                        typeToUrl = "subject";
                        break;
                }
                realm.commitTransaction();
                if(typeToUrl!=null)
                    update(typeToUrl, info, myInfo.getSessionKey());
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void update(String typeToUrl, String info, String token){
        AQuery aq = new AQuery(this);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put(typeToUrl, info);

        aq.ajax("http://iwin247.net:3000/edit/" + typeToUrl, map, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                Log.d("dudco", url);
                Log.d("dudco", status.getCode() + "    " + status.getMessage());
                Log.d("dudco", object.toString());
            }
        });
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((TextView) findViewById(R.id.et_text)).getWindowToken(), 0);
    }

}
