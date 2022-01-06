package com.hzhu.wechat.utils;

import com.hzhu.wechat.model.CommonException;
import com.hzhu.wechat.model.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @fileName: HttpUtils
 * @version: V1.0
 * @Description: http请求
 * @Author: X6879
 * @Date: 2021/9/28 20:30
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private static final String CHARSET = "UTF-8";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PUT = "PUT";
    private static final String HTTP = "http";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";

    public static String get(String url) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpGet.setConfig(requestConfig);
            httpGet.setConfig(requestConfig);
            httpGet.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            httpGet.setHeader("Content-Charset", CHARSET);
            httpGet.setHeader("Accept-Charset", CHARSET);


            response = httpClient.execute(httpGet);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * post方法访问url
     */
    public static String sendPost(String url, String paramJson, Map<String, String> headers, int socketTimeOut, int connTimeOut) {
        HttpPost postMethod = new HttpPost(url);


        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut).setConnectTimeout(Constants.INT_6000)
                .setConnectionRequestTimeout(connTimeOut).build();
        postMethod.setConfig(requestConfig);

        postMethod.setHeader("Content-Charset", CHARSET);
        postMethod.setHeader(CONTENT_TYPE, APPLICATION_JSON);
        postMethod.setHeader("Accept-Charset", CHARSET);


        if (headers != null && headers.size() > Constants.INT_0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                postMethod.addHeader(name, value);
            }
        }

        postMethod.setEntity(new StringEntity(paramJson, CHARSET));

        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(postMethod);

            HttpEntity entity = response.getEntity();

            String content = null;
            if (entity != null) {
                content = EntityUtils.toString(entity, CHARSET);
                EntityUtils.consume(entity);
            }

            return content;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new CommonException("http请求异常");
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("http request error!", e);
                }
            }

            postMethod.releaseConnection();

            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    LOGGER.error("httpClient close error!", e);
                }
            }
        }
    }

    public static String toGetUrl(String url, Map<String, Object> params) {
        StringBuilder urlSb = new StringBuilder();

        urlSb.append(url).append("?");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            urlSb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        urlSb.deleteCharAt(urlSb.length() - 1);

        return urlSb.toString();
    }
}
