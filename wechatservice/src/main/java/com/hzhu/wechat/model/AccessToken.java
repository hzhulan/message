package com.hzhu.wechat.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @description:
 * @author: pp_lan
 * @date: 2021/11/3 8:41
 */
public class AccessToken {

    @JSONField(name = "access_token")
    private String accessToken;

    @JSONField(name = "expires_in")
    private Integer expiresIn = 0;

    private Long current;

    /**
     * 效性时间，绝对秒
     */
    private Long expireSecond;

    //region setter and getter

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getCurrent() {
        return current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getExpireSecond() {
        return this.current + this.expiresIn;
    }

    //endregion
}
