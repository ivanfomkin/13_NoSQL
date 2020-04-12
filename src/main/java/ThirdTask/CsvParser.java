package ThirdTask;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    public static List<Student> parseStudents() {
        List<Student> students = new ArrayList<>();
        final String pathToFile = "src/main/resources/mongo.csv";

        try {
            List<String> lines = Files.readAllLines(Paths.get(pathToFile)); //Все линии в файле
            for (String line : lines) {
                String[] parts = line.split(",", 3);
                Student student = new Student(parts[0], Integer.parseInt(parts[1]), parts[2]);
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }
}
