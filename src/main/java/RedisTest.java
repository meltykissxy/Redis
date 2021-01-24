import org.junit.jupiter.api.Test;
import redis.clients.jedis.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisTest {

    @Test
    public void run13() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.close();
    }

    @Test
    public void run12() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.close();
    }

    // KISS Hyperloglog
    @Test
    public void run11() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.pfadd("hll1", "a", "b", "c", "d", "e", "f");
        jedis.pfadd("hll2", "e", "f", "g", "h", "i", "j");

        jedis.pfmerge("hll3", "hll1", "hll2");

        System.out.println(jedis.pfcount("hll3"));
        jedis.close();
    }

    @Test
    public void run10() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.geoadd("china:city", 116.38, 39.90, "天安门");
        jedis.geoadd("china:city", 116.42, 39.93, "东城区");
        jedis.geoadd("china:city", 116.37, 39.92, "西城区");
        jedis.geoadd("china:city", 116.30, 39.95, "海淀区");
        jedis.geoadd("china:city", 116.35, 39.87, "宣武区");
        jedis.geoadd("china:city", 116.28, 39.85, "丰台区");

        System.out.println(jedis.geopos("china:city", "西城区", "东城区")); //[(116.37000113725662,39.919999041618105), (116.41999751329422,39.93000105131286)]

        System.out.println(jedis.geodist("china:city", "西城区", "东城区", GeoUnit.KM));

        List<GeoRadiusResponse> georadius = jedis.georadius("china:city", 116.30, 39.95, 10, GeoUnit.KM);
        for (GeoRadiusResponse geoRadiusResponse : georadius) {
            System.out.println(geoRadiusResponse.getMemberByString());
        }
        //宣武区
        //海淀区
        //天安门
        //西城区

        jedis.close();
    }

    @Test
    public void run09() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.set("k12", "abcdefg");
        jedis.setrange("k12", 2, "ly");
        System.out.println(jedis.get("k12"));// ablyefg
        jedis.close();
    }

    @Test
    public void run08() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.set("i1", "13");
        jedis.incrBy("i1", 2);
        System.out.println(jedis.get("i1"));

        jedis.set("s1", "lss");
        jedis.append("s1", "lovely");
        jedis.strlen("s1");
        System.out.println(jedis.getrange("s1", 0, 3)); //右侧是闭区间，区别Java
        System.out.println(jedis.getrange("s1", 0, -1)); //右侧全部用-1
        jedis.close();
    }

    @Test
    public void run07() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.zadd("z1", 5, "lss");
        jedis.zadd("z1", 9, "yjy");
        jedis.zadd("z1", 3, "dxy");
        jedis.zadd("z1", 8, "ly");

        System.out.println(jedis.zcard("z1"));

        jedis.zadd("z1", 9, "yjy");
        System.out.println(jedis.zcard("z1"));

        jedis.zadd("z1", 10, "yjy");
        System.out.println(jedis.zcard("z1"));

        jedis.close();
    }

    @Test
    public void run06() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.hset("h1", "name", "lss");
        jedis.hset("h1", "age", "18");
        jedis.hset("h1", "gender", "girl");

        System.out.println(jedis.hgetAll("h1"));

        jedis.close();
    }

    @Test
    public void run05() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.sadd("s1", "lss", "yjy", "lxy", "dxy");
        System.out.println(jedis.smembers("s1"));
        jedis.close();
    }

    @Test
    public void run04() {
        Jedis jedis = new Jedis("hadoop102",6379);
        jedis.flushDB();
        jedis.set("k1", "v1");
        jedis.get("k1");
        // KISS 重命名key
        jedis.rename("k1", "k2");
        System.out.println(jedis.get("k2"));
        System.out.println(jedis.get("k1"));
        jedis.close();
    }

    @Test
    public void run03() {
        Jedis jedis = new Jedis("hadoop102",6379);
        // KISS 你看，其实你可以根据返回值1或0来判断之前有没有这个key，去重时就可以利用这个技巧哦
        System.out.println(jedis.setnx("s1", "18")); // 1
        System.out.println(jedis.setnx("s1", "16")); // 0
        jedis.close();
    }

    @Test
    public void run02() {
        Jedis jedis = new Jedis("hadoop102",6379);
        // KISS 简单的赋值，注意这里可以返回成功还是失败
        System.out.println(jedis.set("k1", "lss"));
        System.out.println(jedis.set("k2", "ly"));
        System.out.println(jedis.set("k3", "yjy"));
        System.out.println(jedis.set("a1", "dh"));
        System.out.println(jedis.set("b2", "chj"));
        // KISS 简单的获取key对应的值
        System.out.println(jedis.get("k1"));
        // KISS 获取所有key
        System.out.println(jedis.keys("*").toString());
        System.out.println(jedis.keys("k*").toString());
        System.out.println(jedis.keys("*1").toString());
        jedis.close();
    }

    // KISS 基础的连接Redis
    @Test
    public void run01() {
        Jedis jedis = new Jedis("hadoop102",6379);
        String pong = jedis.ping();
        System.out.println("连接成功："+pong);
        jedis.close();
    }



    public static void main(String[] args) {
        Jedis jedis = getJedis();
        jedis.set("k1000", "v1000");

        Set<String> keyset = jedis.keys("*");
        for (String key : keyset) {
            System.out.println(key);
        }

        Map<String, String> userMap = jedis.hgetAll("user:0101");
        for (Map.Entry<String, String> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        Set<Tuple> topTuple = jedis.zrevrangeWithScores("article:topn", 0, 2);
        for (Tuple tuple : topTuple) {
            System.out.println(tuple.getElement() + ":" + tuple.getScore());
        }

        System.out.println(jedis.ping());
        jedis.close();
    }

    private static JedisPool jedisPool=null ;

    public static  Jedis getJedis(){
        if(jedisPool == null){
            JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(100);
            jedisPoolConfig.setMinIdle(20);
            jedisPoolConfig.setMaxIdle(30);
            //资源耗尽时等待
            jedisPoolConfig.setBlockWhenExhausted(true);
            jedisPoolConfig.setMaxWaitMillis(5000);
            //从池中去连接后要进行测试
            //导致连接池中的连接坏掉： 1 服务器端重启过 2 网断过 3 服务器端维持空闲连接超时
            jedisPoolConfig.setTestOnBorrow(true);
            // jedisPoolConfig.setTestWhileIdle(true);
            //jedisPool=new JedisPool("192.168.81.8",6379 );
            jedisPool = new JedisPool(jedisPoolConfig, "192.168.81.8", 6379);
        }
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
}
