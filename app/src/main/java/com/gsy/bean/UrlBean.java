package com.gsy.bean;

/**
 * 版本更新，网络解析的数据类
 */
public class UrlBean {
    private int version;             // 版本号
    private String desc;                // 新版本描述信息
    private String url;                 // 新版本的地址


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
