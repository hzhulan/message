package pri.hzhu.file.minio.model;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import pri.hzhu.file.common.model.CommonException;

import java.util.Map;

/**
 * @author: pp_lan
 * @date: 2022/1/2
 */
public class MinioConfig {

    /**
     * 连接api
     */
    private String endPoint;

    /**
     * 下载预览前缀
     */
    private String httpUrl;

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 桶名称，不需要修改
     */
    private String bucketName = "pudding";

    public MinioConfig() {
    }

    /**
     * map转bean
     * @param params
     * @return
     */
    public static MinioConfig getInstance(Map<String, String> params) {
        MinioConfig minioConfig;
        try {
            minioConfig = JSONObject.toJavaObject((JSON) JSONObject.toJSON(params), MinioConfig.class);
        } catch (Exception e) {
            throw new CommonException("获取配置异常", e);
        }
        return minioConfig;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public String toString() {
        return "MinioConfig{" +
                "endPoint='" + endPoint + '\'' +
                ", httpUrl='" + httpUrl + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }
}
