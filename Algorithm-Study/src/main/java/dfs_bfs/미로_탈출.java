package dfs_bfs;

import time.TimeCheck;

import java.util.*;

public class 미로_탈출 {
  private static int[][] graph;
  private static int n;
  private static int m;
  private static Deque<Position> deque = new ArrayDeque<>();

  private static int solution1() {
    deque.addLast(new Position(0, 0)); //항상 가장 왼쪽 위 지점부터 시작하므로

    while (graph[n-1][m-1] == 1) {
      Position dequeuedPosition = deque.pollFirst();
      Map<String, Position> aroundPosition = new HashMap<>();

      //dequeue한 지점을 기준으로 상, 하, 좌, 우 위치 구하기
      aroundPosition.put("up", new Position(dequeuedPosition.row-1, dequeuedPosition.column));
      aroundPosition.put("down", new Position(dequeuedPosition.row+1, dequeuedPosition.column));
      aroundPosition.put("left", new Position(dequeuedPosition.row, dequeuedPosition.column-1));
      aroundPosition.put("right", new Position(dequeuedPosition.row, dequeuedPosition.column+1));

      for (Position p : aroundPosition.values()) {
        //불가능한 위치라면 무시
        if (p.row < 0 || p.row >= n || p.column < 0 || p.column >= m) {
          continue;
        }

        //몬스터가 있다면 무시
        if (graph[p.row][p.column] == 0) {
          continue;
        }

        // 인접 지점에 이전 지점의 값 더하기
        graph[p.row][p.column] += graph[dequeuedPosition.row][dequeuedPosition.column];
        deque.addLast(p); // 인접 지점 enqueue
      }

    }

    return graph[n - 1][m - 1];
  }

  public static int execute() {
    //-------입력--------
    Scanner scn = new Scanner(System.in);
    n = scn.nextInt();
    m = scn.nextInt();
    scn.nextLine();

    graph = new int[n][m];

    for (int i = 0; i < n; i++) {
      String[] s = scn.nextLine().split("");
      for (int u = 0; u < m; u++) {
        graph[i][u] = Integer.parseInt(s[u]);
      }
    }
    //------입력 종료----------

    TimeCheck.start();

    int answer = solution1();

    TimeCheck.end();

    return answer;
  }

  static class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
      this.row = row;
      this.column = column;
    }

  }
}
