package com.gsy.activity;

/**
 *
 */
public class Setup1Activity extends BaseSetupActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_set_up1);
    }

    @Override
    protected void nextActivity() {
        startActivity(Setup2Activity.class);
        finish();
    }

    @Override
    protected void prevActivity() {

    }
}
