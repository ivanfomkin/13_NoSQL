package fourthTask;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

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
        if (fieldExist(shops, "name", shopName)) {
            System.out.println("Shop " + shopName + " already exist!");
        } else {
            shops.insertOne(new Document("name", shopName).append("products", new ArrayList<>()));
            System.out.println("New shop: " + shopName);
        }
    }

    public void addProduct(String productName, int price) {
        if (fieldExist(products, "name", productName)) {
            System.out.println("Product " + productName + " already exist!");
        } else {
            products.insertOne(new Document("name", productName).append("price", price));
            System.out.println("New product: " + productName + ". Price: " + price);
        }
    }

    public void insertProductToShop(String productName, String shopName) {
        boolean doesNamesExist = fieldExist(shops, "name", shopName)
                && fieldExist(products, "name", productName);
        if (doesNamesExist) {
            shops.updateOne(eq("name", shopName),
                    new Document("$addToSet", new Document("products", productName)));
            System.out.println("New product (" + productName + ") has been added to the shop " + shopName);
        } else {
            System.out.println("Error: one or more field(s) not exist");
        }
    }

    public void stats() {
        try {
            long productsCount = products.countDocuments();
            Double averagePrice = products.aggregate(Arrays.asList(Aggregates.group("_id", new BsonField("averagePrice",
                    new BsonDocument("$avg", new BsonString("$price"))))))
                    .first().getDouble("averagePrice");
            String lowerPriceProduct = products.find().sort(new Document("price", 1))
                    .first().get("name").toString();
            int lowerPrice = products.find().sort(new Document("price", 1))
                    .first().getInteger("price");
            String maxPriceProduct = products.find().sort(new Document("price", -1))
                    .first().get("name").toString();
            int maxPrice = products.find().sort(new Document("price", -1))
                    .first().getInteger("price");
            long priceGt100 = products.countDocuments(gt("price", 100));
            System.out.println("\n===================STATS==================");
            System.out.println("Counts of products: " + productsCount);
            System.out.println("Average price of products is " + averagePrice + " rubles");
            System.out.println("Product with lower price: " + lowerPriceProduct + " with price " + lowerPrice + " rubles");
            System.out.println("Product with biggest price: " + maxPriceProduct + " with price " + maxPrice + " rubles");
            System.out.println("Products count with price greater than 100: " + priceGt100);
        } catch (Exception e) {
            System.out.println("Error! Try to add products and shops and try again later");
        }
    }

    public void stats(String shopName) {
        if (shopName.equalsIgnoreCase("all")) {
            for (String shop : this.getAllShops()) {
                stats(shop);
            }
        } else {
            try {
                List<Bson> request = new ArrayList<>(Arrays.asList( //В этом листе будем формировать запрос к БД
                        lookup("shops", "name", "products", "products_in_shop"),
                        unwind("$products_in_shop"),
                        match(eq("products_in_shop.name", shopName))
                ));

                Bson groupBson = group("products_in_shop", //Тут пропишем group
                        sum("productsCount", 1),
                        avg("averagePrice", "$price"),
                        max("maxPriceProduct", "$price"),
                        min("lowerPriceProduct", "$price")
                );
                request.add(groupBson);
                int productsCount = products.aggregate(request).first().getInteger("productsCount");
                double averagePrice = products.aggregate(request).first().getDouble("averagePrice");
                int lowerPrice = products.aggregate(request).first().getInteger("lowerPriceProduct");
                int maxPrice = products.aggregate(request).first().getInteger("maxPriceProduct");
                //Ниже обновим запрос к БД так, чтобы не было Exception, если не удалить
                //groupBson, а сразу добавить функцию lt() - ошибка. Как вариант решения - вынести получение
                //количества продуктов, с ценой более 100 - в отдельный метод
                request.remove(groupBson);
                request.add(match(gt("price", 100)));
                request.add(groupBson);
                int priceGt100 = products.aggregate(request).first().getInteger("productsCount");
                System.out.println("\n===================STATS==================");
                System.out.println("Counts of products in " + shopName + ": " + productsCount);
                System.out.println("Average price of products in " + shopName + ": " + averagePrice + " rubles");
                System.out.println("Minimum price in " + shopName + ": " + lowerPrice + " rubles");
                System.out.println("Maximum price in " + shopName + ": " + maxPrice + " rubles");
                System.out.println("Products count with price greater than 100 in " + shopName + ": " + priceGt100);
            } catch (Exception e) {
                System.out.println("Can't get stats for " + shopName + " now. Try again later");
            }
        }
    }

    private List<String> getAllShops() {
        List<String> shopNames = new ArrayList<>();
        shops.find().forEach(t -> shopNames.add(t.getString("name")));
        return shopNames;
    }

    private boolean fieldExist(MongoCollection collection, String fieldName, Object fieldValue) { //Метод проверяет, существует ли поле в коллекци
        FindIterable<Document> element = collection.find(new Document(fieldName, fieldValue));
        if (element.first() == null) return false;
        return true;
    }
}
