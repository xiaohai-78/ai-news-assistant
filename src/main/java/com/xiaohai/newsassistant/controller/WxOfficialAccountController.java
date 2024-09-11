package com.xiaohai.newsassistant.controller;

import com.xiaohai.newsassistant.help.WxHelp;
import com.xiaohai.newsassistant.service.NewsAssistantService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description: 公众号
 * @Author: XiaoYunTao
 * @Date: 2024/9/5
 */
@RequestMapping("/wx")
@ResponseBody
@Slf4j
@RestController
public class WxOfficialAccountController {
    @Resource
    private WxHelp wxHelp;

    @Resource
    private NewsAssistantService newsAssistantService;

    @GetMapping("test")
    public Map<String, String> test() throws WxErrorException {
        wxHelp.test();
        return Map.of("message", "success");
    }

    @GetMapping("addDraft")
    public Map<String, String> addDraft() throws WxErrorException {
        String publish = newsAssistantService.execute();
        return Map.of("message", publish);
    }
}
