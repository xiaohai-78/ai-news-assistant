package com.xiaohai.newsassistant.mp.service;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpDraftServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class WxMpDraftService2 extends WxMpDraftServiceImpl {

    public WxMpDraftService2(WxMpService mpService) {
        super(mpService);
    }
}
