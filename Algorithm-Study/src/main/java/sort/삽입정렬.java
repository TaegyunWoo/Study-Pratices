package sort;

import time.TimeCheck;

import java.util.Arrays;
import java.util.Scanner;

public class 삽입정렬 {

  private static int[] solution(int[] n) {

    for (int i = 1; i < n.length; i++) { //가장 첫번째 수는 이미 정렬되었다고 취급하므로, 1부터 시작

      int indexOfNowNum = i;

      for (int u = indexOfNowNum - 1; u >= 0; u--) {

        //현재 숫자보다 크다면 멈춘다.
        if (n[u] < n[indexOfNowNum]) {
          break;
        }

        //삽입
        int temp = n[indexOfNowNum];
        n[indexOfNowNum] = n[u];
        n[u] = temp;

        indexOfNowNum--;
      }

    }

    return n;
  }

  public static void execute() {
    Scanner scn = new Scanner(System.in);
    int sizeOfN = scn.nextInt();
    int[] n = new int[sizeOfN];
    scn.nextLine();

    for (int i = 0; i < sizeOfN; i++) {
      n[i] = scn.nextInt();
    }

    TimeCheck.start();

    int[] answer = solution(n);
    for (int i : answer) {
      System.out.println(i + " ");
    }

    TimeCheck.end();
  }
}
