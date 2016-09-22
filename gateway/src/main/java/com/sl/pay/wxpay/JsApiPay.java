package com.sl.pay.wxpay;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 王腾飞 on 2016/6/1.
 * 微信JsApi支付获取支付信息
 */
public class JsApiPay {
    protected static final transient Logger logger = LoggerFactory.getLogger(JsApiPay.class);
    /**
     * @param paramsMap
     * @return 支付订单的参数
     */
    public static Map<String, String> getPreparePay(TreeMap<String, String> paramsMap){
        String sign = WeixinSignUtils.getSign1(paramsMap);
        paramsMap.put("sign", sign);
        String postData =  WeixinSignUtils.parsePayXML(paramsMap);
        String resXml =  WeixinSignUtils.getPrepayIdStr("https://api.mch.weixin.qq.com/pay/unifiedorder", postData);
        Map<String, String> prePayMap = WeixinCore.xml2Map(resXml);

        String prepayId = (prePayMap != null) ? prePayMap.get("prepay_id") : null;
        if (StringUtils.isBlank(prepayId)) {
            logger.error("微信JsApi生成预订单失败");
            throw new RuntimeException("微信JsApi生成预订单失败:"+resXml);
        }
        paramsMap.put("prepay_id", prepayId);
        return WeixinSignUtils.getJsApiPayParams(paramsMap);
    }
}