package ThirdTask;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.gt;

public class ThirdTask {
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = mongoDatabase.getCollection("students");
        collection.drop(); //Удалим все записи из коллекции
        for (Student student : CsvParser.parseStudents()) { //Добавим в базу всех распарсеных студентов
            Document document = new Document("name", student.getName());
            document.append("age", student.getAge());
            document.append("courses", student.getCourses());
            collection.insertOne(document);
        }
        long studentsCount = collection.countDocuments();
        System.out.println("Students count: " + studentsCount);

        long ageGreaterThen40 = collection.countDocuments(gt("age", 40));
        System.out.println("Students with age greater than 40: " + ageGreaterThen40);
        String youngestStudent = collection.find()
                .sort(new Document("age", -1)).first().get("name").toString();
        System.out.println("Youngest student: " + youngestStudent);
        String oldestStudentCourseList = collection.find()
                .sort(new Document("age", 1)).first().get("courses").toString();
        System.out.println("List of oldest student courses:" + oldestStudentCourseList);
    }
}
