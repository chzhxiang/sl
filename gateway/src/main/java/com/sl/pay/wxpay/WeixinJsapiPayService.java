package com.sl.pay.wxpay;

/**
 * 微信JSAPI支付
 *
 * @author xinqigu
 */
public class WeixinJsapiPayService {

/*
    public static void main(String[] args) {
        WeixinJsapiPayService wj = new WeixinJsapiPayService();
        wj.jsAPIPay();


    }

    public void jsAPIPay(){
        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
        paramsMap.put("appid", "wx64445853f60fe3e4");
        paramsMap.put("mch_id", "1270051501");
        paramsMap.put("nonce_str", WeixinCore.getNonceStr());
        paramsMap.put("body","平安20年寿险");
        paramsMap.put("out_trade_no", "201605231123201683123");//商户系统的订单号(32)
        paramsMap.put("total_fee", "1");//订单总金额,单位为分,不能带小数点
        paramsMap.put("spbill_create_ip", "192.168.1.41");
        paramsMap.put("notify_url", "http://jr.sinosig.com/gateway/pay/wxAsyncNotify");
        paramsMap.put("trade_type", "JSAPI");
        //JSAPI，NATIVE，APP
        paramsMap.put("openid", "o_Au7uCpTJ6_0Snrv1VgUEBBc-H8");
        paramsMap.put("key", "85mlqJYQ8t5Jxi9yOLV8vI4KbrcNoqWj");
        String sign = getSign(paramsMap);
        paramsMap.put("sign", sign);
        String postData = parsePayXML(paramsMap);
        String resXml = getPrepayIdStr("https://api.mch.weixin.qq.com/pay/unifiedorder", postData);

        Map<String, String> prePayMap = WeixinCore.xml2Map(resXml);

        String prepayId = (prePayMap != null) ? prePayMap.get("prepay_id") : null;
        if (StringUtils.isBlank(prepayId)) {
            System.out.println("微信APP生成预订单失败");
        }

        System.out.println("----------预订单信息------------------");
        System.out.println(resXml);

        System.out.println("--------------订单信息-----------------");
        paramsMap.put("prepay_id", prepayId);
        String payParamsXml = joinPayParams(paramsMap);
        System.out.println(payParamsXml);
    }

    public void AppPay(){
        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
        paramsMap.put("appid", "wx7e583a36a5ac57e5");
        paramsMap.put("mch_id", "1268034601");
        paramsMap.put("nonce_str", WeixinCore.getNonceStr());
        paramsMap.put("body","平安20年寿险");
        paramsMap.put("out_trade_no", "201605231123201683123");//商户系统的订单号(32)
        paramsMap.put("total_fee", "1");//订单总金额,单位为分,不能带小数点
        paramsMap.put("spbill_create_ip", "192.168.1.41");
        paramsMap.put("notify_url", "http://jr.sinosig.com/gateway/pay/wxAsyncNotify");
        paramsMap.put("trade_type", "APP");
        paramsMap.put("spbill_create_ip", "192.168.1.41");
        paramsMap.put("key", "85mlqJYQ8t5Jxi9yOLV8vI4KbrcNoqWj");
        String sign = getSign(paramsMap);
        paramsMap.put("sign", sign);
        String postData = parsePayXML(paramsMap);
        String resXml = getPrepayIdStr("https://api.mch.weixin.qq.com/pay/unifiedorder", postData);

        Map<String, String> prePayMap = WeixinCore.xml2Map(resXml);

        String prepayId = (prePayMap != null) ? prePayMap.get("prepay_id") : null;
        if (StringUtils.isBlank(prepayId)) {
            System.out.println("微信JSAPI生成预订单失败");
        }

        System.out.println("----------预订单信息------------------");
        System.out.println(resXml);

        System.out.println("--------------订单信息-----------------");
        paramsMap.put("prepay_id", prepayId);
        String payParamsXml = joinPayParams(paramsMap);
        System.out.println(payParamsXml);
    }



    private static String joinPayParams(TreeMap<String, String> reqParams) {
        TreeMap<String, String> prepayParams = new TreeMap<String, String>();
        prepayParams.put("appId", reqParams.get("appid"));
        prepayParams.put("timeStamp", WeixinCore.getTimeStamp());
        prepayParams.put("nonceStr", WeixinCore.getNonceStr());
        prepayParams.put("package", "prepay_id=" + reqParams.get("prepay_id"));
        prepayParams.put("signType", "MD5");
        prepayParams.put("key", reqParams.get("key"));
        String sign = getSign(prepayParams);
        prepayParams.put("sign", sign);
        prepayParams.put("packageval", prepayParams.get("package"));//package关键字
        prepayParams.remove("package");
        String xml = parsePayXML(prepayParams);
        return xml;
    }



    //app支付

    public static void getPreXml(){
        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
        paramsMap.put("appid", "wx7e583a36a5ac57e5");
        paramsMap.put("mch_id", "1268034601");
        paramsMap.put("nonce_str", WeixinCore.getNonceStr());
        paramsMap.put("body","平安20年寿险");
        paramsMap.put("out_trade_no", "20160523112320168");//商户系统的订单号(32)
        paramsMap.put("total_fee", "1");//订单总金额,单位为分,不能带小数点
        paramsMap.put("spbill_create_ip", "192.168.1.41");
        paramsMap.put("notify_url", "http://jr.sinosig.com/gateway/pay/wxAsyncNotify");
        paramsMap.put("trade_type", "APP");
        //JSAPI，NATIVE，APP
        // paramsMap.put("openid", payReqDto.getUserCode());
        paramsMap.put("key", "efccacb8fd231d27b6a48de64aa337f6");
        String sign = getSign(paramsMap);
        paramsMap.put("sign", sign);
        String postData = parsePayXML(paramsMap);

        String resXml = getPrepayIdStr("https://api.mch.weixin.qq.com/pay/unifiedorder", postData);
        System.out.println(resXml);
    }


    
    *//**
	 * 获取预支付订单返回信息
	 * @param prepayUrl
	 * @param postData
	 * @return 
	 *//*
    private static String getPrepayIdStr(String prepayUrl, String postData) {
    	return WeixinCore.commonHttpRequest(prepayUrl, "POST", postData);
	}

    *//**
     * 验证签名
     *
     * @param reqParams
     * @param sign
     * @return
     *//*
    private boolean checkSign(TreeMap<String, String> reqParams, String sign) {
        return StringUtils.equals(this.getSign(reqParams), sign);
    }

    *//**
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
     *//*
    private static  String getSign(TreeMap<String, String> reqParams) {
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

        System.out.println(stringSignTemp);
        return Md5Utils.encode32(stringSignTemp).toUpperCase();
    }

    *//**
     * 拼接微信请求发送xml数据
     *
     * @param reqParams 参数MAP
     * @return
     *//*
    private static  String parsePayXML(TreeMap<String, String> reqParams) {
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

    public enum WxResponse {

        SUCCESS("SUCCESS", "SUCCESS"), FAIL("FAIL", "FAIL");

        private String code;

        private String msg;

        WxResponse(String code, String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }*/
}
