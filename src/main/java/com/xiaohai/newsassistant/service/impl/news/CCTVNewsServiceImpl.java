package com.xiaohai.newsassistant.service.impl.news;

import com.xiaohai.newsassistant.service.abstracts.NewsAbstractService;
import com.xiaohai.newsassistant.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * @Description: 新闻联播获取
 * @Author: XiaoYunTao
 * @Date: 2024/9/10
 */
@Slf4j
@Service("cCTVNewsServiceImpl")
public class CCTVNewsServiceImpl extends NewsAbstractService {
    /**
     * 获取新闻联播简介
     * @param date
     * @return
     */
    @Override
    public String getOriginalNews(String date) {
        String anAbstract = null;
        String url = "http://tv.cctv.com/lm/xwlb/day/" + date + ".shtml";
        String response = OkHttpUtil.get(url);
        log.info("cctv获取url地址：{}", url);
        try {
            Document doc = Jsoup.parse(response);
            Elements links = doc.select("a");
            String href = links.get(0).attr("href");
            anAbstract = getSummer(href);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return anAbstract;
    }

    public String getSummer(String link) {
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
