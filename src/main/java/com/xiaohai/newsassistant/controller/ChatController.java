package com.xiaohai.newsassistant.controller;

import com.xiaohai.newsassistant.enums.ChatModelEnum;
import com.xiaohai.newsassistant.enums.PromptEnum;
import com.xiaohai.newsassistant.pojo.SinaAiNewsPojo;
import com.xiaohai.newsassistant.pojo.dto.ChatOllamaDTO;
import com.xiaohai.newsassistant.service.ChatService;
import com.xiaohai.newsassistant.service.impl.news.CCTVNewsServiceImpl;
import com.xiaohai.newsassistant.service.impl.news.SinaNewsServiceImpl;
import com.xiaohai.newsassistant.utils.DateTimeUtil;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
/**
 * @Description:
 * @Author: XiaoYunTao
 * @Date: 2024/9/4
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    private OllamaChatModel ollamaChatClient;

    @Resource
    private ChatService chatService;

    @Resource
    private SinaNewsServiceImpl sinaNewsService;

    @Resource
    private CCTVNewsServiceImpl cctvNewsService;

    /**
     * 直接对话接口
     * @param message
     * @return
     */
    @PostMapping("generate")
    public Map<String, String> generate(@RequestParam(value = "message",defaultValue = "你好，你是谁？") String message) {
        String call = ollamaChatClient.call(message);
        System.out.println(call);
        return Map.of("message", call);
    }

    /**
     * 有提示词的对话接口
     * @param chatOllamaDTO
     * @return
     */
    @PostMapping("ollama/translate")
    public Map<String, String> translate(@RequestBody @Valid ChatOllamaDTO chatOllamaDTO) {
        String call = chatService.processChat(ChatModelEnum.OLLAMA, PromptEnum.TRANSLATE.getPrompt(), chatOllamaDTO.getMessage());
        System.out.println(call);
        return Map.of("message", call);
    }

    /**
     * 生成新闻内容
     * 新闻联播 + 新浪财经新闻
     * @return
     */
    @GetMapping("allNews")
    public Map<String, String> allNews() {
        String dateStr = DateTimeUtil.getNewsTime();
        String cctvNewsServiceOriginalNews = cctvNewsService.getOriginalNews(dateStr);
        List<SinaAiNewsPojo> aiSinaNewsContent = sinaNewsService.getAiSinaNewsContent(dateStr);
        return Map.of("message", "success", "cctvNewsServiceOriginalNews", cctvNewsServiceOriginalNews, "aiSinaNewsContent", aiSinaNewsContent.toString());
    }
}
