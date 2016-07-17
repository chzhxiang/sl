/**
 * 
 * 惠购 - 惠购网 - www.huiget.com - 特别会购！
 * Copyright © 2014 惠购 www.huiget.com 版权所有
 */
package com.sl.util.web.context;

/**
 * 
 * @author yaofang
 * @version $Id: WebContext.java, v 0.1 2014年10月3日 上午10:10:36 yaofang Exp $
 */
public class WebContext {

    private String  sid;

    private int     loginId;

    private int     userId;

    private String  userName;

    private String  nickName;

    private String  ip;

    private boolean logon;

    private String  requestURI;

    private String  lastLoginTime;

    private int     cartCount;

    /**
     * Getter method for property <tt>sid</tt>.
     * 
     * @return property value of sid
     */
    public String getSid() {
        return sid;
    }

    /**
     * Setter method for property <tt>sid</tt>.
     * 
     * @param sid value to be assigned to property sid
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * Getter method for property <tt>loginId</tt>.
     * 
     * @return property value of loginId
     */
    public int getLoginId() {
        return loginId;
    }

    /**
     * Setter method for property <tt>loginId</tt>.
     * 
     * @param loginId value to be assigned to property loginId
     */
    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    /**
     * Getter method for property <tt>userId</tt>.
     * 
     * @return property value of userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Setter method for property <tt>userId</tt>.
     * 
     * @param userId value to be assigned to property userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Getter method for property <tt>userName</tt>.
     * 
     * @return property value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter method for property <tt>userName</tt>.
     * 
     * @param userName value to be assigned to property userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter method for property <tt>nickName</tt>.
     * 
     * @return property value of nickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Setter method for property <tt>nickName</tt>.
     * 
     * @param nickName value to be assigned to property nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Getter method for property <tt>ip</tt>.
     * 
     * @return property value of ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * Setter method for property <tt>ip</tt>.
     * 
     * @param ip value to be assigned to property ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Getter method for property <tt>logon</tt>.
     * 
     * @return property value of logon
     */
    public boolean isLogon() {
        return logon;
    }

    /**
     * Setter method for property <tt>logon</tt>.
     * 
     * @param logon value to be assigned to property logon
     */
    public void setLogon(boolean logon) {
        this.logon = logon;
    }

    /**
     * Getter method for property <tt>requestURI</tt>.
     * 
     * @return property value of requestURI
     */
    public String getRequestURI() {
        return requestURI;
    }

    /**
     * Setter method for property <tt>requestURI</tt>.
     * 
     * @param requestURI value to be assigned to property requestURI
     */
    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    /**
     * Getter method for property <tt>lastLoginTime</tt>.
     * 
     * @return property value of lastLoginTime
     */
    public String getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * Setter method for property <tt>lastLoginTime</tt>.
     * 
     * @param lastLoginTime value to be assigned to property lastLoginTime
     */
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * Getter method for property <tt>cartCount</tt>.
     * 
     * @return property value of cartCount
     */
    public int getCartCount() {
        return cartCount;
    }

    /**
     * Setter method for property <tt>cartCount</tt>.
     * 
     * @param cartCount value to be assigned to property cartCount
     */
    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }
}
