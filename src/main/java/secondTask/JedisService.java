package secondTask;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JedisService {
    private Jedis jedis;
    private final String KEY;

    public JedisService(String key) {
        this.jedis = new Jedis("localhost");
        this.KEY = key;
        generateUsers();
    }

    private List<String> getUsers() {
        return jedis.lrange(KEY, 0, jedis.llen(KEY));
    }

    private void generateUsers() {
        jedis.del(KEY); //Чтобы не было задвоения, удалим все записи по нужному нам ключу
        //Ниже захардкодим наших пользователей, жаждущих любви
        List<String> names = new ArrayList<>(Arrays.asList("Халк", "Тони", "Грут",
                "Говард", "Тор", "Йонду", "Стивен", "Гамора", "Небула", "Наташа",
                "Пегги", "Ракета", "Один", "Мария", "Дракс", "Мантис", "Аиша",
                "Питер", "Локи", "Скотт"));

        for (int i = 0; i < names.size(); ) { //Тут присвоим номера всем именам, для наглядности и добавим в список
            String currentName = names.get(i);
            jedis.rpush(KEY, (++i + " - " + currentName));
        }
    }

    private String getRandomUser() {
        int randomNumber = (int) (Math.random() * jedis.llen(KEY));
        return jedis.lindex(KEY, randomNumber);
    }


    public void start() {
        int i = 0; //Переменная будет считать до 10
        while (true) {
            List<String> users = getUsers();
            i++;
            if (i % 10 == 0) {
                String randomUser = getRandomUser();
                users.remove(randomUser);
                System.out.println("Пользователь " + randomUser + " оплатил платное размещение");
                System.out.println(randomUser);
                i = 0;
            }
            users.forEach(System.out::println);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}