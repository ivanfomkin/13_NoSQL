package secondTask;

import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import java.util.*;

public class RedissonService {
    private RedissonClient redissonClient; //Клиент для работы с Redis в Redisson
    private RKeys rKeys; //Объект для работы с ключами в Redisson
    private RScoredSortedSet<String> users; //Тут будем хранить значения ключа KEY (пользователей)
    private final String KEY = "USERS";

    public RedissonService() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        try {
            redissonClient = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            System.out.println("Не удалось подключиться к Redis");
            System.out.println(Exc.getMessage());
        }
        rKeys = redissonClient.getKeys();
        users = redissonClient.getScoredSortedSet(KEY);
        generateUsers();
    }

    private void generateUsers() { //Сгенерируем пользователей с именами типа User - N
        rKeys.delete(KEY); //Удалим ключ из базы, чтобы не было дубликатов
        for (int i = 0; i < 20; ) {
            users.add(100, "User - " + ++i); //Заполним user, у всех ранг 100, то есть наименьший
        }
    }

    void shutdown() {
        redissonClient.shutdown();
    }

    public void siteWork() { //Метод эмулирует работу сайта
        while (true) {
            if (Math.random() >= 0.9) { //Вероятность покупки платной услуги пользователем - 10%
                String user = subscribe(); //Запомним пользователя, купившего платную услугу
                printUsers(users); //Выведем список
                users.addAndGetRank(100, user); //Обнулим рейтинг пользователя, купившего подписку
            } else //Иначе просто напечатаем пользователей
                printUsers(users);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printUsers(RScoredSortedSet<String> users) { //Печатает упорядоченных пользователей
        users.forEach(System.out::println);
    }

    private String getUser(int number) { //Получает пользователя по номеру
        String user = (String) users.toArray()[number];
        return user;
    }

    private String subscribe() { //Метод эмулирует покупку ранга пользователем и возвращает пользователя,
        //купившего ранг
        int userNumber = (int) (Math.random() * (users.size() - 1));
        String user = getUser(userNumber);
        users.add(1, user); //У пользователя будет максимальный ранг - 1
        System.out.println("New subscribe! " + user);
        return user;
    }
}
