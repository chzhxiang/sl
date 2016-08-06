package com.sl.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by sunlei on 2016/7/18.
 */
public class HttpsPost {
    /**
     * 获得KeyStore.
     * @param keyStorePath
     *            密钥库路径
     * @param password
     *            密码
     * @return 密钥库
     * @throws Exception
     */
    public static KeyStore getKeyStore(String password, String keyStorePath)
            throws Exception {
        // 实例化密钥库
        KeyStore ks = KeyStore.getInstance("JKS");
        // 获得密钥库文件流
        FileInputStream is = new FileInputStream(keyStorePath);
        // 加载密钥库
        ks.load(is, password.toCharArray());
        // 关闭密钥库文件流
        is.close();
        return ks;
    }

    /**
     * 获得SSLSocketFactory.
     * @param password
     *            密码
     * @param keyStorePath
     *            密钥库路径
     * @param trustStorePath
     *            信任库路径
     * @return SSLSocketFactory
     * @throws Exception
     */
    public static SSLContext getSSLContext(String password,
                                           String keyStorePath, String trustStorePath) throws Exception {
        // 实例化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        // 获得密钥库
        KeyStore keyStore = getKeyStore(password, keyStorePath);
        // 初始化密钥工厂
        keyManagerFactory.init(keyStore, password.toCharArray());

        // 实例化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // 获得信任库
        KeyStore trustStore = getKeyStore(password, trustStorePath);
        // 初始化信任库
        trustManagerFactory.init(trustStore);
        // 实例化SSL上下文
        SSLContext ctx = SSLContext.getInstance("TLS");
        // 初始化SSL上下文
        ctx.init(keyManagerFactory.getKeyManagers(),
                trustManagerFactory.getTrustManagers(), null);
        // 获得SSLSocketFactory
        return ctx;
    }

    /**
     * 初始化HttpsURLConnection.
     * @param password
     *            密码
     * @param keyStorePath
     *            密钥库路径
     * @param trustStorePath
     *            信任库路径
     * @throws Exception
     */
    public static void initHttpsURLConnection(String password,
                                              String keyStorePath, String trustStorePath) throws Exception {
        // 声明SSL上下文
        SSLContext sslContext = null;
        // 实例化主机名验证接口
        HostnameVerifier hnv = new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };
        try {
            sslContext = getSSLContext(password, keyStorePath, trustStorePath);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
                    .getSocketFactory());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
}

    /**
     * 发送请求.
     * @param httpsUrl
     *            请求的地址
     * @param xmlStr
     *            请求的数据
     */
    public static void post(String httpsUrl, String xmlStr) {
        HttpsURLConnection urlCon = null;
        try {
            urlCon = (HttpsURLConnection) (new URL(httpsUrl)).openConnection();
            urlCon.setDoInput(true);
            urlCon.setDoOutput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-Length", String.valueOf(xmlStr.getBytes().length));
            urlCon.setUseCaches(false);
            //设置为gbk可以解决服务器接收时读取的数据中文乱码问题
            urlCon.getOutputStream().write(xmlStr.getBytes("gbk"));
            urlCon.getOutputStream().flush();
            urlCon.getOutputStream().close();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlCon.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试方法.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 密码
        String password = "Paic1234";
        // 密钥库
        String keyStorePath = "C:\\wenjin.jks";
        // 信任库
        String trustStorePath = "C:\\wenjin.jks";
        // 本地起的https服务
        String httpsUrl = "https://222.68.184.181:8007";
        // 传输文本
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><fruitShop><fruits><fruit><kind>萝卜</kind></fruit><fruit><kind>菠萝</kind></fruit></fruits></fruitShop>";
        HttpsPost.initHttpsURLConnection(password, keyStorePath, trustStorePath);
        // 发起请求
        HttpsPost.post(httpsUrl, xmlStr);

//        sendMsgToPingAn(xmlStr,httpsUrl,password, keyStorePath, trustStorePath);
    }

    private static String sendMsgToPingAn(String requestMsg,String httpsUrl,String password, String keyStorePath, String trustStorePath) {
        String responseStr = "";
        try {
            KeyStore ks = getKeyStore(password, keyStorePath);
            KeyStore ts = getKeyStore(password, trustStorePath);

            org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(org.apache.http.conn.ssl.SSLSocketFactory.SSL, ks, password,
                    ts, null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }, org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpClient httpclient = new DefaultHttpClient();

            Scheme sch = new Scheme("https", 8007, socketFactory);
            httpclient.getConnectionManager().getSchemeRegistry().register(sch);

            HttpPost httpPost = new HttpPost(httpsUrl);

            ContentType contentType = ContentType.TEXT_HTML.withCharset("gbk");
            StringEntity entity = new StringEntity(requestMsg, contentType);
            httpPost.setEntity(entity);

            HttpResponse httpResponse = httpclient.execute(httpPost);

            HttpEntity resEntity = httpResponse.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity);
            }
            httpclient.getConnectionManager().shutdown();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return responseStr;

    }
}
