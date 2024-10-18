package com.xiaohai.newsassistant.mp.controller;

import cn.hutool.json.JSONObject;
import com.xiaohai.newsassistant.mp.service.WxBaseService;
import com.xiaohai.newsassistant.mp.service.WxMpMaterial2Service;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
@RestController
@RequestMapping("/wx")
@ResponseBody
public class WxController {

    @Resource
    WxMpMaterial2Service wxMpMaterialService;

    @Resource
    WxBaseService wxBaseService;

    private static final String appid = "wxd35893d33ff520e2";

    @RequestMapping(value = "/addDraft", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void addDraft(@RequestBody String str) throws WxErrorException {
        System.out.println(str);
        JSONObject jsonObject = new JSONObject(str);
        wxBaseService.addDraft(jsonObject.getStr("title"), jsonObject.getStr("content"), null);
    }

    /**
     * 新增永久素材，返回素材url
     * @param str
     * @throws WxErrorException
     */
    @RequestMapping(value = "/mediaImg", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void mediaImg(String str) throws WxErrorException {
        wxMpMaterialService.mediaImgUpload(new File("/Users/xiaoyuntao/Documents/长江公众号.jpg"));
    }

    /**
     * 新增永久素材，返回素材mediaId
     * @param str
     * @throws WxErrorException
     */
    @RequestMapping(value = "/mediaImg2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void mediaImg2(@RequestBody String str) throws WxErrorException {
        WxMpMaterialUploadResult wxMpMaterialUploadResult = wxBaseService.mediaImg2(str);
        System.out.println("=====   返回mediaId    ======");
        System.out.println(wxMpMaterialUploadResult);
    }

    /**
     * 上传指定路径的图片到微信素材库。
     * 注意：此处的文件路径是硬编码的，实际应用中应从配置或请求参数中获取。
     *
     * @param filePath 图片文件的路径
     * @throws WxErrorException 如果上传过程中发生错误，则抛出此异常
     */
    @RequestMapping(value = "/uploadImageToWeChat", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void uploadImageToWeChat(@RequestParam("filePath") String filePath) throws WxErrorException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }
        wxMpMaterialService.mediaUpload(WxConsts.XmlMsgType.IMAGE, file);
    }

    @RequestMapping(value = "/mediaImgGet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void mediaImgGet(String str) throws WxErrorException {
        System.out.println(wxBaseService.mediaImgGet(0, 20).toString());
    }

}
