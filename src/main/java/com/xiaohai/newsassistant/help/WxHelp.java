package com.xiaohai.newsassistant.help;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xiaohai.newsassistant.mp.config.WxMpProperties;
import com.xiaohai.newsassistant.mp.service.WxMpDraftService2;
import com.xiaohai.newsassistant.pojo.WeChatOfficial;
import com.xiaohai.newsassistant.pojo.WxDraftDO;
import com.xiaohai.newsassistant.utils.OkHttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.xiaohai.newsassistant.mp.service.WxBaseService.getFreePublishService;

/**
 * @Description: 微信公众号方法
 * @Author: XiaoYunTao
 * @Date: 2024/9/4
 */
@Service
@Slf4j
public class WxHelp {

    private static final String  draftAddUrl = "https://api.weixin.qq.com/cgi-bin/draft/add?access_token=";
    private static final String  wxAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/stable_token";

    @Resource
    private WxMpMaterialService wxMpMaterialService;

    @Resource
    WxMpDraftService2 wxMpDraftService2;

    @Resource
    WxMpProperties wxMpProperties;

    public static void main(String[] args) {
//        System.out.println(getWxAccessToken());
//        getWxToken();
    }


    public String draftAdd(WxDraftDO wxDraftDO){
        // 创建一个 JSONObject 对象
        JSONObject article = new JSONObject();

        // 设置 JSON 对象的属性
        article.put("title", wxDraftDO.getTitle());
        article.put("author", wxDraftDO.getAuthor());
        article.put("digest", wxDraftDO.getDigest());
        article.put("content", wxDraftDO.getContent());
        article.put("content_source_url", wxDraftDO.getContent_source_url());
        // 图文消息的封面图片素材id（必须是永久MediaID）
        article.put("thumb_media_id", wxDraftDO.getThumb_media_id());

        article.put("need_open_comment", wxDraftDO.getNeed_open_comment());
        article.put("only_fans_can_comment", wxDraftDO.getOnly_fans_can_comment());

        // 将 JSONObject 对象添加到 JSON 数组中
        JSONObject json = new JSONObject();
        json.put("articles", article);

        System.out.println(article.toString());
//        String response = OkHttpUtil.postCommon(draftAddUrl + getWxAccessToken(), article, null, null);
        String response = post(draftAddUrl + getWxAccessToken(), article.toString());
        JSONObject resJson = new JSONObject(response);
        String mediaId = resJson.getStr("media_id");
        return mediaId;
    }

