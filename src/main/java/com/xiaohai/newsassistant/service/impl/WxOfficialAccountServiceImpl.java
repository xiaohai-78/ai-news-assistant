package com.xiaohai.newsassistant.service.impl;

import cn.hutool.json.JSONObject;
import com.xiaohai.newsassistant.help.CCTVNewsHelp;
import com.xiaohai.newsassistant.help.SinaNewsHelp;
import com.xiaohai.newsassistant.help.WxHelp;
import com.xiaohai.newsassistant.pojo.SinaAiNewsPojo;
import com.xiaohai.newsassistant.service.NewsAssistantService;
import com.xiaohai.newsassistant.service.impl.news.CCTVNewsServiceImpl;
import com.xiaohai.newsassistant.service.impl.news.SinaNewsServiceImpl;
import com.xiaohai.newsassistant.utils.DateTimeUtil;
import com.xiaohai.newsassistant.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 微信公众号新闻助手
 * @Author: XiaoYunTao
 * @Date: 2024/9/9
 */
@Slf4j
@Service
public class WxOfficialAccountServiceImpl implements NewsAssistantService {

    @Resource
    private WxHelp wxHelp;

    @Resource
    private SinaNewsServiceImpl sinaNewsService;

    @Resource
    private CCTVNewsServiceImpl cctvNewsService;
    /**
     * 封面图片
     */
    private static String thumbMediaId = "ec0_mO20KC7z0oRLz9NBRWHnWxZpNzZmHgSAN3hH1Z2NGYJDYNdm6cXTGA4mF1rp";


    @Override
    public String execute() throws WxErrorException {
        // 判断能否正常请求公众号接口
        if (WxHelp.getWxToken() == null){
            throw new RuntimeException("公众号接口请求失败！请检查IP白名单配置,https://mp.weixin.qq.com/cgi-bin/safecenterstatus?action=view&t=setting/safe-index");
        }

        // 获取内容
        String str = getContent();

        // 发送到草稿箱
        return wxHelp.addDraft(DateTimeUtil.getWxTitleTime(), str, thumbMediaId);
    }

    @Override
    public String getContent() {

        String str = "";

        // 获取需要获取新闻的日期
        String dateStr = DateTimeUtil.getNewsTime();
        log.info("获取需要获取新闻的日期：{}", dateStr);

        // 获取当天新闻联播
        String cctvNews = cctvNewsService.getOriginalNews(dateStr);
        log.info(" ======= cctvNews ======");
        log.info(cctvNews);

        String formatAiNewsContent = sinaNewsService.getFormatAiNewsContent(dateStr);
//        log.info(" ======= sinaAiNews ======");
//        log.info(sinaAiNewsPojo.toString());

        String everydayEngilsh = getEverydayEngilsh();

        str = str + cctvNews + " <br/> " + " <hr> " + " <br/> " + formatAiNewsContent
                + everydayEngilsh;

        log.info("str: {}", str);
        return str;
    }

    /**
     * 每日英语
     * @return
     */
    private static String getEverydayEngilsh(){
        String response = OkHttpUtil.get("https://apis.tianapi.com/everyday/index?key=a421609eb9f25ea0b2583dac11c94065");
        JSONObject everydayEngilsh = new JSONObject(response);
        JSONObject result = everydayEngilsh.getJSONObject("result");
        String englishSentence = result.getStr("content");
        String chinaSentence = result.getStr("note");

        return  englishSentence + " <br/> " + chinaSentence;
    }

}
