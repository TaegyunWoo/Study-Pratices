package dp;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class 만들기_1로 {

  static int[] d = new int[30001];

  /**
   * Bottom Up 방식
   */
  private static int bottomUp(int x) {

    for (int i = 2; i <= x; i++) {

      d[i] = d[i-1] + 1;

      if (i % 2 == 0) {
        d[i] = Math.min(d[i], d[i / 2] + 1);
      }
      if (i % 3 == 0) {
        d[i] = Math.min(d[i], d[i / 3] + 1);
      }
      if (i % 5 == 0) {
        d[i] = Math.min(d[i], d[i / 5] + 1);
      }

    }

    return d[x];
  }

  /**
   * Top Down 방식
   */
  private static int topDown(int x) {
    if (x == 1) {
      return 0;
    }

    if (d[x] != 0) {
      return d[x];
    }

    d[x] = topDown(x-1) + 1;

    if (x % 5 == 0) {
      int tmp = topDown(x/5) + 1;
      if (d[x] > tmp) {
        d[x] = tmp;
      }
    }

    if (x % 3 == 0) {
      int tmp = topDown(x/3) + 1;
      if (d[x] > tmp) {
        d[x] = tmp;
      }
    }

    if (x % 2 == 0) {
      int tmp = topDown(x/2) + 1;
      if (d[x] > tmp) {
        d[x] = tmp;
      }
    }

    return d[x];
  }

  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    int x = Integer.parseInt(bf.readLine());

//    int answer = bottomUp(n);
    int answer = topDown(x);

    System.out.println(answer);
  }
}
