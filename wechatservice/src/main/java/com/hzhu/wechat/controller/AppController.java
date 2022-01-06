package com.hzhu.wechat.controller;

import com.hzhu.wechat.model.Response;
import com.hzhu.wechat.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: App信息获取
 * @author: pp_lan
 * @date: 2021/11/2 16:50
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @RequestMapping("/register")
    public Response register(String userCode, String code) {

        appService.register(userCode, code);
        return Response.ok("");
    }

    @RequestMapping("/logout")
    public Response logout(String userCode) {

        appService.logout(userCode);

        return Response.ok("");
    }

    @RequestMapping("/send")
    public Response send(String userCode) {

        appService.push(userCode, "测试");
        return Response.ok("");
    }
}