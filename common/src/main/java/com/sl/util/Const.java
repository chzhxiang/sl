package com.sl.util;

/**
 * Created by panglz on 2016/1/26.
 */
public class Const {
    public static String WX_APP_ID = "wx64445853f60fe3e4";
    public static String WX_APP_SECRET = "e72c4c8420c19843fc95323a97abad8a";
    public static String WX_URL_GET_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WX_APP_ID+"&secret="+WX_APP_SECRET;
    public static String WX_ACCESS_TOKEN = "";
    public static int WX_ACCESS_TOKEN_EXPIRES = 7200;
    public static String WX_URL_GET_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+WX_ACCESS_TOKEN+"&type=jsapi";
    public static String WX_JS_API_TICKET = "";
    public static int WX_JS_API_TICKET_EXPIRES = 7200;
}
