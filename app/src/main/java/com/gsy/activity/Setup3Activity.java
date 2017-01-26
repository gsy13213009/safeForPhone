package com.gsy.activity;

/**
 *
 */
public class Setup3Activity extends BaseSetupActivity {
    @Override
    public void initView() {
        setContentView(R.layout.activity_set_up3);
    }

    @Override
    protected void nextActivity() {
        startActivity(Setup4Activity.class);
    }

    @Override
    protected void prevActivity() {
        startActivity(Setup2Activity.class);
    }
}
