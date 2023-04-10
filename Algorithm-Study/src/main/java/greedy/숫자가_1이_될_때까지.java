package greedy;

public class 숫자가_1이_될_때까지 {

    public static int solution1(int n, int k) {
        int answer = 0;

        while (n != 1) {
            if (n % k == 0)
                n /= k;
            else
                n--;
            answer++;
        }

        return answer;
    }
}
