package shortestpath;

import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class 다익스트라_기본 {
  static boolean[] isVisitedNode = new boolean[100001]; //방문한 노드인지 (isVisitedNode[i] = i번째 노드를 방문한적 있는지)
  static int[] shortestTable = new int[100001]; //최단거리 테이블 (shortedTable[i] = startNode에서 i노드까지의 최단 거리)
  static int sizeOfVertex; //노드의 개수
  static int sizeOfEdge; //간선의 개수
  static int startNode; //시작 노드 (index)
  static ArrayList<ArrayList<Node>> graph = new ArrayList<>(); //그래프

  /**
   * 실행 메서드
   */
  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    String s = bf.readLine();
    StringTokenizer st = new StringTokenizer(s);
    sizeOfVertex = Integer.parseInt(st.nextToken());
    sizeOfEdge = Integer.parseInt(st.nextToken());
    startNode = Integer.parseInt(bf.readLine());
    for (int i = 0; i <= sizeOfVertex; i++) {
      graph.add(new ArrayList<Node>());
    }

    for (int i = 0; i < sizeOfEdge; i++) {
      s = bf.readLine();
      st = new StringTokenizer(s);
      int fromNode = Integer.parseInt(st.nextToken());
      int toNode = Integer.parseInt(st.nextToken());
      int distance = Integer.parseInt(st.nextToken());
      graph.get(fromNode).add(new Node(toNode, distance));
    }

    solution();

    for (int i = 1; i <= sizeOfVertex; i++) {

      //시작노드에서 도달할 수 없는 노드인 경우
      if (shortestTable[i] == Integer.MAX_VALUE) {
        System.out.println("INFINITE");
      } else {
        System.out.println(shortestTable[i]);
      }
    }
  }

  /**
   * 다익스트라 알고리즘 수행 메서드
   */
  private static void solution() {
    Arrays.fill(shortestTable, Integer.MAX_VALUE);

    // 시작 노드에 대해 초기화
    shortestTable[startNode] = 0;
    isVisitedNode[startNode] = true;
    for (int i = 0; i < graph.get(startNode).size(); i++) { //시작 노드와 인접한 노드들에 대한 shortest 테이블 값 설정
      shortestTable[graph.get(startNode).get(i).getNodeNum()] = graph.get(startNode).get(i).getDistance();
    }

    // 시작 노드를 제외한 전체 n-1 개의 노드에 대해 반복한다.
    for (int i = 0; i < sizeOfVertex - 1; i++) {
      int currentNode = getShortestNode(); //시작노드로부터 가장 거리가 짧은 노드 구하기
      isVisitedNode[currentNode] = true; //방문 처리

      //현재 노드와 인접한 노드들 확인 (시작노드와 인접노드 간의 최소거리 구하기)
      for (int u = 0; u < graph.get(currentNode).size(); u++) {
        int distanceBetweenCurrentNodeAndNearNode = graph.get(currentNode).get(u).getDistance(); //'현재노드'와 '인접 노드' 간의 거리
        int distanceBetweenStartNodeAndNearNode = shortestTable[currentNode] + distanceBetweenCurrentNodeAndNearNode; //'시작노드'와 '인접 노드' 간의 거리

        if (shortestTable[graph.get(currentNode).get(u).getNodeNum()] > distanceBetweenStartNodeAndNearNode) {
          shortestTable[graph.get(currentNode).get(u).getNodeNum()] = distanceBetweenStartNodeAndNearNode;
        }
      } //내부 for문 종료

    } //외부 for문 종료

  }

  /**
   * 방문하지 않은 노드 중에서,
   * 가장 최단 거리가 짧은 노드의 번호를 반환하는 메서드
   */
  private static int getShortestNode() {
    int min_value = Integer.MAX_VALUE;
    int index = 0; // 가장 최단 거리가 짧은 노드 (인덱스)

    for (int i = 1; i <= sizeOfVertex; i++) {
      if (shortestTable[i] < min_value && !isVisitedNode[i]) {
        min_value = shortestTable[i];
        index = i;
      }
    }

    return index;
  }

  /**
   * 노드 클래스
   */
  private static class Node {
    private int nodeNum;
    private int distance;

    public Node(int nodeNum, int distance) {
      this.nodeNum = nodeNum;
      this.distance = distance;
    }

    public int getNodeNum() {
      return nodeNum;
    }

    public int getDistance() {
      return distance;
    }
  }
}
