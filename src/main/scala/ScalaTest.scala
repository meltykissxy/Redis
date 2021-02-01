import tools.RedisUtilScala.getJedisClient

object ScalaTest {
    def main(args: Array[String]): Unit = {
        val jedisClient = getJedisClient
        println(jedisClient.ping())
        jedisClient.close()
    }
}
