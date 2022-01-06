package pri.hzhu.file.filesys.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pri.hzhu.file.common.model.Page;
import pri.hzhu.file.common.utils.DateUtils;
import pri.hzhu.file.filesys.dao.FileSystemMapper;
import pri.hzhu.file.filesys.model.FileInfo;
import pri.hzhu.file.minio.service.MinioService;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author: pp_lan
 * @date: 2022/1/2
 */
@Service
public class FileSystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemService.class);

    @Autowired
    private MinioService minioService;

    @Resource
    private FileSystemMapper fileSystemMapper;

    /**
     * 为相册插入照片
     * @param prefix
     * @param albumId
     */
    public void reloadMinioFile(String prefix, Integer albumId) {

        List<String> urlList = minioService.listFiles(prefix);
        Long current = DateUtils.currentSecond();
        List<FileInfo> fileList = urlList.stream().filter(s -> StringUtils.isNotEmpty(s)).
                map(url -> new FileInfo(url, getNameFromPath(url), current, current)).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(urlList)) {
            fileSystemMapper.deleteFiles(albumId);
            fileSystemMapper.insertFiles(albumId, fileList);
            LOGGER.info("更新照片成功");
        } else {
            LOGGER.error("更新照片失败，总数为空");
        }

    }

    private static final String getNameFromPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public List<FileInfo> list(Integer albumId, Page page) {
        return fileSystemMapper.list(albumId, page);
    }

    public int total(Integer albumId) {
        return fileSystemMapper.total(albumId);
    }

}
