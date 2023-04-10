package dfs_bfs;

import java.util.ArrayDeque;
import java.util.Deque;

public class BFS_MatrixGraph {
  boolean[][] graph;
  boolean[] visitedNode;
  int numbersOfNodes;
  Deque<Integer> deque = new ArrayDeque<>();

  private BFS_MatrixGraph() {}

  public BFS_MatrixGraph(boolean[][] graph, int numbersOfNodes) {
    this.graph = graph;
    this.numbersOfNodes = numbersOfNodes;
    this.visitedNode = new boolean[numbersOfNodes+1]; //노드는 1번부터 시작하므로
  }

  public void solution(int indexOfStartNode) {
    deque.addLast(indexOfStartNode); //시작 루트 노드 enqueue
    visitedNode[indexOfStartNode] = true; //시작 루트 노드 방문 표시 (index == 노드번호)
    System.out.print(indexOfStartNode + " "); //방문노드 출력

    while (!deque.isEmpty()) {
      int indexOfDequeuedNode = deque.pollFirst(); //dequeue

      //dequeue 한 노드의 인접 노드 구하기
      boolean[] indexOfNearNodes = graph[indexOfDequeuedNode];

      for (int i = 1; i < indexOfNearNodes.length; i++) {
        if (indexOfNearNodes[i]) { //만약 i번 노드가 dequeue된 노드와 인접한다면
          if (!visitedNode[i]) { //만약 i번 노드가 미방문 상태라면
            deque.addLast(i); //인접노드 enqueue
            visitedNode[i] = true; //방문 표시

            System.out.print(i + " "); //방문 노드 출력
          }
        }
      } //for문 종료
    } //while문 종료

  }

  /**
   * 실행 메서드
   */
  public static void execute() {
    int numberOfNode = 8; //총 노드 개수

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

    BFS_MatrixGraph bfs = new BFS_MatrixGraph(graph, numberOfNode);
    bfs.solution(1);
  }
}
