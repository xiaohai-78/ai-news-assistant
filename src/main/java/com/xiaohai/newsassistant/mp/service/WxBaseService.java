package com.xiaohai.newsassistant.mp.service;

import cn.hutool.json.JSONObject;
import com.xiaohai.newsassistant.mp.config.WxMpProperties;
import com.xiaohai.newsassistant.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpDraftService;
import me.chanjar.weixin.mp.api.WxMpFreePublishService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpDraftServiceImpl;
import me.chanjar.weixin.mp.api.impl.WxMpFreePublishServiceImpl;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author xyt
 */
@Slf4j
@Service
public class WxBaseService {
    @Resource
    WxMpMaterial2Service wxMpMaterialService;
    @Resource
    WxMpProperties wxMpProperties;

    private final WxMpService wxMpService;

    private final WxMpDraftService wxMpDraftService;

    public WxMpFreePublishService getFreePublishService(){
        return new WxMpFreePublishServiceImpl(wxMpService);
    }

    @Autowired
    public WxBaseService(WxMpService wxMpService){
        this.wxMpService = wxMpService;
        this.wxMpDraftService = new WxMpDraftServiceImpl(wxMpService);
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
        if (thumbMediaId == null) {
            thumbMediaId = "ec0_mO20KC7z0oRLz9NBRWHnWxZpNzZmHgSAN3hH1Z2NGYJDYNdm6cXTGA4mF1rp";
        }
        return wxMpDraftService.addDraft(title, content, thumbMediaId);
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
}
