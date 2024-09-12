package com.xiaohai.newsassistant.service.abstracts;

import com.xiaohai.newsassistant.enums.ChatModelEnum;
import com.xiaohai.newsassistant.service.ChatService;
import com.xiaohai.newsassistant.service.factory.ChatModelClientFacotry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: XiaoYunTao
 * @Date: 2024/9/10
 */
@Slf4j
public abstract class ChatBaseAbstractService implements ChatService {

    @Resource
    private ChatModelClientFacotry chatModelClientFacotry;

    @Override
    public String processChat(ChatModelEnum chatModelEnum, String prompt, String content) {

        log.info("processChat: SystemMessage: {}, UserMessage: {}", prompt, content);

        Prompt request = new Prompt(List.of(
                new SystemMessage(prompt),
                new UserMessage(content)
        ));

        ChatModel chatClient = chatModelClientFacotry.getChatModelClientFacotry(chatModelEnum);

        ChatResponse chatResponse = chatClient.call(request);

        String responseString = chatResponse.getResult().getOutput().getContent();

        log.info("response: {}", responseString);

        return responseString;
    }
}
