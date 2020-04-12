package firstTask;

import redis.clients.jedis.Jedis;

import java.util.Map;

public class FirstTask {

    public static void main(String[] args) {
        final String KEY = "Cities";
        Jedis jedis = new Jedis("localhost");
        Map<String, Double> cities = Map.of( //Захардкодим города
                "Milan", Double.valueOf(17460),
                "Rome", Double.valueOf(15965),
                "Vladivostok", Double.valueOf(23091),
                "Rio", Double.valueOf(74643),
                "Washington", Double.valueOf(23422),
                "Madrid", Double.valueOf(19323),
                "Berlin", Double.valueOf(14497),
                "Vienna", Double.valueOf(11727),
                "Paris", Double.valueOf(12212),
                "London", Double.valueOf(7232)
        );

        jedis.zadd(KEY, cities);
        System.out.println("Cities with lower cost tickets: ");
        jedis.zrangeWithScores(KEY, 0, 2).forEach(System.out::println);

        System.out.println("Cities with highest cost tickets: ");
        jedis.zrevrangeWithScores(KEY, 0, 2).forEach(System.out::println);
    }
}
