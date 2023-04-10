package greedy;

import java.util.Arrays;

public class 큰_수의_법칙 {

    public static int solution1(int n, int m, int k, int[] arr) {
        int answer = 0;
        int biggestNum = 0;
        int secondaryBigNum = 0;

        //가장 큰 수와 2번째로 큰 수 찾기
        for (int i : arr) {
            if (biggestNum < i) {
                biggestNum = i;
            }
        }
        for (int i : arr) {
            if (secondaryBigNum < i && biggestNum != i) {
                secondaryBigNum = i;
            }
        }

        //정답 찾기
        int temp = 0;
        for (int i = 0; i < m; i++) {
            if (temp < k) {
                answer += biggestNum;
                temp++;
            } else {
                answer += secondaryBigNum;
                temp = 0;
            }
        }

        return answer;
    }

    public static int solution2(int n, int m, int k, int[] arr) {
        int answer = 0;
        int biggestNum = 0;
        int secondaryBigNum = 0;

        //가장 큰 수와 2번째로 큰 수 찾기
        for (int i : arr) {
            if (biggestNum < i) {
                biggestNum = i;
            }
        }
        for (int i : arr) {
            if (secondaryBigNum < i && biggestNum != i) {
                secondaryBigNum = i;
            }
        }

        //정답 찾기
        int 반복횟수 = m / (k+1); // (k+1)개의 원소로 구성된 수열이 반복되는 횟수
        int 나머지 = m % (k+1); // 반복 후 남은 개수, 즉 가장큰 수가 더해져야하는 횟수
        int 수열의_합 = biggestNum * k + secondaryBigNum;
        answer = 수열의_합 * 반복횟수 + (biggestNum * 나머지);

        return answer;
    }

    public static int solution3(int n, int m, int k, int[] arr) {
        int answer = 0;
        int biggestNum = 0;
        int secondaryBigNum = 0;

        Arrays.sort(arr); //O(nlogn) 시간 소요 (Dual-Pivot Quick Sort)

        //가장 큰 수와 2번째로 큰 수 찾기
        biggestNum = arr[arr.length - 1];
        secondaryBigNum = arr[arr.length - 2];

        //정답 찾기
        int 반복횟수 = m / (k+1); // (k+1)개의 원소로 구성된 수열이 반복되는 횟수
        int 나머지 = m % (k+1); // 반복 후 남은 개수, 즉 가장큰 수가 더해져야하는 횟수
        int 수열의_합 = biggestNum * k + secondaryBigNum;
        answer = 수열의_합 * 반복횟수 + (biggestNum * 나머지);

        return answer;
    }
}
