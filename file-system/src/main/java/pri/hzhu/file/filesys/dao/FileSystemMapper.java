package pri.hzhu.file.filesys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pri.hzhu.file.common.model.Page;
import pri.hzhu.file.filesys.model.FileInfo;

import java.util.List;

/**
 * @author: pp_lan
 * @date: 2022/1/2
 */
@Mapper
public interface FileSystemMapper {

    /**
     * 插入文件
     * @param albumId
     * @param fileList
     */
    void insertFiles(@Param("albumId") Integer albumId, @Param("fileList") List<FileInfo> fileList);

    /**
     * 单个文件插入
     * @param albumId
     * @param file
     */
    void insertFile(@Param("albumId") Integer albumId, @Param("file") FileInfo file);

    /**
     * 删除照片
     * @param albumId
     */
    void deleteFiles(@Param("albumId") Integer albumId);

    List<FileInfo> list(@Param("albumId") Integer albumId, @Param("page") Page page);

    int total(@Param("albumId") Integer albumId);
}
