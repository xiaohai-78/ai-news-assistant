package com.xiaohai.newsassistant.mp.service;

import me.chanjar.weixin.mp.api.WxMpFreePublishService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpFreePublishServiceImpl;
import me.chanjar.weixin.mp.api.impl.WxMpServiceOkHttpImpl;

/**
 * @author xyt
 */
//@Component
public class WxBaseService {

    private static final WxMpService wxMpService = new WxMpServiceOkHttpImpl();

//    @Bean
    public static WxMpFreePublishService getFreePublishService(){
        return new WxMpFreePublishServiceImpl(wxMpService);
    }
}
