package sort;

import time.TimeCheck;

import java.util.Arrays;
import java.util.Scanner;

public class 퀵정렬 {


  private static void solution(int[] array, int start, int end) {
    // start: 배열의 시작점
    // end: 배열의 끝점


    //만약 배열이 1개 이하라면 재귀 중단
    if (start >= end) {
      return;
    }

    int pivot = start; //pivot 설정
    int leftPointer = start+1; //pivot 보다 큰 숫자를 찾는 포인터
    int rightPointer = end; //pivot 보다 작은 숫자를 찾는 포인터

    while (true) {

      //pivot보다 큰 수 찾기
      while (leftPointer <= end && array[leftPointer] <= array[pivot]) {
        leftPointer++;
      }

      //pivot보다 작은 수 찾기
      while (rightPointer > start && array[rightPointer] >= array[pivot]) { //시작점은 피봇이므로 start 미포함
        rightPointer--;
      }

      if (leftPointer > rightPointer) { //교차됐다면
        int tmp = array[pivot];
        array[pivot] = array[rightPointer];
        array[rightPointer] = tmp;

        break; //교차시 탈출

      } else { //교차 안됐다면
        int tmp = array[rightPointer];
        array[rightPointer] = array[leftPointer];
        array[leftPointer] = tmp;
      }
    }

    solution(array, start, rightPointer-1);
    solution(array, rightPointer+1, end);

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

    solution(n, 0, sizeOfN-1);
    for (int i : n) {
      System.out.print(i + " ");
    }

    TimeCheck.end();
  }
}
