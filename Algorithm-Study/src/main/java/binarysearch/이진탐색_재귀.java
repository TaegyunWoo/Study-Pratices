package binarysearch;

public class 이진탐색_재귀 {

  private static void solution(int[] n, int start, int end, int middle, int target) {

    if (start == end && n[middle] != target) {
      System.out.println("찾으려는 값이 존재하지 않습니다.");
      return;
    }

    if (n[middle] < target) {
      solution(n, middle+1, end, (middle+1+end)/2, target);
    } else if (n[middle] > target) {
      solution(n, start, middle - 1, (start + middle - 1) / 2, target);
    } else {
      System.out.println(middle);
      return;
    }

  }

  public static void execute() {
    int[] n = {0, 2, 4, 6, 8, 10, 12, 14, 16, 18};
    solution(n, 0, 9, 9/2, 5);
  }
}
