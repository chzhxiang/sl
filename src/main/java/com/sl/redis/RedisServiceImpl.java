package com.sl.redis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCluster;

/**
 * Created by Administrator on 2016/6/23.
 */
public class RedisServiceImpl implements RedisService {
    @Autowired
    private JedisCluster jedisCluster;

    public String get(String key){
        return jedisCluster.get(key);
    }
}
