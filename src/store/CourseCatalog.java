package store;
import model.ClassTime;
import model.Course;
import model.Weekday;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public class CourseCatalog {
    private final Map<String, Course> byCode = new LinkedHashMap<>();//这里用map将课程与它对应的课程类储存

    public void add(Course course) {
        if (byCode.containsKey(course.code())) {
            throw new IllegalArgumentException("课程编号重复：" + course.code());
        }
        byCode.put(course.code(), course);
    }//这里如果添加了重复的课程防止数据被覆盖直接抛出异常

    public Course find(String rawCode) {//查找课程
        if (rawCode == null) {
            return null;
        }
        String canonical;
        try {
            canonical = Course.canonicalCode(rawCode);//统一课程代码编号
        } catch (IllegalArgumentException e) {
            return null;
        }//如果发现课程代码有误返回null
        return byCode.get(canonical);
    }

    public Collection<Course> all() {
        return byCode.values();//返回含有所有课程对象的集合
    }

    public int size() {
        return byCode.size();//得到有多少课程
    }

    public List<Course> browseOrder() {
        List<Course> list = new ArrayList<>(byCode.values());//将所有课程对象存放进一个集合中
        list.sort(Comparator.comparingInt(Course::creditTenths)
                .reversed()
                .thenComparing(Course::code));//用匿名内部类对集合进行排序，先按学分降次排序，再按课程代码升序排序
        return list;
    }
    public static CourseCatalog preset() {
        CourseCatalog catalog = new CourseCatalog();
        catalog.add(course("C001", "高等数学A", 40, Weekday.MON, 1, 2, 60));
        catalog.add(course("C002", "线性代数", 30, Weekday.MON, 3, 4, 55));
        catalog.add(course("C003", "大学英语I", 25, Weekday.TUE, 1, 2, 50));
        catalog.add(course("C004", "程序设计基础", 40, Weekday.TUE, 3, 4, 60));
        catalog.add(course("C005", "数据结构", 35, Weekday.WED, 1, 2, 45));
        catalog.add(course("C006", "离散数学", 30, Weekday.WED, 3, 4, 50));
        catalog.add(course("C007", "计算机组成原理", 35, Weekday.THU, 1, 2, 40));
        catalog.add(course("C008", "操作系统", 35, Weekday.THU, 3, 4, 40));
        catalog.add(course("C009", "计算机网络", 30, Weekday.FRI, 1, 2, 45));
        catalog.add(course("C010", "数据库系统", 30, Weekday.FRI, 3, 4, 45));
        catalog.add(course("C011", "大学物理", 40, Weekday.MON, 5, 6, 55));
        catalog.add(course("C012", "概率论与数理统计", 30, Weekday.TUE, 5, 6, 50));
        catalog.add(course("C013", "软件工程", 25, Weekday.WED, 5, 6, 40));
        catalog.add(course("C014", "人工智能导论", 20, Weekday.THU, 5, 6, 35));
        catalog.add(course("C015", "机器学习", 30, Weekday.FRI, 5, 6, 35));
        catalog.add(course("C016", "Java程序设计", 20, Weekday.MON, 7, 8, 60));
        catalog.add(course("C017", "Python数据分析", 20, Weekday.TUE, 7, 8, 45));
        catalog.add(course("C018", "大学生心理健康", 10, Weekday.SAT, 1, 2, 60));
        catalog.add(course("C019", "体育I", 10, Weekday.SAT, 3, 4, 60));
        catalog.add(course("C020", "创新创业实践", 15, Weekday.SUN, 1, 2, 30));
        return catalog;
    }//存入全部课程，周一第1-2节、周一第3-4节等分布在工作日和周末，方便演示时间冲突，里面存在相同的学分的课程用来展示排序
    private static Course course(String code, String name, int creditTenths,
                                 Weekday day, int start, int end, int capacity) {
        return new Course(code, name, creditTenths, ClassTime.of(day, start, end), capacity, 0);
    }
}

