package com.xiaohai.newsassistant.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

/**
 * @Description: Ai生成的内容
 * @Author: XiaoYunTao
 * @Date: 2024/10/18
 */
@Data
public class ArticlesByAiPojo {

    @JsonProperty(required = true, value = "content")
    @JsonPropertyDescription("文章内容")
    private String content;

    @JsonProperty(required = true, value = "title")
    @JsonPropertyDescription("文章标题")
    private String title;
}
