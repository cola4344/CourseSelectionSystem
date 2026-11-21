package model;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;


public class Student {

    private final String id;//学生id
    private final Set<String> courseCodes = new LinkedHashSet<>();//创建集合储存课程代码

    public Student(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        this.id = id.trim();
    }
    public String id() {
        return id;
    }
    public Collection<String> courseCodes() {
        return Collections.unmodifiableSet(courseCodes);//这里unmodifiable返回了一个不可以修改的集合，这样保证了课程代码不会被随意修改
    }
    public int courseCount() {
        return courseCodes.size();
    }//已选择的课程数量
    public boolean has(String canonicalCode) {
        return courseCodes.contains(canonicalCode);
    }//判断是否选择了对应的课程
    public boolean addCourse(String canonicalCode) {
        return courseCodes.add(canonicalCode);
    }//添加课程
    public boolean removeCourse(String canonicalCode) {
        return courseCodes.remove(canonicalCode);
    }//移除课程
    @Override
    public String toString() {
        return "学号 " + id + "，已选 " + courseCodes.size() + " 门：" + courseCodes;
    }//返回学生选课信息
}
