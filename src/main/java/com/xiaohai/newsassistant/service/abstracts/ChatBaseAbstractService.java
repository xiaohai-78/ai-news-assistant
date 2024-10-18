package com.xiaohai.newsassistant.service.abstracts;

import com.alibaba.fastjson.JSON;
import com.xiaohai.newsassistant.enums.ChatModelEnum;
import com.xiaohai.newsassistant.pojo.ArticlesByAiPojo;
import com.xiaohai.newsassistant.service.ChatService;
import com.xiaohai.newsassistant.service.factory.ChatModelFacotry;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
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
    private ChatModelFacotry chatModelFacotry;

    @Override
    public String processChat(ChatModelEnum chatModelEnum, String prompt, String content) {

        log.info("processChat: SystemMessage: {}, UserMessage: {}", prompt, content);

        Prompt request = new Prompt(List.of(
                new SystemMessage(prompt),
                new UserMessage(content)
        ));

        ChatModel chatModel = chatModelFacotry.getChatModelFacotry(chatModelEnum);

        ChatResponse chatResponse = chatModel.call(request);

        String responseString = chatResponse.getResult().getOutput().getContent();

        log.info("response: {}", responseString);

        return responseString;
    }

    @Override
    public ArticlesByAiPojo processConverterChat(ChatModelEnum chatModelEnum, String prompt, String content) {
        Prompt request = new Prompt(List.of(
                new SystemMessage(prompt),
                new UserMessage(content)
        ));
        ChatModel chatModel = chatModelFacotry.getChatModelFacotry(chatModelEnum);
        ArticlesByAiPojo articlesByAiPojo = ChatClient.create(chatModel).prompt()
                .user(content)
                .system(prompt)
                .call()
                .entity(ArticlesByAiPojo.class);
        log.info("response: {}", articlesByAiPojo);

        return articlesByAiPojo;
    }
}
