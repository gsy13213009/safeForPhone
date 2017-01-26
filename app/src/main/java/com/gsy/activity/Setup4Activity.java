package com.gsy.activity;

/**
 *
 */
public class Setup4Activity extends BaseSetupActivity {
    @Override
    public void initView() {
        setContentView(R.layout.activity_set_up4);
    }

    @Override
    protected void nextActivity() {
        startActivity(LostFindActivity.class);
    }

    @Override
    protected void prevActivity() {
        startActivity(Setup3Activity.class);
    }
}
