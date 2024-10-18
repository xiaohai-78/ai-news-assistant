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
public class ChatModelFacotry {

    @Resource
    private OllamaChatModel ollamaChatModel;

    public ChatModel getChatModelFacotry(ChatModelEnum chatModelEnum) {
        switch (chatModelEnum) {
            case OLLAMA:
                return ollamaChatModel;
            default:
                return null;
        }

    }
}
