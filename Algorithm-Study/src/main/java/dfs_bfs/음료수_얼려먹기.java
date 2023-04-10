package dfs_bfs;

import java.util.Scanner;

public class 음료수_얼려먹기 {
  private static int[][] graph;
  private static int n;
  private static int m;
  private static int answer;

  private 음료수_얼려먹기() {}

  public 음료수_얼려먹기(int[][] graph, int n, int m) {
    this.graph = graph;
    this.n = n;
    this.m = m;
  }

  public boolean solution1(int x, int y) {

    //불가능한 위치인 경우
    if (x < 0 || x >= n || y < 0 || y >= m) {
      return false;
    }

    // 구멍이 뚫려있는 부분이라면 (방문한적 없다면)
    if (graph[x][y] == 0) {

      //구멍이 있는 부분을 방문했을 때, 칸막이가 존재한다고 변경하여 방문처리를 할 수 있다.
      graph[x][y] = 1;

      //상하좌우로 연결된 노드 찾기
      solution1(x-1, y); //상
      solution1(x+1, y); //하
      solution1(x, y-1); //좌
      solution1(x, y+1); //우

      return true;
    }

    return false;
  }

  public static int execute() {

    //--------- 입력 로직 ------------
    Scanner sc = new Scanner(System.in);

    // N, M을 공백을 기준으로 구분하여 입력 받기
    n = sc.nextInt();
    m = sc.nextInt();
    sc.nextLine(); // 버퍼 지우기

    // 2차원 리스트의 맵 정보 입력 받기
    graph = new int[n][m];
    for (int i = 0; i < n; i++) {
      String str = sc.nextLine();
      for (int j = 0; j < m; j++) {
        graph[i][j] = str.charAt(j) - '0';
      }
    }

    //---------- 입력 로직 끝 -----------------

    음료수_얼려먹기 tmp = new 음료수_얼려먹기(graph, n, m);

    for (int i = 0; i < n; i++) {
      for (int u = 0; u < m; u++) {
        if (tmp.solution1(i, u)) {
          answer++;
        }
      }
    }

    return answer;
  }


}
