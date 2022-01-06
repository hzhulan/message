package pri.hzhu.file.common.model;

/**
 * @author: pp_lan
 * @date: 2022/1/2
 */
public class Page {

    private int pageNo = Constants.INT_1;

    private int pageSize = Constants.INT_10;

    private int totalSize;

    /**
     * 分页offset
     * @return
     */
    public int getOffset() {
        return (this.pageNo - Constants.INT_1) * this.pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}
