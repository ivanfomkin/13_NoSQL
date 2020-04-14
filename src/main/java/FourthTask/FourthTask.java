package FourthTask;

public class FourthTask {
    public static void main(String[] args) {
        MongoRepository repository = new MongoRepository();
        repository.addShop("Pyaterochka");
        repository.addProduct("Milk", 30);
    }
}
