package com.hzhu.wechat.controller;

import com.hzhu.wechat.file.service.MinioService;
import com.hzhu.wechat.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @authod: pp_lan on 2021/10/31.
 */
@RestController
@RequestMapping("/file")
public class FileInfoController {

    @Autowired
    private MinioService minioService;

    @RequestMapping("/list")
    public Response getSWiper() {
        List<String> img = minioService.listFiles("pudding/img");

        return Response.ok(img);
    }
}
