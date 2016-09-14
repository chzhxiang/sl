package com.sl.support.filter;

import com.alibaba.havana.sso.api.ticket.TicketService;
import com.alibaba.havana.sso.domain.ticket.AppLoginInfo;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.yunpc.dm.Constants;
import com.yunpc.dm.service.impl.SystemConfigService;
import com.yunpc.dm.support.Tools;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录过滤器
 * 
 * @author Felix Lee
 * @date 2015/01/11
 */

public class AutoLoginFilter extends OncePerRequestFilter implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(AutoLoginFilter.class);
    private static final Logger havanaLogger = LoggerFactory.getLogger("havanaLogger");

    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_URL = "/logout";
    private static final Set<String> notFilterDirs = Sets.newHashSet("/omadm", "/orginfo",
            "/static/", "/check.html", "/checkpreload.htm", "/status.taobao", "/favicon.ico", "/50x.html");
    @Resource
    private TicketService ticketService;

    public TicketService getTicketService() {
        return ticketService;
    }

    public void setTicketService(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    private List<String> needLoginUrls;

    private List<String> indexUrls;

    public List<String> getIndexUrls() {
        return indexUrls;
    }

    public void setIndexUrls(List<String> indexUrls) {
        this.indexUrls = indexUrls;
    }

    public List<String> getNeedLoginUrls() {
        return needLoginUrls;
    }

    public void setNeedLoginUrls(List<String> needLoginUrls) {
        this.needLoginUrls = needLoginUrls;
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
    }

    private boolean notFilter(String requestURI) {
        for (String url : notFilterDirs) {
            if (requestURI.startsWith(url.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI().toLowerCase();
        if (notFilter(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (LOGIN_URL.equals(requestURI) || LOGOUT_URL.equals(requestURI)) {
            request.getSession().invalidate();
            clearCookie(request, response, "/");
            filterChain.doFilter(request, response);
            return;
        }

        String ticket = Strings.nullToEmpty(Tools.resolveCookie(request, Constants.LOGIN_TAOBAO_NAME));
        String oldTicket = request.getSession() == null ? "" : (String) request.getSession().getAttribute("oldTicket");
        boolean needHavana = StringUtils.isBlank(ticket) || !ticket.equals(Strings.nullToEmpty(oldTicket));

        /**
         * 用户cookie为空，验证havana
         */
        if(needHavana){
            havanaOauth(request, response);
        }

        /**
         * 判断是否已写入服务端
         */
        oldTicket = (String) request.getSession().getAttribute("oldTicket");//重新取得服务端ticket
        if (StringUtils.isBlank(oldTicket)) {
            response.sendRedirect(LOGIN_URL);
            return;
        }

        for (String url : needLoginUrls) {
            if (requestURI.indexOf(url.toLowerCase()) != -1) {
                AppLoginInfo loginInfo = (AppLoginInfo) request.getSession().getAttribute("loginInfo");
                if (null == loginInfo) {
                    response.sendRedirect(LOGIN_URL);
                    return;
                }
            }
        }

        request.getSession().setAttribute("indexUrl", "");
        for (String url : indexUrls) {
            if (requestURI.indexOf(url.toLowerCase()) != -1) {
                request.getSession().setAttribute("indexUrl", url.replace("/", ""));
            }
        }

        ServletContext sc = SystemConfigService.getWebServletContext();
        //资源表中所有需要被限制的url
        Map<String, String> urlMap = (Map<String, String>)
                sc.getAttribute(Constants.ALL_RESOURCE_MAP);

        //用户所拥有的可以访问的url
        Map<String, String> userUrlsMap = (Map<String, String>)
                request.getSession().getAttribute(Constants.USERS_RESOURCE_MAP);

        //权限验证
        boolean permission = this.judgePermission(urlMap, userUrlsMap, request);

        if (!permission) { //不具备该权限的返回登陆页

            if (isAjaxRequest(request)) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().print("{\"status\": 0 , \"msg\": \"您无权限请求该链接!\" }");
            } else {
                response.sendRedirect(LOGIN_URL);
                return;
            }
        }
        doFilter(request, response, filterChain);
    }

    /**
     * havana验证st，处理session和cookie
     * 
     * @param request
     * @param response
     */
    private void havanaOauth(HttpServletRequest request,
            HttpServletResponse response) {
        String st = ServletRequestUtils.getStringParameter(request, "st", "");
        if (StringUtils.isNotBlank(st)) {
            AppLoginInfo loginInfo = ticketService.validateSt(st, Constants.LOGIN_APPNAME);
            if (null != loginInfo) {
                if (null == request.getSession().getAttribute(Constants.LOGIN_TAOBAO_NAME)
                        || null == request.getSession().getAttribute("loginId")
                        || null == request.getSession().getAttribute("loginInfo")) {
                    String ticket = DigestUtils.md5Hex(loginInfo.getUsername());
                    request.getSession().setAttribute("oldTicket", ticket);
                    request.getSession().setAttribute("loginInfo", loginInfo);
                    request.getSession().setAttribute("loginId", loginInfo.getUsername());
                    request.getSession().setAttribute("sessionId", loginInfo.getSessionId());

                    Cookie loginNameCookie = new Cookie(Constants.LOGIN_TAOBAO_NAME, ticket);
                    loginNameCookie.setMaxAge(7 * 24 * 60 * 60);
                    loginNameCookie.setPath("/");
                    loginNameCookie.setHttpOnly(true);
                    response.addCookie(loginNameCookie);
                    havanaLogger.info(st + "|" + request.getSession().getAttribute("loginId") + "|" + request.getSession().getAttribute("sessionId"));
                } else {
                    havanaLogger.info(st + "||");
                }
            }
        } else {
            havanaLogger.info("st is null" + "||");
        }
    }

    private void clearCookie(HttpServletRequest request, HttpServletResponse response, String path) {
        Cookie[] cookies = request.getCookies();
        try {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = new Cookie(cookies[i].getName(), null);
                cookie.setMaxAge(0);
                cookie.setPath(path);
                response.addCookie(cookie);
            }
        } catch (Exception ex) {
            log.error("Clear Cookies Exception！");
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        if (header != null && "XMLHttpRequest".equalsIgnoreCase(header))
            return true;
        else
            return false;
    }

    /**
     * 判断用户是否有权限进行该请求
     * 返回true 代表具有该权限 返回false代表不具备该权限
     * 
     * @return
     */
    private Boolean judgePermission(Map<String, String> AllUrls
            , Map<String, String> userUrls, HttpServletRequest request) {

        //验证参数 判断参数是否完整
        if (AllUrls == null) {
            return false;
        }

        boolean falg = true;
        //获取完整的uri
        String requestUri = this.getCompleteUri(request);
        StringBuffer uri = new StringBuffer();

        if (requestUri.contains("?action=")) {
            uri.append(requestUri.split("&")[0]);
        } else {
            uri.append(requestUri.split("[?]")[0]);
        }

        if (AllUrls.containsKey(uri.toString())) { //判断是否在过滤的名单中
            if (!userUrls.containsKey(uri.toString())) { //判断用户是有具该权限
                falg = false; //不具备该权限 ， 返回false
            }
        }
        return falg;
    }

    /**
     * 获取完整的请求的url
     * 
     * @param request
     * @return
     */
    private String getCompleteUrl(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer("");
        sb.append(request.getRequestURL().toString() + "?");

        Enumeration tmp = request.getParameterNames();
        int tmpint = 0;
        while (tmp.hasMoreElements()) {
            tmpint++;
            String parameter = (String) tmp.nextElement();
            String value = request.getParameter(parameter);
            if (tmpint == 1) {
                sb.append("");
            } else {
                sb.append("&");
            }
            sb.append(parameter + "=" + value);
        }
        return sb.toString();
    }

    /**
     * 获取完整的请求的uri
     * 
     * @param request
     * @return
     */
    private String getCompleteUri(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer("");
        sb.append(request.getRequestURI().toString() + "?");

        Enumeration tmp = request.getParameterNames();
        int tmpint = 0;
        while (tmp.hasMoreElements()) {
            tmpint++;
            String parameter = (String) tmp.nextElement();
            String value = request.getParameter(parameter);
            if (tmpint == 1) {
                sb.append("");
            } else {
                sb.append("&");
            }
            sb.append(parameter + "=" + value);
        }
        return sb.toString();
    }

}