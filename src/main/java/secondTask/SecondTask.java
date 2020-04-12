package secondTask;

public class SecondTask {
    public static void main(String[] args) {
        RedisService redisService = new RedisService("USERS");
        redisService.start();
    }
}