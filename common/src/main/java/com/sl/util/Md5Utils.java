package com.sl.util;

import java.security.MessageDigest;

/**
 * Created by ZuoMJ on 15/4/14.
 * desc:
 */
public class Md5Utils {
    protected static final transient org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Md5Utils.class);

    public static String encode(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes("UTF-8"));
            return getEncode16(digest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static String encode32(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes("UTF-8"));
            return getEncode32(digest);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 32位加密
     *
     * @param digest
     * @return
     */
    private static String getEncode32(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    /**
     * 16位加密
     *
     * @param digest
     * @return
     */
    private static String getEncode16(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.substring(8, 24).toString();
    }
}
