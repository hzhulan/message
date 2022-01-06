package pri.hzhu.file.dict.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pri.hzhu.file.dict.model.DictInfo;

import java.util.List;

/**
 * @author: pp_lan on 2022/1/2.
 */
@Mapper
public interface DictMapper {

    /**
     * 查询字典信息
     * @param dictType 字典类型
     * @return
     */
    List<DictInfo> selectDict(@Param("dictType") String dictType);
}
