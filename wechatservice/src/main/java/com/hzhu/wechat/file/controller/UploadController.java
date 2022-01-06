package com.hzhu.wechat.file.controller;

import com.hzhu.wechat.model.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description:
 * @author: pp_lan
 * @date: 2021/12/31 15:24
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

    @RequestMapping("/upload")
    public Response upload() {
        return Response.ok(null);
    }
}
