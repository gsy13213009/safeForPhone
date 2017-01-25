package com.gsy.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.MeiYinConstant;
import com.gsy.Myconstant;
import com.gsy.utils.Md5Utils;
import com.gsy.utils.SpTools;

/**
 * 主界面
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private GridView mGvHomeMenus;
    private MyAdapter mMyAdapter;

    private int[] icons = {R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
            R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings};
    private String[] names = {"手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "病毒查杀"
            , "缓存清理", "高级工具", "设置中心"};
    private Dialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
        initEvent();
    }

    /**
     * 初始化组件的事件
     */
    private void initEvent() {
        mGvHomeMenus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:// 手机防盗，自定义对话框

                        if (TextUtils.isEmpty(SpTools.getString(HomeActivity.this
                                , Myconstant.PASSWORD, ""))) {
                            // 设置密码对话框
                            showSettingPassDialog();
                        } else {
                            // 输入密码对话框
                            showEnterPassDialog();
                        }
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                    case 7:

                        break;
                    case 8:

                        break;
                    case 9:

                        break;

                    default:
                        break;
                }
            }
        });
    }

    /**
     * 显示自定义输入密码的对话框
     */
    private void showEnterPassDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_enter_password, null);
        final EditText enterPass = (EditText) view.findViewById(R.id.et_dialog_enter_password);
        Button btCancel = (Button) view.findViewById(R.id.bt_dialog_enter_password_cancel);
        Button btCofirm = (Button) view.findViewById(R.id.bt_dialog_enter_password_confirm);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        btCofirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etEnterStr = enterPass.getText().toString().trim();
                if (TextUtils.isEmpty(etEnterStr)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    // 密码的判断
                    etEnterStr = Md5Utils.md5(Md5Utils.md5(etEnterStr));
                    if (etEnterStr.equals(SpTools.getString(HomeActivity.this,Myconstant.PASSWORD,""))) {

                    } else {
                        Toast.makeText(getApplicationContext(), "密码不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mDialog.dismiss();
                }
            }
        });
        builder.setView(view);
        mDialog = builder.create();
        mDialog.show();
    }

    private void showSettingPassDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(getApplicationContext(), R.layout.dialog_setting_password, null);
        final EditText etOne = (EditText) view.findViewById(R.id.et_dialog_setting_password_one);
        final EditText etTwo = (EditText) view.findViewById(R.id.et_dialog_setting_password_two);
        Button btCancel = (Button) view.findViewById(R.id.bt_dialog_setting_password_cancel);
        Button btCofirm = (Button) view.findViewById(R.id.bt_dialog_setting_password_confirm);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        btCofirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etOneStr = etOne.getText().toString().trim();
                String etTwoStr = etTwo.getText().toString().trim();
                if (TextUtils.isEmpty(etOneStr) || TextUtils.isEmpty(etTwoStr)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!etOneStr.equals(etTwoStr)) {
                    Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    // 两次md5加密
                    String strMdPassword = Md5Utils.md5(Md5Utils.md5(etOneStr));
                    // 保存密码到sp中
                    Log.d(MeiYinConstant.TAG, "[" + TAG + "] " + "Md5密码:" + strMdPassword);
                    SpTools.putString(HomeActivity.this, Myconstant.PASSWORD, strMdPassword);
                    mDialog.dismiss();
                }
            }
        });
        builder.setView(view);
        mDialog = builder.create();
        mDialog.show();
    }

    private void initData() {
        mMyAdapter = new MyAdapter();
        mGvHomeMenus.setAdapter(mMyAdapter);
    }

    private void initView() {
        mGvHomeMenus = (GridView) findViewById(R.id.gv_home_menus);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return icons.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_home_gridview, null);
            TextView tvItemHomeGvName = (TextView) view.findViewById(R.id.tv_item_home_gv_name);
            ImageView ivItemHomeGvIc = (ImageView) view.findViewById(R.id.iv_item_home_gv_ic);

            ivItemHomeGvIc.setImageResource(icons[position]);
            tvItemHomeGvName.setText(names[position]);

            return view;
        }
    }
}
