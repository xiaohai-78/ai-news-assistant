package com.xiaohai.newsassistant.service;

import com.xiaohai.newsassistant.enums.ChatModelEnum;

/**
 * @Description: ChatService
 * @Author: XiaoYunTao
 * @Date: 2024/9/9
 */
public interface ChatService {

    /**
     * 处理提示词和内容
     * @param prompt 提示词
     * @param content 内容
     * @return 模型处理的结果
     */
    String processChat(ChatModelEnum chatModelEnum, String prompt, String content);
}
