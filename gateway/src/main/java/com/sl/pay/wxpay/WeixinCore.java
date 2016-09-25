package com.sl.pay.wxpay;

import com.bky.util.Md5Utils;
import com.bky.util.XmlUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WeixinCore {
	
	protected final static transient Logger logger = LoggerFactory.getLogger(WeixinCore.class);
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
		String resJson = commonHttpRequest(requestUrl, requestMethod, outputStr);
		JSONObject jsonObject = JSONObject.fromObject(resJson);
		return jsonObject;
	}
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 */
	public static Map<String, String> httpRequestXml(String requestUrl, String requestMethod, String outputStr) {
		String resXml = commonHttpRequest(requestUrl, requestMethod, outputStr);
		return xml2Map(resXml);
	}
	
	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 */
	public static String commonHttpRequest(String requestUrl, String requestMethod, String outputStr) {
		String responseContent = "";
		InputStream inputStream = null;
		HttpsURLConnection httpUrlConn = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			if (StringUtils.isNotBlank(outputStr)) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			responseContent =  buffer.toString();
		} catch (ConnectException ce) {
			logger.error("Weixin server connection timed out.");
			
		} catch (Exception e) {
			logger.error("https request error:{}", e);
			
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					logger.error("close inputstream error.");
				}
			}
			if (httpUrlConn != null) {
				httpUrlConn.disconnect();
				httpUrlConn = null;
			}
		}
		return responseContent;
	}
	
    /**
     * 金额转为分单位
     * @param totalFee
     * @return
     */
	public static String totalFeeToPointStr(Float totalFee) {
    	if (totalFee == null) {
    		return "";
    	} else {
    		Float f = totalFee * 100;
    		return String.valueOf(f.intValue());
    	}
    }
	
	/**
     * 金额分转化为元
     * <p>微信返回以分为单位的整数</p>
     * @param totalFee
     * @return
     */
	public static Float totalFeeToYuan(String totalFee) {
		if (StringUtils.isBlank(totalFee)) {
			return 0f;
		}
		BigDecimal price = new BigDecimal(totalFee);//.setScale(2, RoundingMode.HALF_UP);
		BigDecimal b = new BigDecimal(100);
		return price.divide(b).floatValue();
    }


    /**
     * 生成32位随机串
     * @return
     */
	public static String getNonceStr() {
		Random random = new Random();
		return Md5Utils.encode(String.valueOf(random.nextInt(10000)));
	}

    /**
     * 时间戳，为 1970年1月1日 00:00到请求发起时间的秒数
     * @return
     */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
    
    /**
     * 特殊字符处理
     * @param content
     * @return
     */
	public static String specialCharParse(String content) {
		return content.replace("+", "%20");
	}
	
	/**
	 * xml to map
	 * <p>只读取第一级节点</p>
	 * @param resultXml
	 * @return
	 */
	public static Map<String, String> xml2Map(String resultXml) {
		Map<String, String> map = new HashMap<String, String>();
		Document doc = XmlUtils.getDocumentTxt(resultXml);
		Element rootEl = doc.getRootElement();
		List<Element> list = rootEl.elements();
		for (int i = 0; i < list.size(); i++) {
			Element element = list.get(i);
			map.put(element.getName(), element.getText());
		}
		return map;
	}
	
	/**
	 * 获取json中的值
	 * @param rescontent
	 * @param key
	 * @return
	 */
	public static String getJsonValue(String rescontent, String key) {
		JSONObject jsonObject;
		String v = null;
		try {
			jsonObject = JSONObject.fromObject(rescontent);
			v = jsonObject.getString(key);
		} catch (Exception e) {
			e.getMessage();
		}
		return v;
	}
	
	public enum HttpMethod {
        GET, POST
    }

}
