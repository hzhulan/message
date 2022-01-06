package pri.hzhu.file.dict.service;

import org.springframework.stereotype.Service;
import pri.hzhu.file.dict.dao.DictMapper;
import pri.hzhu.file.dict.model.DictEnum;
import pri.hzhu.file.dict.model.DictInfo;
import pri.hzhu.file.minio.model.MinioConfig;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: pp_lan on 2022/1/2.
 */
@Service
public class DictService {

    @Resource
    private DictMapper dictMapper;

    /**
     * 获取minio连接信息
     * @return
     */
    public MinioConfig getMinioInfo() {
        List<DictInfo> dictInfos = dictMapper.selectDict(DictEnum.MINIO.getDictType());
        Map<String, String> minioMap = dictInfos.stream().collect(Collectors.toMap(DictInfo::getDictCode, DictInfo::getDictValue));

        MinioConfig instance = MinioConfig.getInstance(minioMap);
        return instance;
    }
}
