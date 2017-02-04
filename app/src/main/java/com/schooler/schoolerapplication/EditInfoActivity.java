package com.schooler.schoolerapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {

        super.onStop();
    }
}
