/**
 * 
 * 惠购 - 惠购网 - www.huiget.com - 特别会购！
 * Copyright © 2014 惠购 www.huiget.com 版权所有
 */
package com.sl.util.web.context;

/**
 * 
 * @author yaofang
 * @version $Id: WebContextHolder.java, v 0.1 2014年10月3日 上午10:11:13 yaofang Exp $
 */
public class WebContextHolder {

    private static final ThreadLocal<WebContext> WEB_CONTEXT_HOLDER = new ThreadLocal<WebContext>();

    public WebContextHolder() {
    }

    public static WebContext get() {
        return WEB_CONTEXT_HOLDER.get();
    }

    public static void set(WebContext context) {
        WEB_CONTEXT_HOLDER.set(context);
    }

    public static void remove() {
        WEB_CONTEXT_HOLDER.set(null);
    }

}
