package pub_sub;

import redis.clients.jedis.Jedis;

/**
 * 订阅
 */
public class RedisSubTest {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("127.0.0.1",6379);
        RedisMsgSubListener msgSubListener=new RedisMsgSubListener();
        jedis.subscribe(msgSubListener,"cctv");
    }
}
