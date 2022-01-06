package pri.hzhu.file.filesys;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pri.hzhu.file.common.TestCase;
import pri.hzhu.file.minio.service.MinioService;

import java.util.List;

/**
 * @author: pp_lan
 * @date: 2022/1/2
 */
public class MinioTest extends TestCase {

    @Autowired
    private MinioService minioService;

    @Test
    public void testList() {
        List<String> imgList = minioService.listFiles("/pudding/img");
        System.out.println(imgList);
    }
}
