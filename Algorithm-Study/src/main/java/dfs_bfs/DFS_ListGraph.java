package dfs_bfs;

import java.util.ArrayList;

public class DFS_ListGraph {
  //리스트형 그래프
  private ArrayList<ArrayList<Integer>> graph;
  private boolean[] visitedNode; //배열의 index == 노드번호

  private DFS_ListGraph() {

  }

  public DFS_ListGraph(ArrayList<ArrayList<Integer>> graph, int numbersOfNode) {
    this.graph = graph;
    visitedNode = new boolean[numbersOfNode+1]; //0번 index를 갖는 노드는 생략할 것이기 때문에
  }

  public void solution(int indexOfNowNode) {
    //현재 방문한 노드(indexOfNowNode)를 방문 처리
    visitedNode[indexOfNowNode] = true;

    //현재 방문한 노드 출력
    System.out.print(indexOfNowNode + " ");

    //현재 방문한 노드(indexOfNowNode)의 인접노드들
    ArrayList<Integer> nearNodesOfNowNode = graph.get(indexOfNowNode);

    for (int i = 0; i < nearNodesOfNowNode.size(); i++) {
      int indexOfNearNode = nearNodesOfNowNode.get(i);
      if (!visitedNode[indexOfNearNode]) {
        solution(indexOfNearNode);
      }
    }

  }

  /**
   * DFS 실행 메서드
   */
  public static void execute() {
    int numbersOfNode = 8; //노드 총 개수
    ArrayList<ArrayList<Integer>> graph = new ArrayList<>();

    //그래프 초기화
    for (int i = 0; i <= numbersOfNode; i++) {
      graph.add(new ArrayList<>());
    }

    //------- 그래프 만들기 (노드는 1번부터 시작한다.) -----
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

    graph.get(8).add(2); //'8번 노드'와 인접한(연결된) 노드로 '2번 노드' 등록
    graph.get(8).add(7); //'8번 노드'와 인접한(연결된) 노드로 '7번 노드' 등록

    //----- 그래프 만들기 끝 -----

    //DFS 알고리즘 시작
    DFS_ListGraph dfs = new DFS_ListGraph(graph, numbersOfNode);
    dfs.solution(1); //루트 노드부터 시작
  }



}
