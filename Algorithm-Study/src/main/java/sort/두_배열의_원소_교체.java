package sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class 두_배열의_원소_교체 {

  private static long solution1(Integer[] a, Integer[] b, int n, int k) {
    long answer = 0;

    Arrays.sort(a);
    Arrays.sort(b);

    for (int i = 0; i < k; i++) {

      if (a[i] >= b[n-1]) continue;

      int tmp = a[i];
      a[i] = b[--n];
      b[n] = tmp;
    }

    for (int i = 0; i < a.length; i++) {
      answer += a[i];
    }

    return answer;
  }

  private static long solution2(Integer[] a, Integer[] b, int n, int k) {
    int answer = 0;

    Arrays.sort(a);
    Arrays.sort(b, Collections.reverseOrder());

    for (int i = 0; i < k; i++) {
      if (a[i] < b[i]) {
        int tmp = a[i];
        a[i] = b[i];
        b[i] = tmp;
      }
    }

    for (int i = 0; i < a.length; i++) {
      answer += a[i];
    }

    return answer;
  }

  public static void execute() {
    Scanner scn = new Scanner(System.in);
    int n = scn.nextInt();
    int k = scn.nextInt();
    Integer[] a = new Integer[n];
    Integer[] b = new Integer[n];
    scn.nextLine();

    for (int i = 0; i < n; i++) {
      a[i] = scn.nextInt();
    }
    scn.nextLine();
    for (int i = 0; i < n; i++) {
      b[i] = scn.nextInt();
    }

    long answer = solution2(a, b, n, k);

    System.out.println(answer);

  }
}
