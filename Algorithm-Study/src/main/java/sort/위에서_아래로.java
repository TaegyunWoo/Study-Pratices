package sort;

import time.TimeCheck;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class 위에서_아래로 {

  private static Integer[] solution1(Integer[] n) {
    Arrays.sort(n, Collections.reverseOrder());
    return n;
  }

  public static void execute() {
    Scanner scn = new Scanner(System.in);
    int size = scn.nextInt();
    Integer[] n = new Integer[size];
    scn.nextLine();

    for (int i = 0; i < size; i++) {
      n[i] = scn.nextInt();
      scn.nextLine();
    }

    TimeCheck.start();

    Integer[] answer = solution1(n);

    TimeCheck.end();

    for (int i : answer) {
      System.out.print(i + " ");
    }
  }
}
