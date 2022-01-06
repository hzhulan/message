package pri.hzhu.file.filesys.model;

/**
 * @author: pp_lan
 * @date: 2022/1/2
 */
public class FileInfo {

    /**
     * 访问链接
     */
    private String url;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 照片生成时间
     */
    private Long createTime;

    /**
     * 照片入库时间
     */
    private Long insertTime;

    public FileInfo() {
    }

    public FileInfo(String url, String fileName, Long createTime, Long insertTime) {
        this.url = url;
        this.fileName = fileName;
        this.createTime = createTime;
        this.insertTime = insertTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Long insertTime) {
        this.insertTime = insertTime;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", createTime=" + createTime +
                ", insertTime=" + insertTime +
                '}';
    }
}
