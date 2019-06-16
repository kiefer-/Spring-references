package me.shouheng.service.common.util;

import redis.clients.jedis.Jedis;

/**
 * @author shouh, 2019/6/8-16:54
 */
public class JedisUtils {

    public static void release(Jedis conn) {
        if (conn != null) {
            conn.close();
        }
    }
}
