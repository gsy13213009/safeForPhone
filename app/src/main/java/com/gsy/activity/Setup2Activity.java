package com.gsy.activity;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gsy.Myconstant;
import com.gsy.utils.SpTools;

/**
 *
 */
public class Setup2Activity extends BaseSetupActivity {

    private Button mBtBindSim;
    private ImageView mIvBindIc;

    @Override
    public void initView() {
        setContentView(R.layout.activity_set_up2);
        mBtBindSim = (Button) findViewById(R.id.bt_setup_bind_sim);
        mIvBindIc = (ImageView) findViewById(R.id.iv_bind_ic);
        if (!TextUtils.isEmpty(SpTools.getString(getApplicationContext(), Myconstant.SIM_INFO, ""))) {
            mIvBindIc.setImageResource(R.drawable.lock);
        } else {
            mIvBindIc.setImageResource(R.drawable.unlock);
        }
    }

    @Override
    public void next(View v) {
        if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), Myconstant.SIM_INFO, ""))) {
            Toast.makeText(getApplicationContext(), "请先绑定sim卡", Toast.LENGTH_SHORT).show();
            return;
        }
        super.next(v);
    }

    @Override
    protected void nextActivity() {
        startActivity(Setup3Activity.class);
    }

    @Override
    protected void prevActivity() {
        startActivity(Setup1Activity.class);
    }

    @Override
    public void initEvent() {
        // 添加自己的事件
        mBtBindSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), Myconstant.SIM_INFO, ""))) {
                    {
                        //绑定sim卡,存储sim卡信息
                        //获取sim卡信息
                        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String simSerialNumber = tm.getSimSerialNumber();

                        // 保存sim卡信息
                        SpTools.putString(getApplicationContext(), Myconstant.SIM_INFO, simSerialNumber);
                    }

                    {
                        // 切换图标逻辑
                        mIvBindIc.setImageResource(R.drawable.lock);
                    }
                } else {
                    // 已经绑定
                    SpTools.putString(getApplicationContext(), Myconstant.SIM_INFO, "");
                    mIvBindIc.setImageResource(R.drawable.unlock);
                }

            }
        });
        super.initEvent();
    }
}
