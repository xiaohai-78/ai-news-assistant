package com.xiaohai.newsassistant.service.factory;

import com.xiaohai.newsassistant.enums.ChatModelEnum;
import com.xiaohai.newsassistant.service.abstracts.ChatService;
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
    private ChatService chatBaseService;

    public ChatService getChatModelService(ChatModelEnum chatModelEnum) {
        switch (chatModelEnum) {
            case OLLAMA:
                return chatBaseService;
            default:
                return null;
        }

    }
}
