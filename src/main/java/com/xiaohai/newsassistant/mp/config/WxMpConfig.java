package com.xiaohai.newsassistant.mp.config;


import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

/**
 * 微信公众号配置
 *
 */
public class WxMpConfig extends WxMpDefaultConfigImpl
{

    /**
     * 获取token
     *
     * @return
     */
    public String getToken()
    {
        return token;
    }

    /**
     * 设置token
     *
     * @param token
     */
    public void setToken(String token)
    {
        this.token = token;
    }

    /**
     * 获取aesKey
     *
     * @return
     */
    public String getAesKey()
    {
        return aesKey;
    }

    /**
     * 设置aesKey
     *
     * @param aesKey
     */
    public void setAesKey(String aesKey)
    {
        this.aesKey = aesKey;
    }
}
