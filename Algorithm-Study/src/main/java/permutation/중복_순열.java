package permutation;

public class 중복_순열 {
  static int[] numbers = new int[] {1, 2, 3};
  static int r = 3;
  static int[] result = new int[3];

  //3개의 원소 중, 순서를 고려하여 중복해서 3개를 뽑기
  public static void permutation(int depth, int r) { //n은 필요없다.
    if (r == 0) { //3개를 모두 뽑았다면
      printResult();
      return;
    }

    for (int i = 0; i < numbers.length; i++) { //각 원소를 뽑는다.
      result[depth] = numbers[i]; //i번째 원소를 뽑는다.

      //중복 가능하므로, visited에 기록할 필요가 없다.

      permutation(depth + 1, r - 1);
    }
  }

  private static void printResult() {
    for (int number : result) {
      System.out.print(number + " ");
    }
    System.out.println();
  }
}
