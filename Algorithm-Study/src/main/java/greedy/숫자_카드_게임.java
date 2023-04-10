package greedy;

import java.util.Arrays;
import java.util.OptionalInt;

public class 숫자_카드_게임 {

    public static int solution1(int n, int m, int[][] arr) {
        int answer = 0;
        int temp = 0;

        for (int i = 0; i < n; i++) {
            temp = Arrays.stream(arr[i]).min().getAsInt();
//            Arrays.sort(arr[i]);
//            temp = arr[i][0];
            if (answer < temp) {
                answer = temp;
            }
        }

        return answer;
    }
}
