package com.sl.util;

import org.apache.commons.lang.RandomStringUtils;

import java.util.UUID;


public class Utils {
	// 计算两点距离
	private final static double EARTH_RADIUS = 6378137.0;

	public static long gps2m(Double lng_a, Double lat_a, Double lng_b, Double lat_b) {
		if (null != lng_a && 0 != lng_a && null != lat_a && 0 != lat_a
				&& null != lng_b && 0 != lng_b && null != lat_b && 0 != lat_b) {
			return (long) gps2m(lng_a.doubleValue(), lat_a.doubleValue(), lng_b.doubleValue(), lat_b.doubleValue());
		} else {
			return 0;
		}
	}

	public static double gps2m(double lng_a, double lat_a, double lng_b, double lat_b) {
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	public static String generateCode() {
		UUID uuid = UUID.randomUUID();
		String code = uuid.toString();
		code = code.replaceAll("-", "");
		code = Md5Utils.encode(code);
		//code = code.substring(8, 24);
		return code;
	}

	public static String generatePinYin(String name) {
		return ChineseToPinyinUtils.getPinYinHeadChar(name) + "," + ChineseToPinyinUtils.getPingYin(name);
	}

	/**
	 * 生成订单编号
	 *
	 * @param target 头部标识符号
	 * @return target + 毫秒数 + 6位随机数字
	 */
	public static String generateOrderCode(String target) {
		StringBuffer sb = new StringBuffer();
		sb.append(target).append(System.currentTimeMillis()).append(RandomStringUtils.randomNumeric(6));
		return sb.toString();
	}

	/**
	 * 判断字符串不为空
	 *
	 * @param target
	 * @return
	 */
	public static boolean isNotBlank(String target) {
		return target != null && target.trim().length() > 0;
	}
	
	/**
	 * 对密码编码
	 * @param password
	 * @return
	 */
	public static String encodePassword(String password){
		return Md5Utils.encode32(password).toUpperCase();
	}
	
	
}
