package com.xiaohai.newsassistant.service.impl.news;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.xiaohai.newsassistant.enums.ChatModelEnum;
import com.xiaohai.newsassistant.enums.PromptEnum;
import com.xiaohai.newsassistant.pojo.SinaAiNewsPojo;
import com.xiaohai.newsassistant.service.ChatService;
import com.xiaohai.newsassistant.service.abstracts.NewsAbstractService;
import com.xiaohai.newsassistant.utils.OkHttpUtil;
import com.xiaohai.newsassistant.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description: 新浪财经新闻
 * @Author: XiaoYunTao
 * @Date: 2024/9/10
 */
@Slf4j
@Service
public class SinaNewsServiceImpl extends NewsAbstractService {

    @Resource
    private ChatService chatService;

    @Override
    public List<String> getOriginalNews(String date) {
        // 新浪财经
        List<String> sinaUrls = getAllUrl(date);
        List<String> sinaAllNews = new ArrayList<>();
        for (int i = 0; i < sinaUrls.size(); i++) {
            String s = sinaUrls.get(i);
            String sinaTxt = getAbstract(s);
            if (sinaTxt.length() > 100) {
                sinaAllNews.add(sinaTxt);
            }
        }
        return sinaAllNews;
    }

    /**
     * 获取ai新闻内容
     * @param date
     * @return
     */
    public List<SinaAiNewsPojo> getAiSinaNewsContent(String date){
        List<String> sinaNews = getOriginalNews(date);
        List<SinaAiNewsPojo> sinaAiNewsPojos = new ArrayList<>();
        for (int i = 0; i < sinaNews.size(); i++) {
            SinaAiNewsPojo sinaAiNewsPojo = new SinaAiNewsPojo();
            String news = sinaNews.get(i);
            if (StringUtil.isEmpty(news)) {
                continue;
            }
            JSONObject aiContent = getAiContent(ChatModelEnum.OLLAMA, news, 0);
            if (Objects.isNull(aiContent) || aiContent.isEmpty()) {continue;}
            String resp = aiContent.getStr("title") + aiContent.getStr("context");
            if (Objects.isNull(resp) || resp.isEmpty() || resp.length() < 50) {
                continue;
            }
            sinaAiNewsPojo.setTitles(aiContent.getStr("title"));
            sinaAiNewsPojo.setContent(aiContent.getStr("context"));
            sinaAiNewsPojos.add(sinaAiNewsPojo);
        }
        return sinaAiNewsPojos;
    }

    public String getFormatAiNewsContent(String date){
        StringBuilder contentStr = new StringBuilder();
        StringBuilder titleStr = new StringBuilder();
        List<SinaAiNewsPojo> aiSinaNewsContent = getAiSinaNewsContent(date);
        // 公众号HTML的标签
        String starStr = "<h1 style=\"color: #0080ff;\"><strong>";
        String middleStr = "</strong></h1><p>";
        String endStr = "</p>";

        for (int i = 0; i < aiSinaNewsContent.size(); i++) {
            SinaAiNewsPojo sinaAiNewsPojo = aiSinaNewsContent.get(i);
            String title = sinaAiNewsPojo.getTitles();
            String content = sinaAiNewsPojo.getContent();
            if (StringUtil.isEmpty(title) || StringUtil.isEmpty(content)) {
                continue;
            }
            String data = starStr + title + middleStr + content + endStr + "<br>";
            if (Objects.isNull(data) || data.isEmpty() || data.length() < 50) {
                continue;
            }
            contentStr.append(data);
            titleStr.append(title);
        }
        return extractContents(contentStr.toString()) + "<br>" + contentStr;
    }

    public static List<String> getAllUrl(String date) {
        String url = "http://top.finance.sina.com.cn/ws/GetTopDataList.php?top_type=day&top_cat=finance_0_suda&top_time=" + date + "&top_show_num=20&top_order=DESC&js_var=all_1_data&get_new=1";
        log.info("新浪url: {}", url);
        String response = OkHttpUtil.get(url);
        ArrayList<String> urls = new ArrayList<>();
        if (response != null) {
            Document document = Jsoup.parse(response);
            Element body = document.body();
            String bodyStr = body.toString();
            int eq = bodyStr.indexOf("=");
            int em = bodyStr.indexOf(";");
            String substring = bodyStr.substring(eq + 2, em);
            JSONObject entries = new JSONObject(substring);
            JSONArray jsonArray = entries.getJSONArray("data");
            for (Object o : jsonArray) {
                JSONObject data = (JSONObject)o;
                urls.add(data.getStr("url"));
            }
        } else {
            System.out.println("Failed to fetch the response.");
        }
        return urls;
    }

    public static String getAbstract(String link) {
        String response = OkHttpUtil.get(link);
        String replace = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            Document document = Jsoup.parse(response);
            Element abstractElement = document.getElementById("artibody");
            Elements select = abstractElement.select("p");
            for (int i = 0; i < select.size() - 1; i++) {
                stringBuffer.append(select.get(i).text());
            }
            String str = stringBuffer.toString();
            replace = str.replace("　　", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SinaNews - getAbstract-获取新闻简介时发生错误");
            return "";
        }
        return replace;
    }

    private static String extractContents(String text) {
        StringBuffer stringBuffer = new StringBuffer();
        // 使用正则表达式匹配从"start"开始至"end"结束的内容，包括"start"和"end"
        Pattern pattern = Pattern.compile("<h1 style(.*?)</h1>");
        Matcher matcher = pattern.matcher(text);
        int i = 1;
        while (matcher.find()) {
            String content = matcher.group(0); // 使用group(0)来获取整个匹配
            stringBuffer.append(content);
        }

        return stringBuffer.toString();
    }

//
//    /**
//     * 获取JSONObject格式的ai总结的内容
//     * @param originalNews 原文
//     * @param retryTimes 重试次数，幻觉导致生成的Json格式有问题进行重试
//     * @return JSONObject
//     */
//    private JSONObject getAiContent(String originalNews,int retryTimes) {
//        JSONObject responseJson = null;
//        if(retryTimes > 2) {
//            log.error("重试次数已超过{}次, 原文：{}", retryTimes, originalNews);
//            return null;
//        }
//        try {
//            String response = chatService.processChat(PromptEnum.NEWS_ASSISTANT.getPrompt(), originalNews);
//            log.info("ChatResponse: " + response.toString());
//            responseJson = new JSONObject(response);
//        } catch (JSONException exception){
//            // 生成Json失败了就重新生成，最多试3次
//            log.error("生成Json格式错误");
//            responseJson = getAiContent(originalNews, ++retryTimes);
//        } catch (Exception e){
//            log.error("ChatsinaAiNewsPojo error", e);
//        }
//
//        return responseJson;
//    }
}
