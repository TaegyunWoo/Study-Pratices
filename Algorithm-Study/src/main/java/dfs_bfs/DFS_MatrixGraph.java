package dfs_bfs;

public class DFS_MatrixGraph {
  private int numberOfNode;
  private boolean[][] graph; //0행과 0열은 건너뛴다. (인접하다면 true)
  private boolean[] visitedNode;

  private DFS_MatrixGraph() {}

  public DFS_MatrixGraph(int numberOfNode, boolean[][] graph) {
    this.numberOfNode = numberOfNode;
    this.graph = graph; //0행은 비어있어야 한다.
    visitedNode = new boolean[numberOfNode+1];
  }

  public void solution(int indexOfNowNode) {
    //노드 방문
    visitedNode[indexOfNowNode] = true;

    //방문한 노드 출력
    System.out.print(indexOfNowNode + " ");

    for (int i = 1; i <= numberOfNode; i++) {
      //현재 노드(indexOfNow)와 인접한(연결된) 노드라면
      if (graph[indexOfNowNode][i]) {
        //방문하지 않은 노드라면
        if (!visitedNode[i]) {
          solution(i);
        }
      }
    }

  }

  /**
   * DFS 실행 메서드
   */
  public static void execute() {
    int numberOfNode = 8;

    //graph (true면 인접)
    boolean[][] graph = new boolean[][] {
      {false, false, false, false, false, false, false, false, false},
      {false, false, true, true, false, false, false, false, false},
      {false, true, false, false, false, false, false, true, true},
      {false, true, false, false, true, true, false, false, false},
      {false, false, false, true, false, true, false, false, false},
      {false, false, false, true, true, false, false, false, false},
      {false, false, false, false, false, false, false, true, false},
      {false, false, true, false, false, false, true, false, true},
      {false, false, true, false, false, false, false, true, false}
    };

    DFS_MatrixGraph dfs = new DFS_MatrixGraph(numberOfNode, graph);
    dfs.solution(1);
  }
}
