package com.xiaohai.newsassistant.service.impl;

import com.xiaohai.newsassistant.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: XiaoYunTao
 * @Date: 2024/9/9
 */
@Slf4j
@Service("ollamaChatServiceImpl")
public class OllamaChatServiceImpl implements ChatService {

    @Resource
    private OllamaChatModel ollamaChatClient;

    @Override
    public String processChat(String prompt, String content) {

        log.info("processChat: SystemMessage: {}, UserMessage: {}", prompt, content);

        Prompt request = new Prompt(List.of(
                new SystemMessage(prompt),
                new UserMessage(content)
        ));

        ChatResponse chatResponse = ollamaChatClient.call(request);

        String responseString = chatResponse.getResult().getOutput().getContent();
        log.info("response: " + responseString);
        return responseString;
    }

}
