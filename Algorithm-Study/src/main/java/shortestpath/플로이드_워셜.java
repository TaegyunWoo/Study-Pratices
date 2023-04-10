package shortestpath;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class 플로이드_워셜 {
  static final int INF = (int) 1e9; //무한을 의미하는 값으로 10억을 설정
  static int sizeOfNode;
  static int sizeOfEdge;
  static int[][] graph = new int[501][501]; //그래프


  /**
   * 실행 메서드
   */
  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    sizeOfNode = Integer.parseInt(bf.readLine());
    sizeOfEdge = Integer.parseInt(bf.readLine());

    //그래프를 무한으로 초기화
    for (int i = 0; i < graph.length; i++) {
      Arrays.fill(graph[i], INF);
    }

    //자기자신으로 가는 최소 거리는 0이므로 0으로 초기화
    for (int i = 1; i < graph.length; i++) {
      for (int u = 1; u < graph[i].length; u++) {
        if (i == u) graph[i][u] = 0;
      }
    }

    //입력받은 간선 정보로 그래프의 나머지 부분 초기화
    for (int i = 0; i < sizeOfEdge; i++) {
      String s = bf.readLine();
      StringTokenizer st = new StringTokenizer(s);
      int fromNode = Integer.parseInt(st.nextToken());
      int toNode = Integer.parseInt(st.nextToken());
      int distance = Integer.parseInt(st.nextToken());
      graph[fromNode][toNode] = distance;
    }

//    solution1();
    solution2();

    for (int i = 1; i <= sizeOfNode; i++) {
      for (int u = 1; u <= sizeOfNode; u++) {
        if (graph[i][u] >= INF) {
          System.out.print("INFINITE");
        } else {
          System.out.print(graph[i][u] + " ");
        }
      }
      System.out.println();
    }
  }

  private static void solution1() {

    for (int k = 1; k <= sizeOfNode; k++) {
      for (int a = 1; a <= sizeOfNode; a++) {
        if (a == k) continue; //k를 제외한 나머지에서 선택해야하므로

        for (int b = 1; b <= sizeOfNode; b++) {
          if (b == k) continue; //k를 제외한 나머지에서 선택해야하므로
          if (b == a) continue; //k를 제외한 노드 중, 같은 노드끼리인 경우는 생략 (거리=0)
          graph[a][b] = Math.min(graph[a][b], graph[a][k]+graph[k][b]);
        } //for문 종료

      } //for문 종료
    } //for문 종료
  }

  private static void solution2() {
    for (int k = 1; k <= sizeOfNode; k++) {
      for (int a = 1; a <= sizeOfNode; a++) {
        for (int b = 1; b <= sizeOfNode; b++) {
          graph[a][b] = Math.min(graph[a][b], graph[a][k]+graph[k][b]);
        }//for문 종료
      }//for문 종료
    }//for문 종료

  }

}
