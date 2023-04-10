package sort;

import time.TimeCheck;

import java.util.Arrays;
import java.util.Scanner;

public class 계수정렬 {

  private static int[] solution(int[] n) {
    //배열 n의 각 원소는 정수이고, 0보다 크거나 같다고 가정한다.
    int[] sortAry = new int[Arrays.stream(n).max().getAsInt() + 1];

    for (int number : n) {
      sortAry[number]++;
    }

    return sortAry;
  }

  public static void execute() {
    Scanner scn = new Scanner(System.in);
    int size = scn.nextInt();
    int[] n = new int[size];

    scn.nextLine();

    for (int i = 0; i < size; i++) {
      n[i] = scn.nextInt();
    }

    TimeCheck.start();

    int[] answer = solution(n);

    TimeCheck.end();

    for (int i = 0; i < answer.length; i++) {
      for (int u = 0; u < answer[i]; u++) {
        System.out.print(i + " ");
      }
    }
  }
}
