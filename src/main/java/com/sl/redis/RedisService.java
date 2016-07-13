package com.sl.redis;

import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/6/23.
 */
@Service("redisService")
public interface RedisService {
    public String get(String key);
}
