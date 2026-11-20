package model;

import java.util.Objects;

public class ClassTime {
    public static final int MIN_PERIOD = 1;//每天最少的课数
    public static final int MAX_PERIOD = 8;//每天最多的课数
    private final Weekday day;
    private final int startPeriod;
    private final int endPeriod;

    private ClassTime(Weekday day, int startPeriod, int endPeriod) {
        this.day = day;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }//这里将构造方法私有化的目的是防止外界随意创建数据，比如可能会产生星期8这种错误，这里用下面的of方法解决，将每一节作为对象进行创建，包含了日期，起始时间，结束时间这些属性

    public static ClassTime of(Weekday day, int start, int end) {
        Objects.requireNonNull(day, "星期不能为空");//requireNonNull方法的作用相当于抛出异常，如果传入的day是null就会报错
        if (start < MIN_PERIOD || start > MAX_PERIOD) {
            throw new IllegalArgumentException("开始节次必须在 " + MIN_PERIOD + "-" + MAX_PERIOD + " 之间：" + start);
        }//限制第几节课开始
        if (end < MIN_PERIOD || end > MAX_PERIOD) {
            throw new IllegalArgumentException("结束节次必须在 " + MIN_PERIOD + "-" + MAX_PERIOD + " 之间：" + end);
        }//限制第几节课结束
        if (start > end) {
            throw new IllegalArgumentException("开始节次不能大于结束节次：" + start + " > " + end);
        }
        return new ClassTime(day, start, end);//只有满足了上述条件的才可以成功创建对象
    }//of方法的目的是用上面的几个条件去框定创建的对象里面的属性，防止随意创建对象

    public static ClassTime parse(String text) {
        if (text == null) {
            throw new IllegalArgumentException("时间文本不能为空");
        }
        String trimmed = text.trim();//trim方法清除了多余的空格，这样可以去除用户多余的输入
        int dayIndex = trimmed.indexOf('第');//index方法得到的是该字符存在的索引，如果不存在即返回-1
        if (dayIndex <= 0 || !trimmed.endsWith("节")) {
            throw new IllegalArgumentException("时间格式应为 周X第A-B节：" + text);
        }//endwith是字符串的最后一个字符是不是以该字符结尾，这里的作用是保证输入的字符串不是以“第”开头且保证它是以“节“结尾
        Weekday day = Weekday.ofLabel(trimmed.substring(0, dayIndex));//substring的作用是截取从0开始的到给定索引的字符串，
        //这里的作用是比如”周一第一节“截取周一两个字作为day的映射，oflabel相当于就是对day定义了一个新名字
        String range = trimmed.substring(dayIndex + 1, trimmed.length() - 1);
        //这里返回的是具体的时间比如”周一第一节“在这里就返回”第一节“
        int dash = range.indexOf('-');//截取range中”-“字符
        if (dash <= 0) {//可以保证range里面不存在”-“这个字符， 兼容"周一第5节"这种单节写法
            int single = Integer.parseInt(range.trim());//将range里面的数字转存到single当中
            return of(day, single, single);//创建class time对象，这里两个参数都是single是因为只有一节
        }
        int start = Integer.parseInt(range.substring(0, dash).trim());//拿到起始课时间
        int end = Integer.parseInt(range.substring(dash + 1).trim());//拿到结束课的时间
        return of(day, start, end);//返回对象
    }

    public Weekday day() {
        return day;
    }

    public int startPeriod() {
        return startPeriod;
    }

    public int endPeriod() {
        return endPeriod;
    }//作为方法让供外部使用返回day，start，end属性

    public boolean overlaps(ClassTime other) {//判断课程时间是否重复
        if (other == null) {
            return false;
        }//如果该时间没有其他课则返回false，没有重复
        if (this.day != other.day) {
            return false;//如果这两个课程的日期不一样则返回false
        }
        return this.startPeriod <= other.endPeriod && other.startPeriod <= this.endPeriod;
        //如果这里a课的起始时间小于b课的结束时间且b课的开始时间小于a课的结束时间那么就说明课程重叠反之则没有

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClassTime classTime = (ClassTime) o;
        return startPeriod == classTime.startPeriod && endPeriod == classTime.endPeriod && day == classTime.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, startPeriod, endPeriod);
    }//这里重写这两个方法的目的是如果以后要对这个项目进行增添和classtime有关的比较时可以直接用
}