import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.Map;
import java.util.Set;

public class RedisTest {

    @Test
    public void run09() {
        Jedis jedis = new Jedis("hadoop102",6379);

        jedis.close();
    }

    @Test
    public void run08() {
        Jedis jedis = new Jedis("hadoop102",6379);

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
