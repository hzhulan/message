package pri.hzhu.file.filesys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import pri.hzhu.file.common.model.Page;
import pri.hzhu.file.common.model.Response;
import pri.hzhu.file.filesys.model.FileInfo;
import pri.hzhu.file.filesys.model.FileSearchParam;
import pri.hzhu.file.filesys.service.FileSystemService;

import java.util.List;

/**
 * @author: pp_lan
 * @date: 2022/1/2
 */
@Controller
@RequestMapping("fileSys")
public class FileSystemController {

    @Autowired
    private FileSystemService fileSystemService;

    @RequestMapping("/list")
    @ResponseBody
    public Response<List<FileInfo>> fileList(FileSearchParam param) {
        Integer albumId = param.getAlbumId();
        if (albumId == null) {
            albumId = 1;
        }
        int total = fileSystemService.total(albumId);
        param.setTotalSize(total);
        return Response.ok(fileSystemService.list(albumId, param), param);
    }
}
