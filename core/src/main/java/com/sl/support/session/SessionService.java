package com.sl.support.session;

import com.sl.redis.api.RedisService;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.TairManager;
import com.taobao.tair.impl.mc.MultiClusterTairManager;
import com.yunpc.dm.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.util.HashMap;
import java.util.Map;

public class SessionService {

	private static SessionService instance = null;

    //缓存时间,单位：秒,设置写入后的过期时间
    private static final int CACHE_TIME = 24 * 60 * 60;
    private static final String SESSION_FLAG_PREFIX = "session_cache_";
    //缓存服务数据结构
    private static RedisService redisService = getRedisService();
    private final static synchronized RedisService getRedisService(){
        if(redisService == null) {
            ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
            redisService = (RedisService) ctx.getBean("redisService");
        }
        return redisService;
    }

	public static synchronized SessionService getInstance() {
		if (instance == null) {
			instance = new SessionService();
		}
		return instance;
	}

	private SessionService() {
	}

	public Map getSession(String id) {
        Result<DataEntry> result = getRedisService().get(SESSION_FLAG_PREFIX+id);
        HashMap<String, Object> m = null;
        if (result != null && result.getValue() != null) {
            m = ( HashMap<String, Object>)result.getValue().getValue();
        }
        if (m == null) {
            return new HashMap();
        }
        return m;

    }

	public void saveSession(String id, Map session) {
        int cacheTime = CACHE_TIME;
        getRedisService().put(SESSION_FLAG_PREFIX+id, (java.io.Serializable) session, 0, cacheTime);
    }

	public void removeSession(String id) {
        Result<DataEntry> result = getRedisService().get(SESSION_FLAG_PREFIX+id);
        if(result!=null) {
            getRedisService().delete(SESSION_FLAG_PREFIX + id);
        }
	}

}
