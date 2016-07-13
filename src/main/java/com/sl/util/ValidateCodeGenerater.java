/**
 * 
 * 惠购 - 惠购网 - www.huiget.com - 特别会购！
 * Copyright © 2014 惠购 www.huiget.com 版权所有
 */
package com.sl.util;

import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 
 * @author yaofang
 * @version $Id: ActiveCodeGenerater.java, v 0.1 2014年7月17日 上午11:06:45 yaofang Exp $
 */
public class ValidateCodeGenerater {

    public synchronized static String generate(int userId, String target, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append(userId);
        sb.append(target);
        sb.append(type);
        sb.append(System.nanoTime());
        sb.append(getRandomSalt(10));

        return DigestUtils.md5Hex(sb.toString());
    }

    public synchronized static String generate() {
        return getRandomSalt(6);
    }

    public synchronized static String generateSid() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.nanoTime());
        sb.append(getRandomSalt(10));

        return DigestUtils.md5Hex(sb.toString());
    }

    /**
     * Generates a string of random chars from the B64T set.
     *
     * @param num
     *            Number of chars to generate.
     */
    static String getRandomSalt(final int num) {
        final StringBuilder saltString = new StringBuilder();
        for (int i = 1; i <= num; i++) {
            saltString.append(new Random().nextInt(10));
        }
        return saltString.toString();
    }

}
