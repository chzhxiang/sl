package com.sl.util;

import com.alibaba.fastjson.JSON;

/**
 * json转换，适应老接口
 */
public class JsonUtils {

	public static <T> String beanToJson(T bean) {
	    return JSON.toJSONString(bean);
	}

	public static <T> T parseObject(String json,Class<T> t) {
		return JSON.parseObject(json,t);
	}
    
}
