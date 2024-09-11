package com.xiaohai.newsassistant.enums;

import com.xiaohai.newsassistant.config.PromptManager;

/**
 * @Description: 提示词
 * @Author: XiaoYunTao
 * @Date: 2024/9/9
 */
public enum PromptEnum {
    TRANSLATE("prompt.translate"),

    NEWS_ASSISTANT("prompt.news_assistant");


    private String prompt;

    PromptEnum(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return PromptManager.getPrompt(prompt);
    }
}
