import redis.clients.jedis.Jedis;
import tools.RedisUtilJava;

import java.io.IOException;

public class JavaTest {
    public static void main(String[] args) throws IOException {
        Jedis jedis = RedisUtilJava.getJedis();
        System.out.println(jedis.ping());
        jedis.close();
    }
}
