package dp;

import time.TimeCheck;

public class 피보나치 {

  static int[] d = new int[100]; //메모이제이션을 위한 배열

  /**
   * Top Down 방식
   */
  private static int topDown(int x) {
    if (x == 1) {
      return 1;
    }
    if (x == 2) {
      return 1;
    }

    if (d[x] != 0) { //메모이제이션 활용
      return d[x];
    }

    d[x] = topDown(x-1) + topDown(x-2); //계산 결과 저장: 메모이제이션

    return d[x];
  }

  /**
   * Bottom Up 방식
   */
  private static int bottomUp(int x) {

    d[1] = 1;
    d[2] = 2;

    for (int i = 3; i <= x; i++) {
      d[i] = d[i-1] + d[i-2];
    }

    return d[x];
  }

  public static void execute() {
    int x = 40;

    TimeCheck.start();

//    int answer = topDown(x);
    int answer = bottomUp(x);

    TimeCheck.end();
    System.out.println(answer);
  }
}
