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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private final OllamaChatModel ollamaChatClient;

    @Resource
    private ChatService chatService;

//    @Resource
//    private SinaNewsServiceImpl sinaNewsService;
//
//    @Resource
//    private CCTVNewsServiceImpl cctvNewsService;

    public ChatController(OllamaChatModel ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    /**
     * 直接对话接口
     * @param chatOllamaDTO
     * @return
     */
    @PostMapping(value = "generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> generate(@RequestBody @Valid ChatOllamaDTO chatOllamaDTO) {
        String call = ollamaChatClient.call(chatOllamaDTO.getMessage());
        System.out.println(call);
        return Map.of("message", call);
    }

    /**
     * 有提示词的对话接口
     * @param chatOllamaDTO
     * @return
     */
    @PostMapping(value = "ollama/translate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
//    @GetMapping(value = "allNews")
//    public Map<String, String> allNews() {
//        String dateStr = DateTimeUtil.getNewsTime();
//        String cctvNewsServiceOriginalNews = cctvNewsService.getOriginalNews(dateStr);
//        List<SinaAiNewsPojo> aiSinaNewsContent = sinaNewsService.getAiSinaNewsContent(dateStr);
//        return Map.of("message", "success", "cctvNewsServiceOriginalNews", cctvNewsServiceOriginalNews, "aiSinaNewsContent", aiSinaNewsContent.toString());
//    }
}
