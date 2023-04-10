package combination;

public class 조합_구하기 {

  public static void getCombination(int[] numAry, boolean[] visited, int depth, int n, int r) {
    if (r == 0) { //뽑아야하는 만큼 뽑았으므로, 출력 후 종료
      print(numAry, visited);
      return;
    }
    if (depth == n) { //모든 원소를 둘러보았으므로, 종료
      return;
    }

    visited[depth] = true; //현재 원소를 뽑았을 때
    getCombination(numAry, visited, depth+1, n, r-1); // n-1Cr-1

    visited[depth] = false; //현재 원소를 뽑지 않았을 때
    getCombination(numAry, visited, depth+1, n, r); // n-1Cr
  }

  private static void print(int[] numAry, boolean[] visited) {
    System.out.print("{");
    for (int i = 0; i < visited.length; i++) {
      if (visited[i]) {
        System.out.print(numAry[i] + " ");
      }
    }
    System.out.print("}\n");
  }
}
