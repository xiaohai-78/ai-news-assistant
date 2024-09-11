package com.xiaohai.newsassistant.service.abstracts;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.xiaohai.newsassistant.enums.ChatModelEnum;
import com.xiaohai.newsassistant.enums.PromptEnum;
import com.xiaohai.newsassistant.service.factory.ChatModelServiceFactory;
import com.xiaohai.newsassistant.service.ChatService;
import com.xiaohai.newsassistant.service.NewsService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @Description: 新闻抽象类
 * @Author: XiaoYunTao
 * @Date: 2024/9/10
 */
@Slf4j
public abstract class NewsAbstractService implements NewsService {

    @Resource
    private ChatModelServiceFactory chatModelServiceFactory;

    /**
     * 获取JSONObject格式的ai总结的内容
     * @param originalNews 原文
     * @param retryTimes 重试次数，幻觉导致生成的Json格式有问题进行重试
     * @return JSONObject
     */
    protected JSONObject getAiContent(ChatModelEnum chatModelEnum, String originalNews, int retryTimes) {
        JSONObject responseJson = null;
        if(retryTimes > 2) {
            log.error("重试次数已超过{}次, 原文：{}", retryTimes, originalNews);
            return null;
        }
        ChatService chatModelService = chatModelServiceFactory.getChatModelService(chatModelEnum);
        try {
            String response = chatModelService.processChat(PromptEnum.NEWS_ASSISTANT.getPrompt(), originalNews);
            log.info("ChatResponse: " + response.toString());
            responseJson = new JSONObject(response);
        } catch (JSONException exception){
            // 生成Json失败了就重新生成，最多试3次
            log.error("生成Json格式错误");
            responseJson = getAiContent(chatModelEnum, originalNews, ++retryTimes);
        } catch (Exception e){
            log.error("ChatsinaAiNewsPojo error", e);
        }

        return responseJson;
    }
}
