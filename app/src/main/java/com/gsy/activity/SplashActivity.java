package com.gsy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.MeiYinConstant;
import com.gsy.bean.UrlBean;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//192.168.0.106

/**
 * 动画，检测版本更新
 */
public class SplashActivity extends AppCompatActivity {

    public static final String TAG = "SplashActivity";
    private static final int LOADHOMEACTIVITY = 1;              // 加载主界面
    private static final int SHOWDOWNLOADDIALOG = 2;            // 显示是否更新的对话框
    private static final int ERROR_VERSION = 3;
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
                case ERROR_VERSION:
                    switch (msg.arg1) {
                        case 4001:
                            Toast.makeText(getApplicationContext(), "4001没有网络", Toast.LENGTH_SHORT).show();
                            break;
                        case 4003:
                            Toast.makeText(getApplicationContext(), "4003json格式错误", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
                            Toast.makeText(getApplicationContext(), "404找不到资源文件", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                    loadMain();
                    break;
                default:
                    break;
            }
        }
    };
    private UrlBean mUrlBean;
    private long mStartTimeMillis;
    private ProgressBar mPbDownloadProgress;

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
                URL url = null;
                int errorCode = -1;
                mStartTimeMillis = System.currentTimeMillis();
                try {
                    url = new URL("http://192.168.0.105:8080/gsy/version.json");

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
                        mUrlBean = parseJson(jsonString);
                        connection.disconnect();
                        reader.close();
                        Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "urlBean.getVersion:" + mUrlBean.getVersion());
                    } else {
                        errorCode = 404;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    errorCode = 4002;
                    loadMain();
                } catch (IOException e) {
                    errorCode = 4001;
                    e.printStackTrace();
                } catch (JSONException e) {
                    errorCode = 4003;
                    e.printStackTrace();
                } finally {
                    Message msg = Message.obtain();
                    if (errorCode == -1) {
                        msg.what = isNewVersion(mUrlBean);
                    } else {
                        msg.what = ERROR_VERSION;
                        msg.arg1 = errorCode;
                    }
                    long endTime = System.currentTimeMillis();
                    if (endTime - mStartTimeMillis < 3000) {
                        SystemClock.sleep(3000 - (endTime - mStartTimeMillis));
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 在子线程中执行
     *
     * @param urlBean 版本数据
     */
    private int isNewVersion(UrlBean urlBean) {
        int versionCode = urlBean.getVersion();
        if (mVersionCode == versionCode) {
            // 一致，进入主界面
            return LOADHOMEACTIVITY;
        } else {
            // 有新版本
            return SHOWDOWNLOADDIALOG;
        }
    }

    /**
     * 解析获取到的最新版本信息
     *
     * @param jsonString
     */
    private UrlBean parseJson(StringBuilder jsonString) throws JSONException {
        UrlBean urlBean = new UrlBean();
        JSONObject jsonObject = new JSONObject(String.valueOf(jsonString));
        urlBean.setVersion(jsonObject.optInt("version"));
        urlBean.setDesc(jsonObject.optString("desc"));
        urlBean.setUrl(jsonObject.optString("url"));
        return urlBean;
    }

    private void initView() {
        mRlRoot = findViewById(R.id.rl_splash_root);
        mTvSplashVersionName = (TextView) findViewById(R.id.tv_splash_version_name);
        mPbDownloadProgress = (ProgressBar) findViewById(R.id.pb_splash_download_progress);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("提醒")
                .setMessage("是否更新新版本？新版本具有如下特性：" + mUrlBean.getDesc())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "更新apk");
                        downLoadNewApk();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadMain();
                    }
                })
                .setCancelable(false);
        builder.show();
    }

    /**
     * 下载新的apk的方法
     */
    private void downLoadNewApk() {
        HttpUtils utils = new HttpUtils();
        utils.download(mUrlBean.getUrl(), "/sdcard/xx.apk", new RequestCallBack<File>() {

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                mPbDownloadProgress.setVisibility(View.VISIBLE);
                mPbDownloadProgress.setMax((int) total);
                mPbDownloadProgress.setProgress((int) current);
                super.onLoading(total, current, isUploading);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "下载最新版本成功");
                Toast.makeText(getApplicationContext(), "下载最新版本成功", Toast.LENGTH_SHORT).show();

                // 安装apk
                installApk();
                mPbDownloadProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getApplicationContext(), "下载最新版本失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 安装下载的新版本
     */
    private void installApk() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri data = Uri.fromFile(new File("/sdcard/xx.apk"));
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 如果用户取消，则直接进入主界面
        loadMain();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入主界面
     */
    private void loadMain() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }
}
