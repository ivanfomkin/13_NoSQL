package secondTask;

public class SecondTaskJedis {
    public static void main(String[] args) {
        JedisService jedisService = new JedisService("USERS");
        jedisService.start();
    }
}