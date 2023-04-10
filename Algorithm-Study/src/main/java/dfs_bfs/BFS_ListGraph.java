package dfs_bfs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Queue;

public class BFS_ListGraph {
  private ArrayList<ArrayList<Integer>> graph; //리스트형 그래프
  private boolean[] visitedNode; //방문 노드 표시 (index == 노드번호)
  private Deque<Integer> deque = new ArrayDeque<>(); //BFS에서 사용할 큐
  private int numbersOfNode; //노드 총 개수

  private BFS_ListGraph() {}

  public BFS_ListGraph(ArrayList<ArrayList<Integer>> graph, int numbersOfNode) {
    this.graph = graph;
    this.numbersOfNode = numbersOfNode;
    visitedNode = new boolean[numbersOfNode+1]; //0번 노드는 없으므로
  }

  public void solution(int indexOfStartNode) {
    //시작 루트 노드 enqueue
    deque.addLast(indexOfStartNode);
    visitedNode[indexOfStartNode] = true;
    System.out.print(indexOfStartNode + " "); //방문 노드 출력

    while (!deque.isEmpty()) {

      int indexOfDequeuedNode = deque.pollFirst(); //dequeue

      ArrayList<Integer> indexOfNearNodes = graph.get(indexOfDequeuedNode); //dequeue 한 노드의 인접 노드들 구하기

      //인접 노드들을 enqueue
      for (Integer indexOfNearNode : indexOfNearNodes) {
        //만약 인접 노드가 방문하지 않은 노드라면 enqueue
        if (!visitedNode[indexOfNearNode]) {
          deque.addLast(indexOfNearNode); //enqueue

          visitedNode[indexOfNearNode] = true; //enqueue 한 노드 방문처리
          System.out.print(indexOfNearNode + " "); //방문 노드 출력
        }
      }

    } //while문 종료

  }

  /**
   * 실행 메서드
   */
  public static void execute() {
    int numbersOfNode = 8; //총 노드 개수

    //그래프 초기화 (노드는 1번부터 시작한다.)
    ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
    for (int i = 0; i <= numbersOfNode; i++) {
      graph.add(new ArrayList<Integer>());
    }

    //--- 그래프 작성 ---
    graph.get(1).add(2); //'1번 노드'와 인접한(연결된) 노드로 '2번 노드' 등록
    graph.get(1).add(3); //'1번 노드'와 인접한(연결된) 노드로 '3번 노드' 등록

    graph.get(2).add(1); //'2번 노드'와 인접한(연결된) 노드로 '1번 노드' 등록
    graph.get(2).add(7); //'2번 노드'와 인접한(연결된) 노드로 '7번 노드' 등록
    graph.get(2).add(8); //'2번 노드'와 인접한(연결된) 노드로 '8번 노드' 등록

    graph.get(3).add(1); //'3번 노드'와 인접한(연결된) 노드로 '1번 노드' 등록
    graph.get(3).add(4); //'3번 노드'와 인접한(연결된) 노드로 '4번 노드' 등록
    graph.get(3).add(5); //'3번 노드'와 인접한(연결된) 노드로 '5번 노드' 등록

    graph.get(4).add(3); //'4번 노드'와 인접한(연결된) 노드로 '3번 노드' 등록
    graph.get(4).add(5); //'4번 노드'와 인접한(연결된) 노드로 '5번 노드' 등록

    graph.get(5).add(3); //'5번 노드'와 인접한(연결된) 노드로 '3번 노드' 등록
    graph.get(5).add(4); //'5번 노드'와 인접한(연결된) 노드로 '4번 노드' 등록

    graph.get(6).add(7); //'6번 노드'와 인접한(연결된) 노드로 '7번 노드' 등록

    graph.get(7).add(2); //'7번 노드'와 인접한(연결된) 노드로 '2번 노드' 등록
    graph.get(7).add(6); //'7번 노드'와 인접한(연결된) 노드로 '6번 노드' 등록
    graph.get(7).add(8); //'7번 노드'와 인접한(연결된) 노드로 '8번 노드' 등록

    graph.get(8).add(1); //'8번 노드'와 인접한(연결된) 노드로 '1번 노드' 등록
    graph.get(8).add(7); //'8번 노드'와 인접한(연결된) 노드로 '7번 노드' 등록

    //----- 그래프 작성 끝 -----

    //BFS 알고리즘 시작
    BFS_ListGraph bfs = new BFS_ListGraph(graph, numbersOfNode);
    bfs.solution(1);
  }
}
