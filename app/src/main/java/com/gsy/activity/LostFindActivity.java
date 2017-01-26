package com.gsy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gsy.Myconstant;
import com.gsy.utils.SpTools;

/**
 *
 */
public class LostFindActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SpTools.getBoolean(getApplicationContext(), Myconstant.IS_SET_UP,false)) {
            startActivity(new Intent(LostFindActivity.this,Setup1Activity.class));
            finish();
        }
        setContentView(R.layout.activity_lost_find);
        initView();
    }

    private void initView() {

    }
}
