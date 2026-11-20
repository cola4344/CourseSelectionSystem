package model;

public enum Weekday {

    MON("周一"),
    TUE("周二"),
    WED("周三"),
    THU("周四"),
    FRI("周五"),
    SAT("周六"),
    SUN("周日");

    private final String label;

    Weekday(String label) {
        this.label = label;
    }//枚举的构造方法是私有的这里

    /** 中文显示名，例如"周一"。 */
    public String label() {
        return label;
    }//外部访问枚举返回的值
    /** 对应 courses.txt 里保存的 1-7（周一=1，周日=7）。 */
    public int isoValue() {
        return ordinal() + 1;
    }//ordinal方法得到的是该枚举的顺序，比如MON为第一个定义的枚举，所以返回值为0，这里要加1

    public static Weekday ofIso(int value) {
        if (value < 1 || value > 7) {
            throw new IllegalArgumentException("星期取值必须在 1-7 之间，实际为 " + value);//这里将不满足的日期作为异常抛出
        }
        return values()[value - 1];//这里将前面得到的isovalue是星期数，这里转换为数组进行储存需要减1
    }
    /** 由中文字面量还原星期，例如"周三"。 */
    public static Weekday ofLabel(String text) {
        for (Weekday day : values()) {//这里的value方法的作用是将这个枚举形成一个数组进行放入增强for中，然后用临时变量day进行储存
            if (day.label.equals(text)) {
                return day;
            }
        }
        throw new IllegalArgumentException("无法识别的星期：" + text);
    }
}





