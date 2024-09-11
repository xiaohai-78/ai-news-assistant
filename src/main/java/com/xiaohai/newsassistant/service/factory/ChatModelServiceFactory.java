package com.xiaohai.newsassistant.service.factory;

import com.xiaohai.newsassistant.enums.ChatModelEnum;
import com.xiaohai.newsassistant.service.ChatService;
import com.xiaohai.newsassistant.service.impl.OllamaChatServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: XiaoYunTao
 * @Date: 2024/9/11
 */
@Component
public class ChatModelServiceFactory {

    @Resource
    private OllamaChatServiceImpl ollamaChatService;

    public ChatService getChatModelService(ChatModelEnum chatModelEnum) {
        switch (chatModelEnum) {
            case OLLAMA:
                return ollamaChatService;
            default:
                return null;
        }

    }
}
