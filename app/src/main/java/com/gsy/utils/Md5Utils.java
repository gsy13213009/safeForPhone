package com.gsy.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class Md5Utils {
    public static String md5(String str) {
        MessageDigest md = null;
        StringBuilder strbuilder = new StringBuilder();
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes();
            byte[] digest = md.digest(bytes);
            for (byte b : digest ) {
                int d = b & 0xff;
                String hexStr = Integer.toHexString(d);
                if (hexStr.length() == 1) {
                    hexStr = "0" + hexStr;
                }
                strbuilder.append(hexStr);
            }
            System.out.println(strbuilder.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return strbuilder.toString();
    }
}
