package pub_sub;

import redis.clients.jedis.Jedis;

/**
 * 发布
 */
public class RedisPubTest {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("127.0.0.1",6379);
        RedisMsgSubListener msgSubListener=new RedisMsgSubListener();
        jedis.publish("cctv","hello cctv2");
    }
}
