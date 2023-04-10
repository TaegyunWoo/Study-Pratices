package binarysearch;

import time.TimeCheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

public class 부품_찾기 {

  /**
   * 이진탐색 직접 구현
   */
  private static void solution1(int[] n, int[] m) {
    String answer = "";
    Arrays.sort(n); //이진탐색을 수행하기 위해, 먼저 정렬을 해야 한다.

    for (int i = 0; i < m.length; i++) {

      int start = 0;
      int end = n.length - 1;
      int middle = (start+end)/2;

      //이진탐색 (반복문 활용)
      while (true) {

        //값이 없는 경우
        if (start == end && m[i] != n[middle]) {
          answer += "no ";
          break;
        }

        if (m[i] < n[middle]) {
          end = middle - 1;
          middle = (start+end)/2;
        }
        else if (m[i] > n[middle]) {
          start = middle + 1;
          middle = (start+end)/2;
        }
        else if (m[i] == n[middle]) {
          answer += "yes ";
          break;
        }
      }

    }

    System.out.println(answer);
  }

  /**
   * 이진탐색 라이브러리 활용
   */
  private static void solution2(int[] n, int[] m) {
    String answer = "";
    Arrays.sort(n); //이진탐색을 수행하기 위해, 먼저 정렬을 해야 한다.

    for (int p : m) {
      int result = Arrays.binarySearch(n, p);
      if (result >= 0) { //p를 배열 n에서 찾았다면, 해당 index가 result에 담긴다.
        answer += "yes ";
      } else { //p를 배열 n에서 찾지 못했다면, 음수가 result에 담긴다.
        answer += "no ";
      }
    }

    System.out.println(answer);
  }

  /**
   * 계수 정렬 활용
   * 모든 값이 0 이상의 정수이고, 최대 크기가 100,000,000 이기때문에 사용할 수 있다.
   */
  private static void solution3(int[] n, int[] m) {
    String answer = "";
    int max = Arrays.stream(n).max().getAsInt();
    int[] answerAry = new int[max+1];

    for (int i = 0; i < n.length; i++) {
      answerAry[n[i]]++;
    }

    for (int i = 0; i < m.length; i++) {
      if (answerAry[m[i]] > 0) {
        answer += "yes ";
      } else {
        answer += "no ";
      }
    }

    System.out.println(answer);
  }

  public static void execute() throws Exception {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int sizeOfN = Integer.parseInt(br.readLine());
    int[] n = new int[sizeOfN];

    String s = br.readLine();
    StringTokenizer st = new StringTokenizer(s);
    for (int i = 0; i < sizeOfN; i++) {
      n[i] = Integer.parseInt(st.nextToken());
    }

    int sizeOfM = Integer.parseInt(br.readLine());
    int[] m = new int[sizeOfM];

    s = br.readLine();
    st = new StringTokenizer(s);
    for (int i = 0; i < sizeOfM; i++) {
      m[i] = Integer.parseInt(st.nextToken());
    }

    TimeCheck.start();
//    solution1(n, m);
//    solution2(n, m);
    solution3(n, m);
    TimeCheck.end();
  }
}
