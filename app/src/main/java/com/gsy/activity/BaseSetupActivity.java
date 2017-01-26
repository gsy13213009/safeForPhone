package com.gsy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 *
 */
public abstract class BaseSetupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public abstract void initView();

    /**
     * 下一个页面按钮的处理
     */
    public void next(View v) {
        nextActivity();
        startAnimation();
    }

    public void startActivity(Class type) {
        startActivity(new Intent(this,type));
        finish();
    }

    private void startAnimation() {

    }

    /**
     * 上一个页面按钮的处理
     * @param view
     */
    public void prev(View view) {
        prevActivity();
        startAnimation();
    }

    protected abstract void nextActivity();

    protected abstract void prevActivity();
}
