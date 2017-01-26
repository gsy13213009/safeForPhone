package com.gsy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 */
public abstract class BaseSetupActivity extends AppCompatActivity {

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initGesture();
        initEvent();
        initData();
    }

    public void initData() {

    }

    public void initEvent() {

    }

    private void initGesture() {

        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // x轴方向的速度是否满足条件  像素每秒
                if (velocityX > 20) {    // 速度大于400像素每秒,可以滑动
                    float dx = (e2.getX() - e1.getX());// x轴方向的滑动间距
                    if (Math.abs(dx) < 10) {
                        return true;
                    }
                    if (dx < 0) {
                        next(null);
                    } else {
                        prev(null);
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public abstract void initView();

    /**
     * 下一个页面按钮的处理
     */
    public void next(View v) {
        nextActivity();
        nextAnimation();
    }

    /**
     * 下一个界面的动画
     */
    private void nextAnimation() {
        overridePendingTransition(R.anim.next_in,R.anim.next_out);
    }

    /**
     * 共有的跳转activity的方法
     *
     * @param type
     */
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
        prevAnimation();
    }

    private void prevAnimation() {
        overridePendingTransition(R.anim.prev_in,R.anim.prev_out);
    }

    protected abstract void nextActivity();

    protected abstract void prevActivity();
}
