package com.hzhu.wechat.file;

import com.hzhu.wechat.common.TestCase;
import com.hzhu.wechat.file.service.MinioService;
import io.minio.messages.Bucket;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description:
 * @author: pp_lan
 * @date: 2021/12/31 15:54
 */
public class UploadTest extends TestCase {

    @Autowired
    private MinioService minioService;

    @Test
    public void test01() throws Exception {
        List<Bucket> allBucket = minioService.getAllBucket();
        System.out.println(allBucket);
    }

    @Test
    public void test02() throws Exception {
        List<String> imgList = minioService.listFiles("pudding/img");
        System.out.println(imgList);
    }
}
