
import com.example.ServiceApplication;
import com.example.com.springboot.pojo.test.UserRedis;
import com.example.com.springboot.service.AddressService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class RedisTest {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    AddressService service;
    @Test
    public void insertRedis(){
       UserRedis user=service.getUser("tony");
       System.out.println(user);
    }

    @Test
    public void setTest(){//可以用于共同关注 取交并集
        Jedis jedis=new Jedis("127.0.0.1",6379);
        jedis.sadd("A","jack","rose","poll");
        jedis.sadd("B","jack","peanut");
        System.out.println(jedis.sinter("A","B"));//取交集
        jedis.close();
    }

    @Test
    public void listTest(){
        Jedis jedis=new Jedis("127.0.0.1",6379);
        jedis.lpush("1001","A");
        jedis.lpush("1001","Z");
        jedis.lpush("1001","C","T");
        jedis.lrange("1001",0,-1);
        while (true){
            String item=jedis.lpop("1001");
            if(item!=null){
                System.err.println(item);
            }
            break;
        }
    }

    @Test
    public void hashTest(){//类似于hashmap
        Jedis jedis=new Jedis("127.0.0.1",6379);
        jedis.hset("student","name","jack");
        jedis.hset("student","age","11");
        jedis.hset("stduent","hoppy","basketball");
        System.out.println(jedis.hget("student","name"));
        System.out.println(jedis.hgetAll("student"));
    }
    @Test
    public void zSetTest(){//可以用于游戏排行榜排序set
        Jedis jedis=new Jedis("127.0.0.1",6379);
        jedis.zadd("QQ",96,"jack");
        jedis.zadd("QQ",62,"rose");
        jedis.zadd("QQ",33,"pckl");
        jedis.zadd("QQ",76,"pool");
        Set<String> qq = jedis.zrange("QQ", 0, 2);
        for(String name:qq){
            System.out.println(name);
        }
        System.out.println(jedis.zcount("QQ",60,90));
    }
}
