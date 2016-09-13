package sl.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static String addOneDay(String s, int n) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(s));
			cd.add(Calendar.DATE, n);// 天数
			return sdf.format(cd.getTime());
		} catch (Exception e) {
			return null;
		}
	}

	public static double getFloatFormatStr(double number) {
		String str;
		try {
			String parten = "#.##";
			DecimalFormat decimal = new DecimalFormat(parten);
			str = decimal.format(number);
			return Double.parseDouble(str);
		} catch (Exception e) {
		}
		return 0;
	}

	public static String parseDateToString(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}

		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}

		return ip;
	}

	public static boolean checkNullStr(String str) {
		if (null == str || "null".equalsIgnoreCase(str) || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static String getIpAddr(HttpServletRequest request) {
		if (null == request) {
			return "localhost";
		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String dateToString(Date date, String formatString) {
		if (checkNullStr(formatString)) {
			formatString = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat dd = new SimpleDateFormat(formatString);
		String str = "";
		if (null != date) {
			str = dd.format(date);
		}
		return str;
	}

	public static Date stringToDate(String date, String format) {
		if (Utils.checkNullStr(date)) {
			return null;
		}
		if (Utils.checkNullStr(format)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 判断手机号格式是否正确
	 * 
	 * @author
	 * @since v1.0
	 * @date
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^([\\d]{1,4}-?[\\d]{1,10})$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断座机号是否正确
	 * 
	 * @author huxin
	 * @since v1.0
	 * @date 2013年10月24日 下午14:31
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isPhoneNO(String mobiles) {
		Pattern p = Pattern.compile("^((17[0-9])|(14[0-9])|(13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static String unicodeDecode(String strText) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		char c;
		while (i < strText.length()) {
			c = strText.charAt(i);
			if (c == '\\' && (i + 1) != strText.length() && strText.charAt(i + 1) == 'u') {
				sb.append((char) Integer.parseInt(strText.substring(i + 2, i + 6), 16));
				i += 6;
			} else {
				sb.append(c);
				i++;
			}
		}

		return sb.toString();
	}

	// 计算两点距离
	private final static double EARTH_RADIUS = 6378137.0;

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

	public static boolean isDate(String date) {
		Pattern p = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
		Matcher m = p.matcher(date);
		return m.matches();
	}

	/**
	 * 字符串的格式的日期如何比较大小
	 * 
	 */
	public static boolean compareStringDate(String date, String startDate, String endDate) {
		if (startDate.compareTo(date) <= 0 && endDate.compareTo(date) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	public static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				digest.reset();
				digest.update(originString.getBytes());
				String resultString = toHexString(digest.digest());
				return resultString.toLowerCase();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}

		return originString;
	}

	private static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			sb.append(HEX_DIGITS[(bytes[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[bytes[i] & 0x0f]);
		}
		return sb.toString();
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static boolean isEmpty(List<?> data) {
		if (null == data || data.size() <= 0) {
			return true;
		}
		return false;
	}

	public static final String getNullStr(String str) {
		if (null == str || "null".equalsIgnoreCase(str) || str.length() <= 0) {
			return "";
		}
		return str;
	}

	/**
	 *
	 * @param currentVersion APP当前版本
	 * @param onlineVersion 服务器上APP版本
	 * @return 是否有最新版本
	 */
	public static boolean isHaveNewVersion(String currentVersion, String onlineVersion) {
		if (StringUtils.isBlank(currentVersion)){
			return true;
		}
		if (currentVersion.equals(onlineVersion)) {
			return false;
		}
		String[] localArray = currentVersion.split("\\.");
		String[] onlineArray = onlineVersion.split("\\.");

		int length = localArray.length < onlineArray.length ? localArray.length : onlineArray.length;

		for (int i = 0; i < length; i++) {
			if (Integer.parseInt(onlineArray[i]) > Integer.parseInt(localArray[i])) {
				return true;
			} else if (Integer.parseInt(onlineArray[i]) < Integer.parseInt(localArray[i])) {
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) {
	}
}
