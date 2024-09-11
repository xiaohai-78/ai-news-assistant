package com.xiaohai.newsassistant.enums;

import lombok.Getter;

/**
 * @Description: 模型枚举
 * @Author: XiaoYunTao
 * @Date: 2024/9/11
 */
@Getter
public enum ChatModelEnum {

    /**
     * Ollama
     */
    OLLAMA("OLLAMA");

    private final String model;

    ChatModelEnum(String model) {
        this.model = model;
    }

}
