package com.sl.redis.impl;

import com.sl.redis.api.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

/**
 * Created by Administrator on 2016/6/23.
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService {
    @Autowired
    private JedisCluster jedisCluster;

    public String get(String key){
        return jedisCluster.get(key);
    }
}
