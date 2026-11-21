package store;
import model.Student;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class StudentRoster {
    public static final int PRESET_COUNT = 50;//学生的总数
    private static final String ID_PREFIX = "2024";
    private static final int FIRST_INDEX = 1;
    //设计学生的学号，从“2024001”开始，这里把学号拆开更方便后面的维护
    private final Map<String, Student> byId = new LinkedHashMap<>();//用来储存学生对象
    public void add(Student student) {
        if (byId.containsKey(student.id())) {
            throw new IllegalArgumentException("学号重复：" + student.id());
        }
        byId.put(student.id(), student);//通过学号来添加学生对象
    }
    public Student find(String rawId) {
        if (rawId == null) {
            return null;
        }
        return byId.get(rawId.trim());
    }//根据学号来查找学生对象
    public Collection<Student> all() {
        return byId.values();
    }//将所有学生存入到一个集合当中

    public int size() {
        return byId.size();
    }
    public static StudentRoster preset() {
        StudentRoster roster = new StudentRoster();//这里调用的隐藏的无参构造
        for (int i = FIRST_INDEX; i < FIRST_INDEX + PRESET_COUNT; i++) {
            String id = ID_PREFIX + String.format(Locale.ROOT, "%03d", i);//这里可以展示类似“001”的形式
            roster.add(new Student(id));
        }
        return roster;
    }//调用该方法可以很快的获得一个存满学生对象的集合
}
