package com.xiaohai.newsassistant.service.impl;

import com.xiaohai.newsassistant.service.ChatService;
import com.xiaohai.newsassistant.service.abstracts.ChatBaseAbstractService;
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
public class OllamaChatServiceImpl extends ChatBaseAbstractService {
}
