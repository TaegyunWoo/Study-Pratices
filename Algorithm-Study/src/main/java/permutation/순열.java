package permutation;

public class 순열 {

  static int[] numbers = {1, 2, 3, 4};
  static boolean[] visited = new boolean[4];
  static int[] result = new int[4];
  static int n = 4;
  static int r = 2;

  // 4P3
  public static void permutation(int depth) {
    if (depth == r) { //r개의 원소를 모두 골랐다면 탈출
      print();
    }

    for (int i = 0; i < n; i++) {
      if (visited[i]) continue; //i번째 원소가 이미 선택되어 있다면, 뽑는것을 생략한다.

      visited[i] = true; //i번째 원소를 뽑는다.
      result[depth] = numbers[i]; //뽑은 순서를 기억하기 위해, result 배열에 depth를 인덱스로 삼아 저장한다.
      permutation(depth+1); //재귀호출을 하여, 나머지를 뽑는다.

      //위 재귀호출을 통해, 나머지를 모두 뽑아 결과를 확인했으므로
      //i번째 원소를 뽑지 않고, 다음 원소를 고려하도록 false로 설정한다.
      visited[i] = false;
    }

  }

  private static void print() {
    for (int i = 0; i < r; i++) {
      System.out.print(result[i] + " ");
    }
    System.out.println();
  }

}
