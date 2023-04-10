package implementation;

import java.util.Arrays;

public class 상하좌우 {

    private static int[] touristPosition = {1, 1};

    public static int[] solution1(int n, String[] move) {

        for (String s : move) {
            switch (s) {
                case "U":
                    if (touristPosition[0] != 1) {
                        touristPosition[0]--;
                    }
                    break;
                case "D":
                    if (touristPosition[0] != n) {
                        touristPosition[0]++;
                    }
                    break;
                case "L":
                    if (touristPosition[1] != 1) {
                        touristPosition[1]--;
                    }
                    break;
                case "R":
                    if (touristPosition[1] != n) {
                        touristPosition[1]++;
                    }
                    break;
            }
        }

        return touristPosition;
    }

}
