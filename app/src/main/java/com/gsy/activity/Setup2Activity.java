package com.gsy.activity;

/**
 *
 */
public class Setup2Activity extends BaseSetupActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_set_up2);
    }

    @Override
    protected void nextActivity() {
        startActivity(Setup3Activity.class);
    }

    @Override
    protected void prevActivity() {
        startActivity(Setup1Activity.class);
    }
}
