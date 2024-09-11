package com.xiaohai.newsassistant.mp.service;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpMaterialServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class WxMpMaterial2Service extends WxMpMaterialServiceImpl {

    public WxMpMaterial2Service(WxMpService wxMpService) {
        super(wxMpService);
    }
}
