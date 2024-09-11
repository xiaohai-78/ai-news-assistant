package com.xiaohai.newsassistant.service;

/**
 * @Description: 多个不同新闻的接口
 * @Author: XiaoYunTao
 * @Date: 2024/9/10
 */
public interface NewsService {

    /**
     * 获取源新闻
     * @param link
     * @return
     */
    Object getOriginalNews(String link);
}
