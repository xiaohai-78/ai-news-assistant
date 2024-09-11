package com.xiaohai.newsassistant.mp.controller;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;

/**
 * jsapi 演示接口的 controller.
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 * @date 2020-04-25
 */
@AllArgsConstructor
@RestController
@RequestMapping("/wx/jsapi")
public class WxJsapiController {
    private final WxMpService wxService;

    @GetMapping("/getJsapiTicket")
    public String getJsapiTicket(@PathVariable String appid) throws WxErrorException {
        final WxJsapiSignature jsapiSignature = this.wxService.switchoverTo(appid).createJsapiSignature("111");
        System.out.println(jsapiSignature);
        return this.wxService.getJsapiTicket(true);
    }

    @Resource
    WxMpMaterialService wxMpMaterialService;

    private static final String appid = "wxd35893d33ff520e2";

    @GetMapping("/addDraft01")
    public void addDraft01() throws WxErrorException {
        wxMpMaterialService.mediaUpload(WxConsts.XmlMsgType.IMAGE, new File("/Users/xiaoyuntao/Documents/IMG_0003.JPG"));
    }
}
