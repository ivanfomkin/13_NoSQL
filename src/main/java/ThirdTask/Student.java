package ThirdTask;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
public class Student {
//    private ObjectId id;
    private String name;
    private int age;
    private String courses;
}
