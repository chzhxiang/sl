package com.sl.redis.api;

import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/6/23.
 */
public interface RedisService {
    public String get(String key);
}
