package greedy;

public class 거스름돈 {



    public static int solution1(int input) {
        int answer = 0;

        while (input != 0) {
            if (input >= 500) {
                input -= 500;
            } else if (input >= 100) {
                input -= 100;
            } else if (input >= 50) {
                input -= 50;
            } else if (input >= 10) {
                input -= 10;
            }
            answer++;
        }

        return answer;
    }

    public static int solution2(int input) {
        int answer = 0;
        int[] coins = {500, 100, 50, 10};

        for (int coin : coins) {
            answer += input / coin;
            input %= coin;
        }

        return answer;
    }
}
