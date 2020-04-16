package FourthTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FourthTask {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        MongoRepository repository = new MongoRepository();
        // Ниже закомментированны команды для проверки кода
//        repository.addShop("Pyaterochka");
//        repository.addProduct("Milk", 70);
//        repository.addProduct("Jus", 92);
//        repository.addProduct("Chocolate", 70);
//        repository.addProduct("Coffee", 230);
//        repository.insertProductToShop("Milk", "Pyaterochka");
//        repository.insertProductToShop("Coffee", "Pyaterochka");
//        repository.addProduct("Bacon", 350);
//        repository.insertProductToShop("Bacon", "Pyaterochka");
//        repository.insertProductToShop("Chocolate", "Pyaterochka");
//        repository.insertProductToShop("Jus", "Pyaterochka");
//        repository.stats();
//        repository.shopStats("Pyaterochka");
        System.out.println("This is the simple program for shops management");
        printCommandList();
        while (true) {
            System.out.print("Enter command: ");
            try {
                String input = reader.readLine();
                if (input.equalsIgnoreCase("exit")) break;

                if (input.equalsIgnoreCase("stats")) repository.stats();
                else {
                    String[] commands = input.split(" ");
                    if (commands.length == 2 && commands[0].equalsIgnoreCase("add_shop")) {
                        repository.addShop(commands[1]);
                    } else if (commands.length == 3 && commands[0].equalsIgnoreCase("add_product")) {
                        try {
                            repository.addProduct(commands[1], Integer.parseInt(commands[2]));
                        } catch (Exception e) {
                            System.out.println("Error!");
//                            e.printStackTrace();
                        }
                    } else if (commands.length == 3 && commands[0].equalsIgnoreCase("insert_product")) {
                        repository.insertProductToShop(commands[1], commands[2]);
                    } else if (commands.length == 2 && commands[0].equalsIgnoreCase("stats")) {
                        repository.shopStats(commands[1]);
                    } else {
                        System.out.println("Error! Wrong command!");
                        printCommandList();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printCommandList() {
        System.out.println("Command list: \nEXIT - close the program" +
                "\nADD_SHOP *shop name* - add new shop (f.e. ADD_SHOP Spar)" +
                "\nADD_PRODUCT *product* *price* - create new product (f.e. ADD_PRODUCT Milk 70)" +
                "\nINSERT_PRODUCT *product* *shop* - insert existing product to existing shop " +
                "(f.e. INSERT_PRODUCT Milk Spar)" +
                "\nSTATS - show statistic for all products" +
                "\nSTATS ALL - show statistic for all shops" +
                "\nSTATS *shop name* - show statistic for shop");
    }
}
