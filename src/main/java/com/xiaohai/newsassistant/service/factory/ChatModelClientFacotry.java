package com.xiaohai.newsassistant.service.factory;

import com.xiaohai.newsassistant.enums.ChatModelEnum;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: XiaoYunTao
 * @Date: 2024/9/12
 */
@Component
public class ChatModelClientFacotry {

    @Resource
    private OllamaChatModel ollamaChatClient;

    public ChatModel getChatModelClientFacotry(ChatModelEnum chatModelEnum) {
        switch (chatModelEnum) {
            case OLLAMA:
                return ollamaChatClient;
            default:
                return null;
        }

    }
}
