package com.sl.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wtf on 2016/3/28.
 * web工具
 */
public class WebUtils {
    /**
     * request参数->map
     * @param request
     * @return
     */
    public static final Map<String, String> getRequestMap(HttpServletRequest request) {
        Map result = new HashMap();
        Enumeration it = request.getParameterNames();
        String key = null;
        while (it.hasMoreElements()) {
            key = (String)it.nextElement();
            result.put(key, request.getParameter(key));
        }
        return result;
    }

    /**
     * 解析无参post中xml-map
     * @param request
     * @return
     */
    public static final Map<String, String> parsePostXmlToMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        InputStream in = null;
        try {
            in = request.getInputStream();
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();
            for (Element e : elementList){
                map.put(e.getName(), e.getText());
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        }
        return map;
    }

}
