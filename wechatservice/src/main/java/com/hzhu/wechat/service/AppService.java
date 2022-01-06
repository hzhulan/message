package com.hzhu.wechat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hzhu.wechat.model.AccessToken;
import com.hzhu.wechat.model.Constants;
import com.hzhu.wechat.model.TemplateData;
import com.hzhu.wechat.model.WxMssVo;
import com.hzhu.wechat.utils.DateUtils;
import com.hzhu.wechat.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description:
 * @author: pp_lan
 * @date: 2021/11/2 16:44
 */
@Service
public class AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    @Value("${weChat.appId}")
    private String appId;

    @Value("${weChat.secret}")
    private String secret;

    @Value("${weChat.templateId}")
    private String templateId;

    @Value("${weChat.code2Session.url}")
    private String sessionUrl;

    @Value("${weChat.msg.send.url}")
    private String msgSendUrl;

    @Value("${weChat.token.url}")
    private String tokenUrl;

    @Value("${weChat.getAccessToken.url}")
    private String getAccessTokenUrl;

    @Value("${weChat.code2Session.url}")
    private String codeToSessionUrl;

    /**
     * 读写锁
     */
    private static final ReadWriteLock readerWriteLock = new ReentrantReadWriteLock();

    /**
     * openId映射
     */
    private static final Map<String, String> openIdMapping = new HashMap<>();

    private static final Map<String, AccessToken> cache = new HashMap<>();

    public void register(String userCode, String code) {
        Lock lock = readerWriteLock.writeLock();
        lock.lock();
        try {
            if(openIdMapping.containsKey(userCode)) {
                LOGGER.info("{}", "已经注册，跳过注册");
                return;
            }
            // 未注册过进行注册
            String openId = queryOpenId(code);
            LOGGER.info("{}注册oepnId: {}", userCode, openId);
            if (openIdMapping.containsKey(userCode)) {
                openIdMapping.remove(userCode);
            }
            openIdMapping.put(userCode, openId);
        } finally {
            lock.unlock();
        }
    }

    public void logout(String userCode) {
        Lock lock = readerWriteLock.writeLock();
        lock.lock();
        try {
            openIdMapping.remove(userCode);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取openId
     * @param userCode
     * @return
     */
    public String getOpenId(String userCode) {
        Lock lock = readerWriteLock.readLock();
        lock.lock();
        try {
            return openIdMapping.get(userCode);
        } finally {
            lock.unlock();
        }
    }

    private String queryOpenId(String code) {
        JSONObject params = new JSONObject();
        params.put("appid", appId);
        params.put("secret", secret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");

        String url = HttpUtils.toGetUrl(codeToSessionUrl, params);
        String result = HttpUtils.get(url);
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        return openid;
    }

    public void push(String userCode, String msg) {

        String openId = getOpenId(userCode);

        String url = msgSendUrl + "?access_token=" + getAccessToken();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WxMssVo wxMssVo = new WxMssVo();

        //用户的openid（要发送给那个用户，通常这里应该动态传进来的）
        wxMssVo.setTouser(openId);

        //订阅消息模板id
        wxMssVo.setTemplate_id(templateId);
        wxMssVo.setPage("pages/message/index");

        Map<String, TemplateData> m = new HashMap<>();
        m.put("name1", new TemplateData("进度提醒"));
        m.put("time3", new TemplateData(df.format(new Date())));
        m.put("thing4", new TemplateData(msg));
        wxMssVo.setData(m);

        String result = HttpUtils.sendPost(url, JSONObject.toJSONString(wxMssVo), null, Constants.INT_6000, Constants.INT_6000);
        LOGGER.info("推送结果: {}", result);
    }

    /**
     * 获取token
     * @return
     */
    private String getAccessToken() {
        Lock lock = readerWriteLock.writeLock();
        lock.lock();
        try {
            AccessToken token = cache.get("token");

            // 失效前1min重新获取
            if (token != null && DateUtils.currentSec() + Constants.INT_60 > token.getExpireSecond()) {
                cache.remove("token");
                token = null;
            }

            // 没有token
            if (token == null) {
                Map<String, Object> params = new HashMap<>();
                params.put("appid", appId);
                params.put("secret", secret);
                params.put("grant_type", "client_credential");

                String url = HttpUtils.toGetUrl(tokenUrl, params);
                String body = HttpUtils.get(url);

                JSONObject object = JSON.parseObject(body);
                token = JSONObject.toJavaObject(object, AccessToken.class);
                token.setCurrent(DateUtils.currentSec());
                cache.put("token", token);
                LOGGER.info("{}", "重新获取token");

            } else {
                LOGGER.info("{}", "使用缓存token");
            }

            return token.getAccessToken();

        } finally {
            lock.unlock();
        }
    }
}
