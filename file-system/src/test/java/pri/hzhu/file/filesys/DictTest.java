package pri.hzhu.file.filesys;

import org.junit.Test;
import pri.hzhu.file.common.TestCase;
import pri.hzhu.file.minio.model.MinioConfig;
import pri.hzhu.file.dict.service.DictService;

import javax.annotation.Resource;

/**
 * @authod: pp_lan on 2022/1/2.
 */
public class DictTest extends TestCase {

    @Resource
    private DictService dictService;

    @Test
    public void test() {
        MinioConfig minioInfo = dictService.getMinioInfo();
        System.out.println(minioInfo);
    }
}
