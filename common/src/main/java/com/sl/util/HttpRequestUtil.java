package com.sl.util;


import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;


public class HttpRequestUtil {


	private final static Logger LOG = LoggerFactory.getLogger(HttpRequestUtil.class);

	private static final String DEFAULT_ENCODING = "UTF-8";

	public static String saveFile(String url, String savePath) {
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		String result = "";
		try {
			URL getUrl = new URL(url);
			// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
			// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
			// 服务器
			connection.connect();
			// 取得输入流，并使用Reader读取
			InputStream input = connection.getInputStream();
			//设置本地保存的文件
			File saveFile = new File(savePath);
			FileOutputStream output = new FileOutputStream(saveFile);
			byte b[] = new byte[1024];
			int i = 0;
			while ((i = input.read(b)) != -1) {
				output.write(b, 0, i);
			}
			output.flush();
			output.close();
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			LOG.error("", e);
		}
		return result;
	}


	public static String get(String url, HashMap<String, Object> params, String encoding) {
		if (null == encoding || "".equals(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
		StringBuilder getURL = new StringBuilder(url);
		String result = "";
		try {
			if (null != params && params.size() > 0) {
				getURL.append("?");
				for (Entry<String, Object> param : params.entrySet()) {
					getURL.append(param.getKey()).append("=").append(param.getValue().toString()).append("&");
				}
				getURL.deleteCharAt(getURL.length() - 1);
			}
			System.out.println("url:" + getURL.toString());
			URL getUrl = new URL(getURL.toString());
			// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
			// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
			HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
//			if (connection instanceof javax.net.ssl.HttpsURLConnection) {
//				SSLContext context = SSLContext.getInstance("TLS");
//				context.init(new KeyManager[0], TRUST_MANAGER,
//						new SecureRandom());
//				SSLSocketFactory socketFactory = context.getSocketFactory();
//				((javax.net.ssl.HttpsURLConnection) connection)
//						.setSSLSocketFactory(socketFactory);
//				((javax.net.ssl.HttpsURLConnection) connection)
//						.setHostnameVerifier(HOSTNAME_VERIFIER);
//			}

			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);

			// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
			// 服务器
			connection.connect();
			// 取得输入流，并使用Reader读取
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));//设置编码,否则中文乱码
			String lines;
			while ((lines = reader.readLine()) != null) {
				//lines = new String(lines.getBytes(), "utf-8");
				result += lines;
			}
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			LOG.error("", e);
		}
		return result;
	}

	public static String post(String url, HashMap<String, Object> params, String encoding) {
		if (null == encoding || "".equals(encoding)) {
			encoding = DEFAULT_ENCODING;
		}
		String result = "";
		try {
			URL postUrl = new URL(url);
			// 打开连接
			HttpURLConnection connection = (HttpURLConnection) postUrl
					.openConnection();

			// 设置是否向connection输出，因为这个是post请求，参数要放在
			// http正文内，因此需要设为true
			connection.setDoOutput(true);
			// Read from the connection. Default is true.
			connection.setDoInput(true);
			// Set the post method. Default is GET
			connection.setRequestMethod("POST");
			// Post 请求不能使用缓存
			connection.setUseCaches(false);
			// This method takes effects to every instances of this class.
			// URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
			// connection.setFollowRedirects(true);
			// This methods only takes effacts to this instance.
			// URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
			connection.setInstanceFollowRedirects(true);
			// Set the content type to urlencoded,because we will write some URL-encoded content to the connection.
			// Settings above must be set before connect!
			// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
			// 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
			// 进行编码
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setConnectTimeout(6000);  //设置超时时间为6s
			connection.setReadTimeout(6000);//设置超时时间6s

			// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
			// 要注意的是connection.getOutputStream会隐含的进行connect。
			connection.connect();

			if (null != params && params.size() > 0) {
				DataOutputStream out = new DataOutputStream(connection.getOutputStream());
				StringBuilder content = new StringBuilder("");
				for (Entry<String, Object> param : params.entrySet()) {
					if (null == param.getValue()) {
						content.append(param.getKey()).append("=").append(URLEncoder.encode("", "UTF-8")).append("&");
					} else {
						content.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue().toString(), "UTF-8")).append("&");
					}
					//content.append(param.getKey()).append("=").append(param.getValue().toString()).append("&");
				}
				content.deleteCharAt(content.length() - 1);
				// DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
				out.writeBytes(content.toString());
				out.flush();
				out.close();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));//设置编码,否则中文乱码
			String line = "";
			while ((line = reader.readLine()) != null) {
				//line = new String(line.getBytes(), "utf-8");
				result += line;
			}
			reader.close();
			connection.disconnect();
		} catch (Exception e) {
			LOG.error("", e);
		}

		return result;
	}


	/**
	 * 使用POST传递json数据
	 *
	 * @param url
	 * @param json
	 * @throws Exception
	 */
	public static String postWithJson(String url, String json) {
		String response = null;
		try {
			//创建连接
			URL postUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.connect();

			//POST请求
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(json);
			out.flush();
			out.close();

			//读取响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String lines;
			StringBuffer sb = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}
			reader.close();
			// 断开连接
			connection.disconnect();
			response = sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;

	}

	/**
	 * @param f   保存的文件
	 * @param url 图片地址
	 */
	public static void down(File f, String url) {
		byte[] buffer = new byte[8 * 1024];
		URL u;
		URLConnection connection = null;
		try {
			u = new URL(url);
			connection = u.openConnection();
		} catch (Exception e) {
			System.out.println("ERR:" + url);
			return;
		}
		connection.setReadTimeout(100000);
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			connection.connect();
		} catch (IOException e1) {
			LOG.error("", e1);
		}
		try {
			f.createNewFile();
			is = connection.getInputStream();
			fos = new FileOutputStream(f);
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			f.delete();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOG.error("", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		buffer = null;
	}

	@SuppressWarnings("unused")
	private static TrustManager[] TRUST_MANAGER = {new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}


		public void checkServerTrusted(X509Certificate[] certs, String authType)
				throws CertificateException {
			return;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType)
				throws CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] chain, String authType)
				throws java.security.cert.CertificateException {
		}
	}};

}