    private static String post(String targetURL, String request){
        try {
            URL url = new URL(targetURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = request.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            // 你可能还想读取响应。这个例子没有展示如何读取响应体。

            // 读取响应
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // 打印出响应内容
                System.out.println("Response body : " + response.toString());
                return response.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取稳定版接口调用凭据
     * @return
     */
    public String getWxAccessToken(){
        WxMpProperties.MpConfig mpConfig = wxMpProperties.getConfigs().get(0);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("grant_type", "client_credential");
        jsonObject.put("appid", mpConfig.getAppId());
        jsonObject.put("secret", mpConfig.getSecret());

        Map<String, Object> params = new HashMap<>();
        String post = OkHttpUtil.postCommon(wxAccessTokenUrl, jsonObject, null, null);
        JSONObject res = new JSONObject(post);
        return res.getStr("access_token");
    }

    public void test() throws WxErrorException {
        wxMpMaterialService.mediaUpload(WxConsts.XmlMsgType.IMAGE, new File("～/Users/xiaoyuntao/Documents/IMG_0003.JPG"));
    }

    /**
     * 获取微信token
     * @return
     */
    public String getWxToken(){
        WxMpProperties.MpConfig mpConfig = wxMpProperties.getConfigs().get(0);
        String response = OkHttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + mpConfig.getAppId() + "&secret=" + mpConfig.getSecret());
        log.info(response);
        JSONObject everydayEngilsh = new JSONObject(response);
        String accessToken = everydayEngilsh.getStr("access_token");
        log.info(accessToken);
        return  accessToken;
    }

    /**
     * 添加临时素材
     * @param str 素材地址如："/Users/xiaoyuntao/Documents/IMG_0003.JPG"
     * @throws WxErrorException
     */
    public WxMediaUploadResult addDraft(String str) throws WxErrorException {
        String media = getFreePublishService().submit("ec0_mO20KC7z0oRLz9NBRanpRJV33_6n0EFdI1sl_TqqslPi1hkCF1zbrRbhxwzu");
        return wxMpMaterialService.mediaUpload(WxConsts.XmlMsgType.IMAGE, new File(str));
    }

    /**
     * 新建草稿 - 只有默认必填参数
     * <pre>
     * 请求地址：POST https://api.weixin.qq.com/cgi-bin/draft/add?access_token=ACCESS_TOKEN
     * 文档地址：https://developers.weixin.qq.com/doc/offiaccount/Draft_Box/Add_draft.html
     * </pre>
     *
     * @param title        标题
     * @param content      图文消息的具体内容，支持HTML标签，必须少于2万字符，小于1M，且此处会去除JS,涉及图片url必须来源 "上传图文消息内的图片获取URL"接口获取。外部图片url将被过滤。
     * @param thumbMediaId 图文消息的封面图片素材id（必须是永久MediaID）
     * @return the string
     * @throws WxErrorException .
     */
    public String addDraft(String title, String content, String thumbMediaId) throws WxErrorException {
        return wxMpDraftService2.addDraft(title, content, thumbMediaId);
    }

    /**
     * 新增永久素材，返回素材url
     *
     * @param str 素材地址如："/Users/xiaoyuntao/Documents/IMG_0003.JPG"
     * @return
     * @throws WxErrorException
     */
    public WxMediaImgUploadResult mediaImg(String str) throws WxErrorException {
        return wxMpMaterialService.mediaImgUpload(new File(str));
    }

    /**
     * <pre>
     * 新增非图文永久素材
     * 通过POST表单来调用接口，表单id为media，包含需要上传的素材内容，有filename、filelength、content-type等信息。请注意：图片素材将进入公众平台官网素材管理模块中的默认分组。
     * 新增永久视频素材需特别注意：
     * 在上传视频素材时需要POST另一个表单，id为description，包含素材的描述信息，内容格式为JSON，格式如下：
     * {   "title":VIDEO_TITLE,   "introduction":INTRODUCTION   }
     * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738729&token=&lang=zh_CN">新增永久素材</a>
     * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE
     *
     * 除了3天就会失效的临时素材外，开发者有时需要永久保存一些素材，届时就可以通过本接口新增永久素材。
     * 永久图片素材新增后，将带有URL返回给开发者，开发者可以在腾讯系域名内使用（腾讯系域名外使用，图片将被屏蔽）。
     * 请注意：
     * 1、新增的永久素材也可以在公众平台官网素材管理模块中看到
     * 2、永久素材的数量是有上限的，请谨慎新增。图文消息素材和图片素材的上限为5000，其他类型为1000
     * 3、素材的格式大小等要求与公众平台官网一致。具体是，图片大小不超过2M，支持bmp/png/jpeg/jpg/gif格式，语音大小不超过5M，长度不超过60秒，支持mp3/wma/wav/amr格式
     * 4、调用该接口需https协议
     * </pre>
     *
     *  mediaType 媒体类型, 请看{@link WxConsts}
     *  material  上传的素材, 请看{@link WxMpMaterial}
     * @return the wx mp material upload result
     * @throws WxErrorException the wx error exception
     */
    public WxMpMaterialUploadResult mediaImg2(String str) throws WxErrorException {
        WxMpMaterial cat = new WxMpMaterial("cat", new File(str), null, null);
        return wxMpMaterialService.materialFileUpload(WxConsts.MediaFileType.THUMB, cat);
    }

    /**
     * <pre>
     * 分页获取图文素材列表
     *
     * 详情请见: <a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1444738734&token=&lang=zh_CN">获取素材列表</a>
     * 接口url格式：https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN
     * </pre>
     *
     * @param offset 从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
     * @param count  返回素材的数量，取值在1到20之间
     * @return the wx mp material news batch get result
     * @throws WxErrorException the wx error exception
     */
    public WxMpMaterialFileBatchGetResult mediaImgGet(int offset, int count) throws WxErrorException {
        return wxMpMaterialService.materialFileBatchGet(WxConsts.XmlMsgType.IMAGE, offset, count);
    }
}

