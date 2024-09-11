package com.xiaohai.newsassistant.service;

import me.chanjar.weixin.common.error.WxErrorException;

/**
 * @Description: 新闻助手service
 * @Author: XiaoYunTao
 * @Date: 2024/9/9
 */
public interface NewsAssistantService {

    /**
     * 执行推送
     * @return
     */
    String execute() throws WxErrorException;

    /**
     * 获取内容
     */
    String getContent();
}
