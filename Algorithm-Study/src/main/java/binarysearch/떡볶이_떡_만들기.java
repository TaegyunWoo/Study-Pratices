package binarysearch;

import time.TimeCheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;

public class 떡볶이_떡_만들기 {

  private static void solution1(long[] n, long m) {
    long start = 0L; //시작점
    long end = Arrays.stream(n).max().getAsLong(); //끝점
    long middle = (end+start)/2; //중간점
    long height = middle; //절단기의 높이 (중간점이 절단기의 높이가 된다.)


    while (start <= end) {


      long resultLength = getResultLength(n, height); //잘린 떡의 총 길이 구하기

      if (resultLength >= m) {
        start = middle + 1;
      } else if (resultLength < m) {
        end = middle - 1;
      }

      middle = (end+start)/2;
      height = middle;
    }

    System.out.println(height);

  }

  /**
   * 절단기 높이에 따라, 잘린 떡의 총 길이를 구하는 메서드
   */
  private static long getResultLength(long[] n, long height) {
    long resultLength = 0L; //절단기의 높이에 따라, 구해질 떡의 총 길이 (결과)

    for (int i = 0; i < n.length; i++) {
      if (n[i]-height > 0)
        resultLength += n[i]-height;
    }

    return resultLength;
  }

  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    String s = bf.readLine();
    StringTokenizer st = new StringTokenizer(s);
    int sizeOfN = Integer.parseInt(st.nextToken());
    long m = Long.parseLong(st.nextToken());

    long[] n = new long[sizeOfN];

    s = bf.readLine();
    st = new StringTokenizer(s);

    for (int i = 0; i < sizeOfN; i++) {
      n[i] = Long.parseLong(st.nextToken());
    }

    TimeCheck.start();

    solution1(n, m);

    TimeCheck.end();
  }
}
