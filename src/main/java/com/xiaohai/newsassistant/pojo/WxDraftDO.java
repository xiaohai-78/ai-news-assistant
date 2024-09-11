package com.xiaohai.newsassistant.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class WxDraftDO {

    /**
     * 请替换为实际的标题
     */
    private String title;
    /**
     * 请替换为实际的作者
     */
    private String author;
    /**
     * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前54个字。
     */
    private String digest;
    /**
     * 图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源 "上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。
     */
    private String content;
    /**
     * 图文消息的原文地址，即点击“阅读原文”后的URL
     */
    private String content_source_url;
    /**
     * 图文消息的封面图片素材id（必须是永久MediaID）
     */
    private String thumb_media_id;
    /**
     * Uint32 是否打开评论，0不打开(默认)，1打开
     */
    private Integer need_open_comment;
    /**
     * Uint32 是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     */
    private Integer only_fans_can_comment;

    /*
    article.put("title", "TITLE"); // 请替换为实际的标题
    article.put("author", "AUTHOR"); // 请替换为实际的作者
    article.put("digest", "DIGEST"); // 请替换为实际的摘要
    article.put("content", "CONTENT"); // 请替换为实际的内容
    article.put("content_source_url", "CONTENT_SOURCE_URL"); // 请替换为实际的内容源 URL
    article.put("thumb_media_id", "THUMB_MEDIA_ID"); // 请替换为实际的缩略图媒体 ID
    article.put("need_open_comment", 0); // 是否打开评论，0 表示不打开
    article.put("only_fans_can_comment", 0); // 是否只允许粉丝评论，0 表示不允许

    // 图片裁剪坐标，请根据实际情况替换坐标值
    article.put("pic_crop_235_1", "X1_Y1_X2_Y2");
    article.put("pic_crop_1_1", "X1_Y1_X2_Y2");
    */
}
