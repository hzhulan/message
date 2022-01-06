package pri.hzhu.file.dict.model;

/**
 * @author: pp_lan on 2022/1/2.
 */
public enum DictEnum {

    /**
     * 文件服务器 字典
     */
    MINIO("minio");

    private String dictType;

    DictEnum(String dictType) {
        this.dictType = dictType;
    }

    public String getDictType() {
        return dictType;
    }
}
