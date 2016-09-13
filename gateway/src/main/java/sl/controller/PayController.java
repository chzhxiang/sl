package sl.controller;

import com.sl.pay.alipay.config.AlipayConfig;
import com.sl.pay.alipay.util.AlipayNotify;
import com.sl.pay.alipay.util.AlipaySubmit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sunlei on 2016/5/23.
 *
 * 1、用于支付相关微信支付以及支付宝支付
 * 2、支付字符串的生成以及同步回调地址和异步回调处理
 * 3、支付状态查询和更新
 */

@Controller
@RequestMapping("/pay")
public class PayController {
    private final Logger logger = LoggerFactory.getLogger(PayController.class);

    @RequestMapping("/wxSyncNotify")
    public String  wxPaySyncNotify(){
        logger.info(">>>>>>>>>>>>>>>>>>>>微信同步回调");
        return "redirect:http://www.baidu.com";
    }

    /**
     * 微信异步回调
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/wxAsyncNotify", produces="application/json;charset=UTF-8", method= RequestMethod.POST)
    public String wxPayAsyncNotify(HttpServletRequest request){
        /**
         * todo 1、验证请求的签名 (由于是手工的又去查询了一次订单状态，暂时先不做微信的签名校验)
         */
        logger.info(">>>>>>>>>>>>>>>>>>>>微信异步回调");
        Map<String, String> dataMap = new HashMap<String, String>();
        Map<String, String> xmlMap = WebUtils.parsePostXmlToMap(request);
        dataMap.putAll(xmlMap);
        logger.info(JsonUtils.beanToJson(dataMap));
        if (!xmlMap.containsKey("out_trade_no")) {
            return this.getNotifyErrorResult();
        }
        String out_trade_no = dataMap.get("out_trade_no");
        boolean paystatus = appPay.queryPayStatus(out_trade_no);
        if(paystatus){
            logger.info("订单{}支付成功",new Object[]{out_trade_no});
             mobileMyOrderServiceImpl.updateMyOrderModelStateByOrderNo(out_trade_no,"已支付");
            return this.getNotifySuccessResult();
        }else{
            return this.getNotifyErrorResult();
        }
    }

    /**
     * 根据订单号查询微信订单是否支付成功
     * @param orderno
     * @return
     */
    @RequestMapping("/verifyOrderPayStatus")
    @ResponseBody
    public JResult<String> verifyOrderPayStatus(@RequestParam(required = true) String orderno){
        logger.info("查询订单{}支付状态",new Object[]{orderno});
        if(appPay.queryPayStatus(orderno)){
            return JResult.SUCCESS;
        }
        return JResult.ERROR("订单支付失败");
    }

    /**
     * 回调接口返回正确信息
     * @return
     */
    private String getNotifySuccessResult() {
        return "<xml><return_code>SUCCESS</return_code><return_msg></return_msg></xml>";
    }

    /**
     * 回调接口返回错误信息
     * @return
     */
    private String getNotifyErrorResult() {
        return "<xml><return_code>FAIL</return_code><return_msg>FAIL</return_msg></xml>";
    }

    @RequestMapping(value="/toAliPay",method = RequestMethod.POST,produces="application/json")
    @ResponseBody
    public void toAliPay(HttpServletRequest request){
        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = request.getParameter("orderno");

        //订单名称，必填
        String subject = request.getParameter("productName");

        //付款金额，必填
        String total_fee = request.getParameter("money");

        //收银台页面上，商品展示的超链接，必填
        String show_url = request.getParameter("show_url");

        //商品描述，可空
        String body = request.getParameter("desc");

        //////////////////////////////////////////////////////////////////////////////////

        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", AlipayConfig.notify_url);
        sParaTemp.put("return_url", AlipayConfig.return_url);
        sParaTemp.put("out_trade_no", out_trade_no);
        sParaTemp.put("subject", subject);
        sParaTemp.put("total_fee", total_fee);
        sParaTemp.put("show_url", show_url);
        //sParaTemp.put("app_pay","Y");//启用此参数可唤起钱包APP支付。
        sParaTemp.put("body", body);
        //其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.2Z6TSk&treeId=60&articleId=103693&docType=1
        //如sParaTemp.put("参数名","参数值");

        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");

        JResult<String> result = new JResult<String>();
        result.setData(sHtmlText);
//        out.println(sHtmlText);

    }

    @ResponseBody
    @RequestMapping(value="/aliPayAsyncNotify", produces="application/json;charset=UTF-8", method= RequestMethod.POST)
    public void aliPayAsyncNotify(HttpServletRequest request,HttpServletResponse response) throws IOException{
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = request.getParameter("out_trade_no");

        //支付宝交易号

        String trade_no = request.getParameter("trade_no");

        //交易状态
        String trade_status = request.getParameter("trade_status");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

        if(AlipayNotify.verify(params)){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

            response.getWriter().print("success");	//请不要修改或删除

            //////////////////////////////////////////////////////////////////////////////////////////
        }else{//验证失败
            response.getWriter().print("fail");
        }
    }


//    public String  aliPaySyncNotify(HttpServletRequest request){
//        //获取支付宝GET过来反馈信息
//        Map<String,String> params = new HashMap<String,String>();
//        Map requestParams = request.getParameterMap();
//        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i]
//                        : valueStr + values[i] + ",";
//            }
//            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//            params.put(name, valueStr);
//        }
//
//        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
//        //商户订单号
//
//        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
//
//        //支付宝交易号
//
//        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
//
//        //交易状态
//        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
//
//        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
//
//        //计算得出通知验证结果
//        boolean verify_result = AlipayNotify.verify(params);
//
//        if(verify_result){//验证成功
//            //////////////////////////////////////////////////////////////////////////////////////////
//            //请在这里加上商户的业务逻辑程序代码
//
//            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
//            if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
//                //判断该笔订单是否在商户网站中已经做过处理
//                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
//                //如果有做过处理，不执行商户的业务程序
//            }
//
//            //该页面可做页面美工编辑
//            out.println("验证成功<br />");
//            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//
//            //////////////////////////////////////////////////////////////////////////////////////////
//        }else{
//            //该页面可做页面美工编辑
//            out.println("验证失败");
//        }
//    }
}
