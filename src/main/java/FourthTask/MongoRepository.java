package FourthTask;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public class MongoRepository {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> products;
    private MongoCollection<Document> shops;

    public MongoRepository() {
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        this.mongoDatabase = mongoClient.getDatabase("test");
        this.products = mongoDatabase.getCollection("products"); //Товары
        this.shops = mongoDatabase.getCollection("shops");
        products.drop(); //Удалим все записи из коллекциий
        shops.drop();
    }

    public void addShop(String shopName) {
        shops.insertOne(new Document("name", shopName).append("products", new ArrayList<>()));
    }

    public void addProduct(String productName, int price) {
        products.insertOne(new Document("name", productName).append("price", price));
    }
}
