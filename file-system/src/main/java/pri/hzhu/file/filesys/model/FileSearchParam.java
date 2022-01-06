package pri.hzhu.file.filesys.model;

import pri.hzhu.file.common.model.Page;

/**
 * @author: pp_lan
 * @date: 2022/1/3
 */
public class FileSearchParam extends Page {

    private Integer albumId;

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }
}
