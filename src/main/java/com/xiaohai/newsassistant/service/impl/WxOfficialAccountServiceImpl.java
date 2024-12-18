package com.xiaohai.newsassistant.service.impl;

import cn.hutool.json.JSONObject;
import com.xiaohai.newsassistant.mp.service.WxBaseService;
import com.xiaohai.newsassistant.service.NewsAssistantService;
import com.xiaohai.newsassistant.service.impl.news.CCTVNewsServiceImpl;
import com.xiaohai.newsassistant.service.impl.news.SinaNewsServiceImpl;
import com.xiaohai.newsassistant.utils.DateTimeUtil;
import com.xiaohai.newsassistant.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: 微信公众号新闻助手
 * @Author: XiaoYunTao
 * @Date: 2024/9/9
 */
@Slf4j
@Service
public class WxOfficialAccountServiceImpl implements NewsAssistantService {

    @Resource
    private SinaNewsServiceImpl sinaNewsService;

    @Resource
    private CCTVNewsServiceImpl cctvNewsService;

    @Resource
    WxBaseService wxBaseService;

    /**
     * 封面图片
     */
    private static String thumbMediaId = "ec0_mO20KC7z0oRLz9NBRWHnWxZpNzZmHgSAN3hH1Z2NGYJDYNdm6cXTGA4mF1rp";


    @Override
    public String execute() throws WxErrorException {
        // 判断能否正常请求公众号接口
        if (wxBaseService.getWxToken() == null){
            throw new RuntimeException("公众号接口请求失败！请检查IP白名单配置和secret配置,https://mp.weixin.qq.com/cgi-bin/safecenterstatus?action=view&t=setting/safe-index");
        }
        String str = getContent();
        // 发送到草稿箱
        return wxBaseService.addDraft(DateTimeUtil.getWxTitleTime(), str, thumbMediaId);
    }

    @Override
    public String getContent() {

        String str = "";

        // 获取需要获取新闻的日期
        String dateStr = DateTimeUtil.getNewsTime();
        log.info("获取需要获取新闻的日期：{}", dateStr);

        // 获取当天新闻联播
        String cctvNews = cctvNewsService.getOriginalNews(dateStr);
        log.info(" ======= cctvNews ====== {}", cctvNews);

        String formatAiNewsContent = sinaNewsService.getFormatAiNewsContent(dateStr);

        String everydayEngilsh = getEverydayEngilsh();

        str = str + generateHtmlFromCCTVNews(cctvNews) + " <hr> " + " <br/> "
                + formatAiNewsContent
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

    public String generateHtmlFromCCTVNews(String dateStr) {

        String[] newsItems = dateStr.split("(?<=；|\\n)");

        // 开始拼接 HTML
        StringBuilder htmlBuilder = new StringBuilder();

        // 遍历每一条新闻，生成 HTML 列表项
        for (String newsItem : newsItems) {
            htmlBuilder.append("<p>").append(escapeHtml(newsItem.trim())).append("</p>");
        }

        // 返回生成的 HTML 字符串
        return htmlBuilder.toString();
    }

    /**
     * 转义HTML特殊字符（如<、>、&等）
     */
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}
