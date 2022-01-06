package pri.hzhu.file.minio.service;

import com.jayway.jsonpath.internal.function.numeric.Min;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pri.hzhu.file.common.model.CommonException;
import pri.hzhu.file.common.model.Constants;
import pri.hzhu.file.dict.service.DictService;
import pri.hzhu.file.minio.model.MinioConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @fileName: MinioUtils
 * @version: V1.0
 * @Description: 文件工具
 * @Author: X6879
 * @Date: 2021/10/18 16:46
 */
@Component
public class MinioService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MinioService.class);

    private MinioConfig minioConfig;

    private volatile MinioClient minioClient;

    @Autowired
    private DictService dictService;

    public MinioClient getInstance() {

        if (minioClient == null) {
            synchronized (MinioService.class) {
                if (minioClient == null) {
                    minioConfig = dictService.getMinioInfo();
                    minioClient = MinioClient.builder().endpoint(minioConfig.getEndPoint()).
                            credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
                    makeBucketIfNotExist(minioConfig.getBucketName());
                }
            }
        }
        return minioClient;
    }

    /**
     * 检查文件存储桶是否存在
     *
     * @param bucketName 桶名称
     */
    private boolean bucketExists(String bucketName) {
        BucketExistsArgs name = BucketExistsArgs.builder().bucket(bucketName).build();
        try {
            return minioClient.bucketExists(name);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
    }

    /**
     * 新建文件存储桶
     *
     * @param bucketName 桶名称
     */
    private void makeBucket(String bucketName) {
        MakeBucketArgs name = MakeBucketArgs.builder().bucket(bucketName).build();
        try {
            minioClient.makeBucket(name);
        } catch (Exception e) {
            throw new CommonException("文件存储桶创建失败");
        }
    }

    /**
     * 如果桶不存在则创建
     * @param bucketName
     */
    private void makeBucketIfNotExist(String bucketName) {
        if(!bucketExists(bucketName)) {
            makeBucket(bucketName);
        }
    }

    /**
     * 获取所有的bucket
     * @return
     * @throws Exception
     */
    public List<Bucket> getAllBucket() throws Exception {
        // 获取minio中所以的bucket
        List<Bucket> buckets = getInstance().listBuckets();
        return buckets;
    }

    public String upload(InputStream inputStream, String objectName) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String timeStr = sdf.format(new Date());
            StringBuilder path = new StringBuilder();
            path.append("message").append(Constants.LINE_FLAG).append(timeStr).append(Constants.LINE_FLAG).append(objectName);
            long size = inputStream.available();
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(path.toString())
                    .contentType("application/octet-stream")
                    .stream(inputStream, size, Constants.INT_NEG_1)
                    .build();

            // 上传
            getInstance().putObject(putObjectArgs);

            return new StringBuilder().append(minioConfig.getHttpUrl()).append(Constants.LINE_FLAG)
                    .append(minioConfig.getBucketName()).append(Constants.LINE_FLAG).append(path).toString();

        } catch (Exception e) {
            throw new CommonException("上传失败", e);
        }
    }

    public void remoteDownload(OutputStream os, String url) {
        try (BufferedOutputStream bos = new BufferedOutputStream(os)) {
            URL urlFIle = new URL(url);
            URLConnection httpUrl = urlFIle.openConnection();
            httpUrl.connect();

            try (BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());) {
                int length;
                byte[] buffer = new byte[Constants.INT_1024];

                while ((length = bis.read(buffer)) != Constants.INT_NEG_1) {
                    bos.write(buffer, Constants.INT_0, length);
                }

            } catch (Exception e) {
                throw new CommonException("文件下载异常", e);
            }

        } catch (Exception e) {
            throw new CommonException("文件URL获取异常", e);
        }

    }

    /**
     * 遍历文件
     * @param prefix
     * @return
     */
    public List<String> listFiles(String prefix) {
        List<String> fileList = new ArrayList<>();

        prefix = prefix.endsWith(Constants.LINE_FLAG) ? prefix : prefix + Constants.LINE_FLAG;

        Iterable<Result<Item>> results = getInstance().listObjects(
                ListObjectsArgs.builder().bucket(minioConfig.getBucketName()).prefix(prefix).recursive(false).build());

        for (Iterator<Result<Item>> it = results.iterator(); it.hasNext();) {
            Result<Item> next = it.next();
            Item item = null;
            try {
                item = next.get();
            } catch (Exception e) {
                LOGGER.error("获取文件异常", e);
            }
            if (item.isDir()) {
                continue;
            }

            String fileName = item.objectName();
            fileList.add(new StringBuilder().append(minioConfig.getHttpUrl()).append(Constants.LINE_FLAG).append(minioConfig.getBucketName()).append(Constants.LINE_FLAG).append(fileName).toString());
        }

        return fileList;
    }


    public void deleteAll() {
        try {
            ListObjectsArgs params = ListObjectsArgs.builder().bucket(minioConfig.getBucketName()).build();
            MinioClient instance = getInstance();
            Iterable<Result<Item>> results = instance.listObjects(params);
            List<DeleteObject> deleteList = new ArrayList<>();
            for (Result<Item> result : results) {
                Item item = result.get();
                String name = item.objectName();
                if(name.endsWith(".jpg")) {
                    continue;
                }

                deleteList.add(new DeleteObject(name));
            }

            RemoveObjectsArgs deleteParams = RemoveObjectsArgs.builder().bucket(minioConfig.getBucketName()).objects(deleteList).build();
            Iterable<Result<DeleteError>> deleteResults = instance.removeObjects(deleteParams);

            for (Result<DeleteError> deleteResult : deleteResults) {
                DeleteError error = deleteResult.get();
                LOGGER.error("【删除异常】文件名:{} ", error.objectName() ,error.message());
            }

        } catch (Exception e) {
            LOGGER.error("【删除失败】", e);
        }
    }

    public void delete(String... fileNames) {
        try {
            List<DeleteObject> deleteList = new ArrayList<>();
            for (String fileName : fileNames) {
                deleteList.add(new DeleteObject(fileName));
            }

            RemoveObjectsArgs deleteParams = RemoveObjectsArgs.builder().bucket(minioConfig.getBucketName()).objects(deleteList).build();
            Iterable<Result<DeleteError>> results = getInstance().removeObjects(deleteParams);
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                LOGGER.error("【删除异常】文件名:{} ", error.objectName() ,error.message());
            }

        } catch (Exception e) {
            LOGGER.error("【删除失败】", e);
        }
    }

}
