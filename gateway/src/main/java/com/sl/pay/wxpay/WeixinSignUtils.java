package com.sl.pay.wxpay;

import com.bky.util.Md5Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 王腾飞 on 2016/5/31.
 */
public class WeixinSignUtils {
    protected static final transient Logger logger = LoggerFactory.getLogger(WeixinSignUtils.class);
    /**
     * APP支付获取支付是的参数信息
     * @param reqParams
     * @return
     */
    public static  String getAppPayParams(TreeMap<String, String> reqParams){
        TreeMap<String, String> prepayParams = new TreeMap<String, String>();
        prepayParams.put("appid", reqParams.get("appid"));
        prepayParams.put("partnerid", reqParams.get("mch_id"));
        prepayParams.put("prepayid", reqParams.get("prepay_id"));
        prepayParams.put("package", "Sign=WXPay");
        prepayParams.put("noncestr",reqParams.get("nonce_str"));
        prepayParams.put("timestamp",WeixinCore.getTimeStamp());
        prepayParams.put("key", reqParams.get("key"));
        String sign = getSign(prepayParams);
        prepayParams.put("sign", sign);
        String xml = parsePayXML(prepayParams);
        return xml;
    }
    /**
     * JsApi支付获取支付是的参数信息
     * @param reqParams
     * @return
     */
    public static Map<String, String> getJsApiPayParams(TreeMap<String, String> reqParams) {
        TreeMap<String, String> prepayParams = new TreeMap<String, String>();
        prepayParams.put("appId", reqParams.get("appid"));
        prepayParams.put("timeStamp", WeixinCore.getTimeStamp());
        prepayParams.put("nonceStr", WeixinCore.getNonceStr());
        prepayParams.put("package", "prepay_id=" + reqParams.get("prepay_id"));
        prepayParams.put("signType", "MD5");
        String sign = getSign1(prepayParams);
        prepayParams.put("paySign", sign);
        return prepayParams;
    }


    /**
     * 拼接微信请求发送xml数据
     *
     * @param reqParams 参数MAP
     * @return
     */
    public static  String parsePayXML(TreeMap<String, String> reqParams) {
        StringBuilder sbl = new StringBuilder();
        sbl.append("<xml>");
        Iterator<String> itr = reqParams.keySet().iterator();
        String key = null;
        String value = null;
        while (itr.hasNext()) {
            key = itr.next();
            value = reqParams.get(key);
            if (StringUtils.isNotBlank(value) && !StringUtils.equals(key, "key")) {
                sbl.append("<").append(key).append(">");
                if (StringUtils.equals(key, "attach")
                        || StringUtils.equals(key, "body")
                        || StringUtils.equals(key, "sign")) {
                    sbl.append("<![CDATA[").append(value).append("]]>");
                } else {
                    sbl.append(value);
                }
                sbl.append("</").append(key).append(">");
            }
        }
        sbl.append("</xml>");
        return sbl.toString();
    }

    /**
     * 生成签名
     * <p>
     * a.对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）后，
     * 使用 URL 键值对的格式（即 key1=value1&key2=value2…）拼接成字符串 string1,
     * 注意：值为空的参数不参不签名
     * b.在 string1 最后拼接上 key=Key(商户支付密钥)得到 stringSignTemp
     * 字符串，并对stringSignTemp 进行 md5 运算，再将得到的字符串所有字符转换为大写，得到 sign值
     * </p>
     *
     * @param reqParams
     * @return
     */
    public static  String getSign(TreeMap<String, String> reqParams) {
        StringBuilder sbl = new StringBuilder();
        Iterator<String> itr = reqParams.keySet().iterator();
        String key = null;
        String value = null;
        while (itr.hasNext()) {
            key = itr.next();
            value = reqParams.get(key);
            if (StringUtils.isNotBlank(value)
                    && !StringUtils.equals(key, "sign")
                    && !StringUtils.equals(key, "key")) {
                sbl.append(key).append("=").append(value).append("&");
            }
        }
        String stringSignTemp = sbl.append("key=").append(reqParams.get("key")).toString();
        return Md5Utils.encode32(stringSignTemp).toUpperCase();
    }

    public static  String getSign1(TreeMap<String, String> reqParams) {
        StringBuilder sbl = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = reqParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isNotBlank(value)
                    && !StringUtils.equals(key, "sign")) {
                sbl.append(key).append("=").append(value).append("&");
            }
        }

        if(sbl.length() > 0){
            sbl.deleteCharAt(sbl.length()-1);
        }

        return Md5Utils.encode32(sbl.toString()).toUpperCase();
    }

    /**
     * 获取预支付订单返回信息
     * @param prepayUrl
     * @param postData
     * @return
     */
    public static String getPrepayIdStr(String prepayUrl, String postData) {
        return WeixinCore.commonHttpRequest(prepayUrl, "POST", postData);
    }
}
