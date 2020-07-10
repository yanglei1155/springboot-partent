import com.example.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisCommands;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class GeoRedisTest {
    @Autowired
    StringRedisTemplate redisTemplate;

    public void addPoint(Point point, String userId){//名称 point是位置 userid用户
        redisTemplate.opsForGeo().add("userGeo",new RedisGeoCommands.GeoLocation<>(userId,point));
    }

    /**
     * 返回用户周围的人
     * @param point
     * @return
     */
    public GeoResult<RedisCommands.GeoLocation<String>> near(Point point){
        //半径100米
        Distance distance=new Distance(100, RedisCommands.DistanceUnit.METERS);
        Circle circle=new Circle(point,distance);
        //附近五个人
        RedisCommands.GeoRadiusCommandArgs geoRadiusCommandArgs=RedisCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().limit(5);
        GeoResults<RedisGeoCommands.GeoLocation<String>> userGeo = redisTemplate.opsForGeo().radius("userGeo", circle, geoRadiusCommandArgs);
        //return userGeo;
        return null;
    }


    @Test
    public void getGeo(){
        //上报位置
        addPoint(new Point(116.405258,39.904898),"jack");
        addPoint(new Point(116.405358,39.908898),"jxk");
        addPoint(new Point(116.405248,39.924898),"rose");
        addPoint(new Point(116.405558,39.908898),"pool");
        //获取附近人
        GeoResult<RedisGeoCommands.GeoLocation<String>> near = near(new Point(116.405558, 39.908898));
       /* for(GeoResults<RedisCommands.GeoLocation<String>> nearPeople:near){
            List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = nearPeople.getContent();
            //System.out.println(content.getName()+content.getDis());
        }*/
    }
}
