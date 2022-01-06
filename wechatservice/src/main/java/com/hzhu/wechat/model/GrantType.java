package com.hzhu.wechat.model;

/**
 * @description:
 * @author: pp_lan
 * @date: 2021/11/2 20:38
 */
public enum GrantType {

    /**
     * 微信GrantType枚举
     */
    clientCredential("client_credential", "获取token使用");

    private String value;

    private String desc;

    GrantType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
