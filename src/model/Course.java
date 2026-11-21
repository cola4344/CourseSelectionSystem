package model;
import java.util.Locale;
import java.util.Objects;
public class Course {
    private final String code;//课程代码
    private final String name;//课程名字
    private final int creditTenths;//学分，这里用40来代表4.0的学分，因为学分如果出现浮点数可能会因为浮点数精度的问题带来不方便
    private final ClassTime time;//时间
    private final int capacity;//可容纳的学生数量
    private int enrolled;//已经注册的学生数量
    public Course(String code, String name, int creditTenths, ClassTime time, int capacity, int enrolled) {
        this.code = canonicalCode(code);//统一code传入的大小写
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("课程名称不能为空");
        }//传入的名字不能为空
        if (creditTenths < 10 || creditTenths > 40) {
            throw new IllegalArgumentException("学分必须在 1.0-4.0 之间：" + creditTenths / 10.0);
        }//学分的限制
        if (capacity <= 0) {
            throw new IllegalArgumentException("课程容量必须为正数：" + capacity);
        }
        if (enrolled < 0 || enrolled > capacity) {
            throw new IllegalArgumentException("已选人数 " + enrolled + " 超出 0-" + capacity + " 的合法范围");
        }
        this.name = name.trim();//将用户输入的空格等符号删去
        this.creditTenths = creditTenths;
        this.time = Objects.requireNonNull(time, "上课时间不能为空");
        this.capacity = capacity;
        this.enrolled = enrolled;
    }
    public static String canonicalCode(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("课程编号不能为空");
        }
        String trimmed = raw.trim().toUpperCase(Locale.ROOT);//将raw中的字母转为大写，这里的Locale.root相当于就是一种转换的要求
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("课程编号不能为空");
        }
        return trimmed;
    }
    public String code() {
        return code;
    }

    public String name() {
        return name;
    }//向外界提供访问名字和课程代码的方法
    public int creditTenths() {
        return creditTenths;
    }//访问这节课的学分
    public String creditText() {
        return String.format(Locale.ROOT, "%.1f", creditTenths / 10.0);
    }//向外界展示学分，第一个参数是显示形式，第二个参数代表娴=显示的格式，第三个参数是传入的数值
    public double creditsAsDouble() {
        return creditTenths / 10.0;
    }//这里的double学分是用来进行比较的而不是真正的储存学分
    public ClassTime time() {
        return time;
    }

    public int capacity() {
        return capacity;
    }

    public int enrolled() {
        return enrolled;
    }
    public boolean isFull() {
        return enrolled >= capacity;
    }

    public int remaining() {
        return capacity - enrolled;
    }//计算还剩多少容量
    public void enrollOne() {
        if (isFull()) {
            throw new IllegalStateException("课程 " + code + " 已满（" + enrolled + "/" + capacity + "），不能继续增加人数");
        }
        enrolled++;
    }//防止有人直接调用方法使得人数超出上限，额外加一个检测，如果人数超出容量直接报错，而不是留下错误的数据
    public void dropOne() {
        if (enrolled <= 0) {
            throw new IllegalStateException("课程 " + code + " 已选人数为 0，无法再减少");
        }
        enrolled--;
    }//该方法用来处理退课逻辑，如果人数小于0仍然退课就会报错
    public void syncEnrolled(int value) {
        if (value < 0 || value > capacity) {
            throw new IllegalArgumentException("同步人数 " + value + " 超出 0-" + capacity + " 的合法范围");
        }
        this.enrolled = value;
    }//该方法实在程序加载阶段将选课结束后储存的txt文档数据导入到该对象中，它不会实际的参与选课和退课流程，而是作为一个储存机制
    @Override
    public String toString() {
        return code + " " + name + " " + creditText() + "学分 " + time
                + " 容量" + capacity + " 已选" + enrolled;
    }//返回与本节课有关的信息
}
