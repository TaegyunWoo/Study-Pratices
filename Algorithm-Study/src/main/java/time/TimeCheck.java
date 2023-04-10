package time;

public class TimeCheck {
    public static long startTime;
    public static long endTime;
    public static long spentTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static void end() {
        endTime = System.currentTimeMillis();
        spentTime = endTime - startTime;
        System.out.println("소요시간: " + spentTime + "ms");
    }
}
