package pri.hzhu.file.filesys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pri.hzhu.file.common.model.Constants;

/**
 * @author: pp_lan on 2020/7/11.
 */
@RequestMapping("/")
@Controller
public class IndexController {

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/album")
    public ModelAndView album(Integer albumId, ModelAndView modelAndView) {
        albumId = albumId == null ? Constants.INT_1 : albumId;
        modelAndView.addObject("albumId", albumId);
        modelAndView.setViewName("album");
        return modelAndView;
    }

}
