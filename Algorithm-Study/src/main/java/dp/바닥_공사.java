package dp;

import time.TimeCheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class 바닥_공사 {

  static int[] d = new int[1000];

  private static int solution1(int n) {
    //d[n] = n*2의 영역을 채우는 경우의 수
    d[0] = 0;
    d[1] = 1;
    d[2] = 3;

    for (int i = 3; i <= n; i++) {
      d[i] = (d[i-1] + 2*d[i-2])%796796;
    }

    return d[n];
  }

  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    int n = Integer.parseInt(bf.readLine());

    TimeCheck.start();

    int answer = solution1(n);

    TimeCheck.end();

    System.out.println(answer);
  }
}
