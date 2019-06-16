package me.shouheng.service.basic;

import me.shouheng.service.base.SpringBaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author shouh, 2019/6/8-16:57
 */
public class RedisEnvTest extends SpringBaseTest {

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void testRedisConnection() {
        Jedis jedis = jedisPool.getResource();
        jedis.set("the-key", "the-value");
        String value = jedis.get("the-key");
        Assert.assertEquals(value, "the-value");
    }
}
