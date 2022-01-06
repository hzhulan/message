package com.hzhu.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @authod: pp_lan on 2020/7/11.
 */
@RequestMapping("/")
@Controller
public class IndexController {

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("pic.html");
        return modelAndView;
    }

}
