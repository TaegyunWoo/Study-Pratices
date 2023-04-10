package binarysearch;

public class 이진탐색_반복문 {

  private static void solution(int[] n, int target) {
    int start = 0;
    int end = n.length - 1;
    int middle = (start+end)/2;

    while (true) {

      if (start == end && n[middle] != target) {
        System.out.println("찾으려는 값이 존재하지 않습니다.");
        return;
      }

      if (n[middle] > target) {
        end = middle - 1;
        middle = (start + end) / 2;
      } else if (n[middle] < target) {
        start = middle + 1;
        middle = (start+end) / 2;
      } else if (n[middle] == target) {
        break;
      }
    }

    System.out.println(middle);

  }

  public static void execute() {
    int[] n = {0, 2, 4, 6, 8, 10, 12, 14, 16, 18};
    solution(n, 5);
  }
}
