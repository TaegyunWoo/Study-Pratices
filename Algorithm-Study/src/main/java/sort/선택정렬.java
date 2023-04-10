package sort;

import time.TimeCheck;

import java.util.Scanner;

public class 선택정렬 {

  private static int[] solution(int[] n) {
    int indexOfLastSortedNum = -1; //정렬된 숫자를 기억하기 위한 변수

    while (indexOfLastSortedNum < n.length-1) {


      //정렬되지 않은 수들 중 가장 작은 것 선택
      int indexOfSmallestNum = indexOfLastSortedNum+1; //초기화
      for (int i = indexOfLastSortedNum + 2; i < n.length; i++) {
        if (n[indexOfSmallestNum] > n[i]) {
          indexOfSmallestNum = i;
        }
      }

      //정렬되지 않은 수들 중, '선택된 것'과 '가장 앞에 있는 것'을 서로 교체한다.
      int temp = n[indexOfLastSortedNum+1];
      n[indexOfLastSortedNum+1] = n[indexOfSmallestNum];
      n[indexOfSmallestNum] = temp;

      //업데이트
      indexOfLastSortedNum++;
    }

    return n;
  }

  /**
   * 실행 메서드
   */
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
    TimeCheck.end();

    for (int i : answer) {
      System.out.print(i + " ");
    }
  }
}
