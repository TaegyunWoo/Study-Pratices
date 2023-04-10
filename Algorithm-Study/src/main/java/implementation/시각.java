package implementation;

public class 시각 {

    private static int n;
    private static int hour = 0;
    private static int minute = 0;
    private static int second = 0;
    private static int answer = 0;

    public static int solution1(int input) {
        n = input;
        for (; hour <= n; hour++) {
            for (; minute < 60; minute++) {
                for (; second < 60; second++) {
                    if (String.valueOf(second).contains("3")) {
                        answer++;
                    } else if (String.valueOf(minute).contains("3")) {
                        answer++;
                    } else if (String.valueOf(hour).contains("3")) {
                        answer++;
                    }
                }
                second = 0;
            }
            minute = 0;
        }

        return answer;
    }

    public static int solution2(int input) {
        n = input;

        while (hour != -1) {
            if (String.valueOf(second).contains("3")) {
                answer++;
            } else if (String.valueOf(minute).contains("3")) {
                answer++;
            } else if (String.valueOf(hour).contains("3")) {
                answer++;
            }

            nextSecond();
        }

        return answer;
    }

    private static void nextSecond() {
        if (second != 59) {
            second++;
        } else {
            second = 0;
            nextMinute();
        }
    }

    private static void nextMinute() {
        if (minute != 59) {
            minute++;
        } else {
            minute = 0;
            nextHour();
        }
    }

    private static void nextHour() {
        if (hour < n) {
            hour++;
        } else {
            hour = -1;
        }
    }
}
