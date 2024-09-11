package com.xiaohai.newsassistant.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class WeChatOfficial {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公众号appid
     */
    private String appId;

    /**
     * 公众号appsecret
     */
    private String appSecret;

    /**
     * 文章图片链接
     */
    private String imageUrl;

    /**
     * 文章封面图片ID
     */
    private String mediaId;

    /**
     * 是否启用（0否；1是）
     */
    private Integer enable;

    /**
     * 公众号名称
     */
    private String officialName;

    /**
     * 微信公众号原始ID
     */
    private String originalId;

}

