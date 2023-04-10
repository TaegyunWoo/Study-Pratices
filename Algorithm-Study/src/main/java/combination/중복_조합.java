package combination;

public class 중복_조합 {
  static int[] numbers = new int[] {1, 2, 3};
  static int r = 3;
  static int[] result = new int[3];

  //3개의 원소 중, 순서를 고려하지 않고 중복해서 3개를 뽑기
  public static void combination(int depth, int r, int start) { //n은 필요없다.
    if (r == 0) { //3개를 모두 뽑았다면
      printResult();
      return;
    }

    for (int i = start; i < numbers.length; i++) {
      result[depth] = numbers[i];
      combination(depth + 1, r - 1, i);
    }
  }

  private static void printResult() {
    for (int number : result) {
      System.out.print(number + " ");
    }
    System.out.println();
  }
}
