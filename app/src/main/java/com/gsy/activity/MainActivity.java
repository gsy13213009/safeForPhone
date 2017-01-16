package com.gsy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

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
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private View mRlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initAnimation();
        checkVersion();
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
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        String line = reader.readLine();                    // 读一行
                        StringBuilder jsonString = new StringBuilder();
                        while (line != null) {
                            jsonString.append(line);
                            line = reader.readLine();                       // 继续读取
                        }
                        UrlBean urlBean = parseJson(jsonString);
                        connection.disconnect();
                        reader.close();
                        Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "urlBean.getDesc:"+urlBean.getDesc());
                    }
                } catch (Exception e) {
                    Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "访问网络，更新新版本信息失败:"+e.toString());
                }
            }
        }.start();
    }

    /**
     * 解析获取到的最新版本信息
     * @param jsonString
     */
    private UrlBean parseJson(StringBuilder jsonString) {
        UrlBean urlBean = new UrlBean();
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(jsonString));
            urlBean.setVersion(jsonObject.optString("version"));
            urlBean.setDesc(jsonObject.optString("desc"));
            urlBean.setUrl(jsonObject.optString("url"));
        } catch (JSONException e) {
            Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "json转换异常:"+e.toString());
            e.printStackTrace();
        }
        return urlBean;
    }

    private void initView() {
        mRlRoot = findViewById(R.id.rl_splash_root);
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
        aa.setDuration(1000);
        aa.setFillAfter(true);
        sa.setDuration(1000);
        sa.setFillAfter(true);
        ra.setDuration(1000);
        ra.setFillAfter(true);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(aa);
        animationSet.addAnimation(sa);
        animationSet.addAnimation(ra);
        mRlRoot.setAnimation(animationSet);
    }
}
