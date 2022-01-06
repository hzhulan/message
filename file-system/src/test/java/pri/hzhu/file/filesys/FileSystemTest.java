package pri.hzhu.file.filesys;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pri.hzhu.file.common.TestCase;
import pri.hzhu.file.common.model.Page;
import pri.hzhu.file.filesys.model.FileInfo;
import pri.hzhu.file.filesys.service.FileSystemService;

import java.util.List;

/**
 * @author: pp_lan
 * @date: 2022/1/2
 */
public class FileSystemTest extends TestCase {

    @Autowired
    private FileSystemService fileSystemService;

    @Test
    public void testReloadFile() {
//        fileSystemService.reloadMinioFile("/pudding/img", 1);
        fileSystemService.reloadMinioFile("/pudding/test", 2);
    }

    @Test
    public void testTotal() {
        int total = fileSystemService.total(1);
        System.out.println(total);
    }

    @Test
    public void testList() {
        List<FileInfo> list = fileSystemService.list(1, new Page());
        System.out.println(list);
    }

}
