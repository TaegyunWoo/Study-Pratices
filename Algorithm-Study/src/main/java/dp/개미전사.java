package dp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class 개미전사 {

  static int[] d = new int[100]; // d[i] = i번째 창고에 도착했을 때의 최대값 (i번째 창고를 털수있는지는 확정 X)

  /**
   * Bottom Up 방식
   */
  private static int bottomUp(int[] n) {

    d[0] = n[0];
    d[1] = Math.max(n[0], n[1]);

    for (int i = 2; i < n.length; i++) {
      d[i] = Math.max(d[i-1], d[i-2]+n[i]);
    }

    return d[n.length-1];
  }

  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    int sizeOfN = Integer.parseInt(bf.readLine());
    int[] n = new int[sizeOfN];
    String s = bf.readLine();
    StringTokenizer st = new StringTokenizer(s);

    for (int i = 0; i < sizeOfN; i++) {
      n[i] = Integer.parseInt(st.nextToken());
    }

    int answer = bottomUp(n);

    System.out.println(answer);
  }
}
