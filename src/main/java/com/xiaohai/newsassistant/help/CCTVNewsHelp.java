package com.xiaohai.newsassistant.help;

import com.xiaohai.newsassistant.utils.DateTimeUtil;
import com.xiaohai.newsassistant.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

/**
 * @Description: 新闻联播
 * @Author: XiaoYunTao
 * @Date: 2024/9/9
 */
@Component
@Slf4j
public class CCTVNewsHelp {

    public static void main(String[] args) {
        String newsTime = DateTimeUtil.getNewsTime();
        System.out.println(getCCTVNews(newsTime));
    }

    /**
     * 获取新闻联播简介
     * @param date
     * @return
     */
    public static String getCCTVNews(String date) {
        String anAbstract = null;
        String url = "http://tv.cctv.com/lm/xwlb/day/" + date + ".shtml";
        String response = OkHttpUtil.get(url);
        log.info("cctv获取url地址：{}", url);
        try {
            Document doc = Jsoup.parse(response);
            Elements links = doc.select("a");
            String href = links.get(0).attr("href");
            anAbstract = getAbstract(href);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        anAbstract.replaceAll("\n", "<br>");
        return anAbstract + "<br/>";
    }

    public static String getAbstract(String link) {
        String response = OkHttpUtil.get(link);

        try {
            Document document = Jsoup.parse(response);
            Element abstractElement = document.selectFirst(
                    "#page_body > div.allcontent > div.video18847 > div.playingCon > div.nrjianjie_shadow > div > ul > li:nth-child(1) > p"
            );
            if (abstractElement != null) {
                String abstractText = abstractElement.html()
                        .replaceAll("；", "；\n")
                        .replaceAll("：", "：\n");
//                System.out.println(abstractText);
                return abstractText;
            } else {
                System.out.println("abstractElement == null 无法获取新闻简介");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CCTV - getAbstract 获取新闻简介时发生错误");
            return "";
        }
    }
}
