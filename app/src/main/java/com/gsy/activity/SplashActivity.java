package com.gsy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.MeiYinConstant;
import com.gsy.bean.UrlBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//192.168.0.106

/**
 * 动画，检测版本更新
 */
public class SplashActivity extends AppCompatActivity {

    public static final String TAG = "SplashActivity";
    private static final int LOADHOMEACTIVITY = 1;              // 加载主界面
    private static final int SHOWDOWNLOADDIALOG = 2;            // 显示是否更新的对话框
    private View mRlRoot;
    private int mVersionCode;
    private String mVersionName;
    private TextView mTvSplashVersionName;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADHOMEACTIVITY:
                    loadMain();
                    break;
                case SHOWDOWNLOADDIALOG:
                    showUpdateDialog();
                    break;

                default:
                    break;
            }
        }
    };
    private UrlBean mUrlBean;
    private long mStartTimeMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();
        initAnimation();
        checkVersion();
    }

    private void initData() {
        // 获取自己的版本信息
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            mVersionCode = packageInfo.versionCode;
            mVersionName = packageInfo.versionName;
            mTvSplashVersionName.setText(mVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "获取版本信息异常:" + e.toString());
        }
    }

    /**
     * 访问服务器，获取最新版本信息
     */
    private void checkVersion() {
        // 访问服务器 获取数据url
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.0.106:8080/gsy/version.json");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    if (connection.getResponseCode() == 200) {
                        mStartTimeMillis = System.currentTimeMillis();
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = reader.readLine();                    // 读一行
                        StringBuilder jsonString = new StringBuilder();
                        while (line != null) {
                            jsonString.append(line);
                            line = reader.readLine();                       // 继续读取
                        }
                        mUrlBean = parseJson(jsonString);
                        isNewVersion(mUrlBean);
                        connection.disconnect();
                        reader.close();
                        Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "urlBean.getVersion:" + mUrlBean.getVersion());
                    }
                } catch (Exception e) {
                    Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "访问网络，更新新版本信息失败:" + e.toString());
                    Toast.makeText(SplashActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                    loadMain();
                }
            }
        }.start();
    }

    /**
     * 在子线程中执行
     *
     * @param urlBean 版本数据
     */
    private void isNewVersion(UrlBean urlBean) {
        int versionCode = urlBean.getVersion();
        Message msg = Message.obtain();
        if (mVersionCode == versionCode) {
            // 一致，进入主界面
            long endTimeMillis = System.currentTimeMillis();
            if (endTimeMillis - mStartTimeMillis < 3000) {
                SystemClock.sleep(3000 - (endTimeMillis - mStartTimeMillis));
            }
            msg.what = LOADHOMEACTIVITY;
        } else {
            // 有新版本
            msg.what = SHOWDOWNLOADDIALOG;
        }
        mHandler.sendMessage(msg);
    }

    /**
     * 解析获取到的最新版本信息
     *
     * @param jsonString
     */
    private UrlBean parseJson(StringBuilder jsonString) {
        UrlBean urlBean = new UrlBean();
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(jsonString));
            urlBean.setVersion(jsonObject.optInt("version"));
            urlBean.setDesc(jsonObject.optString("desc"));
            urlBean.setUrl(jsonObject.optString("url"));
        } catch (JSONException e) {
            Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "json转换异常:" + e.toString());
            e.printStackTrace();
        }
        return urlBean;
    }

    private void initView() {
        mRlRoot = findViewById(R.id.rl_splash_root);
        mTvSplashVersionName = (TextView) findViewById(R.id.tv_splash_version_name);
    }

    /**
     * 动画
     */
    private void initAnimation() {
        // 创建动画
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f
                // 设置锚点 相对于自己 自己的一半，便是中间
                , Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation ra = new RotateAnimation(0, 360
                , Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        aa.setDuration(3000);
        aa.setFillAfter(true);
        sa.setDuration(3000);
        sa.setFillAfter(true);
        ra.setDuration(3000);
        ra.setFillAfter(true);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(aa);
        animationSet.addAnimation(sa);
        animationSet.addAnimation(ra);
        mRlRoot.setAnimation(animationSet);
    }

    /**
     * 显示是否更新的对话框
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提醒")
                .setMessage("是否更新新版本？新版本具有如下特性："+mUrlBean.getDesc())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "更新apk");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadMain();
                    }
                });
    }

    private void loadMain() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
    }
}
