package com.sl.pay.wxpay;

import com.bky.gateway.payment.PayOrder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 王腾飞 on 2016/5/31.
 * 微信app支付 获取支付相关信息
 */
@Component
public class AppPay {
    protected static final transient Logger logger = LoggerFactory.getLogger(AppPay.class);

    private static final String APPID="appid";
    private static final String MCH_ID="mch_id";
    private static final String NONCE_STR="nonce_str";
    private static final String BODY="body";
    private static final String OUT_TRADE_NO="out_trade_no";
    private static final String TOTAL_FEE="total_fee";
    private static final String SPBILL_CREATE_IP="spbill_create_ip";
    private static final String NOTIFY_URL = "notify_url";
    private static final String TRADE_TYPE="trade_type";
    private static final String KEY="key";
    private static final String SING="sign";
    private static final String PREPAY_ID="prepay_id";
    private static final String TRADE_STATE = "trade_state";

    //private String appid="wx7e583a36a5ac57e5";
    //private String mch_id="1268034601";
    private String spbill_create_ip="192.168.1.41";
    private String trade_type="APP";
    //private String key="85mlqJYQ8t5Jxi9yOLV8vI4KbrcNoqWj";
    private String prepayUrl="https://api.mch.weixin.qq.com/pay/unifiedorder";
    //@Value("${weixinpay.notifyurl}")
    //private String notifyurl;
    private String orderQueryUrl="https://api.mch.weixin.qq.com/pay/orderquery";


    private String appid="wx5c0fd2cb2d816285";
    private String mch_id="1298141401";
    private String key="85mlqJYQ8t5Jxi9yOLV8vI4KbrcNoqWj";

    /**
     * 根据payorder 请求微信服务器生成预订单
     * @param payOrder
     * @return 支付所需相关数据
     * 本身 notifyurl需要在这里面根据不同环境进行注入，而不应该由调用端传入，但是由于无法用@Value注入，采取如此下策，后续更改
     */
    public  Map<String, String> getPreparePay(PayOrder payOrder,String notifyurl){
        logger.info("查看notifyurl的值");
        if(StringUtils.isEmpty(notifyurl)){
            logger.info("notifyurl的为为空");
        }else{
            logger.info(notifyurl);
        }

        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
        paramsMap.put(APPID, appid);
        paramsMap.put(MCH_ID, mch_id);
        paramsMap.put(NONCE_STR, WeixinCore.getNonceStr());
        paramsMap.put(BODY,payOrder.getBody());
        paramsMap.put(OUT_TRADE_NO, payOrder.getOut_trade_no());//商户系统的订单号(32)
        paramsMap.put(TOTAL_FEE, payOrder.getTotal_fee()+"");//订单总金额,单位为分,不能带小数点
        paramsMap.put(SPBILL_CREATE_IP,spbill_create_ip);
        paramsMap.put(NOTIFY_URL,notifyurl);
        paramsMap.put(TRADE_TYPE,trade_type);
        paramsMap.put(KEY, key);
        String sign = WeixinSignUtils.getSign(paramsMap);
        paramsMap.put(SING, sign);
        String postData = WeixinSignUtils.parsePayXML(paramsMap);

        logger.info("微信App支付请求参数：{}",new Object[]{postData});
        String resXml = WeixinSignUtils.getPrepayIdStr(prepayUrl, postData);
        Map<String, String> prePayMap = WeixinCore.xml2Map(resXml);
        String prepayId = (prePayMap != null) ? prePayMap.get(PREPAY_ID) : null;
        if (StringUtils.isBlank(prepayId)) {
            logger.error("微信App支付生成预订单失败 {}",new Object[]{resXml});
            throw new RuntimeException("微信App支付生成预订单失败");
        }
        paramsMap.put("prepay_id", prepayId);
        String payParamsXml = WeixinSignUtils.getAppPayParams(paramsMap);
        logger.info(">>微信app支付返回支付字符串{}",new Object[]{payParamsXml});
        Map<String, String> stringStringMap = WeixinCore.xml2Map(payParamsXml);
        return stringStringMap;
    }

    /**
     * 根据订单号向微信服务器查询订单是否支付成功
     * @param orderNo 订单号
     * @return
     */
    public boolean queryPayStatus(String orderNo){
        TreeMap<String, String> paramsMap = new TreeMap<String, String>();
        paramsMap.put(APPID,appid);
        paramsMap.put(MCH_ID,mch_id);
        paramsMap.put(OUT_TRADE_NO,orderNo);
        paramsMap.put(NONCE_STR,WeixinCore.getNonceStr());
        paramsMap.put(KEY,key);
        String sign = WeixinSignUtils.getSign(paramsMap);
        paramsMap.put(SING, sign);
        String postData = WeixinSignUtils.parsePayXML(paramsMap);
        String resXml = WeixinSignUtils.getPrepayIdStr(orderQueryUrl, postData);
        System.out.println(resXml);
        Map<String, String> stringStringMap = WeixinCore.xml2Map(resXml);
        String trade_state = stringStringMap.get(TRADE_STATE);
        if(StringUtils.isNotEmpty(trade_state)){
        	return  "SUCCESS".equals(trade_state.trim().toUpperCase());
        }
        return false;
        
    }
}
