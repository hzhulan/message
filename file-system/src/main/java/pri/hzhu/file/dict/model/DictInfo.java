package pri.hzhu.file.dict.model;

/**
 * @author: pp_lan on 2022/1/2.
 */
public class DictInfo {

    /**
     * 父类型
     */
    private String pType;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * code
     */
    private String dictCode;

    /**
     * value
     */
    private String dictValue;

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }
}
