package com.gsy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gsy.Myconstant;

/**
 * sp的数据封装类
 */
public class SpTools {
    public static void putString(Context context, String key, String values) {
        SharedPreferences sp = context.getSharedPreferences(Myconstant.SPFILES, Context.MODE_PRIVATE);
        sp.edit().putString(key, values).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(Myconstant.SPFILES, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean values) {
        SharedPreferences sp = context.getSharedPreferences(Myconstant.SPFILES, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, values).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(Myconstant.SPFILES, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }
}
