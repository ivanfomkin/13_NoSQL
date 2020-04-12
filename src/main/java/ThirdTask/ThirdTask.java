package ThirdTask;

public class ThirdTask {
    public static void main(String[] args) {
        CsvParser.parseStudents().forEach(System.out::println);
    }
}
