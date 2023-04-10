package shortestpath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class 다익스트라_개선 {

  private static int sizeOfNode;
  private static int sizeOfEdge;
  private static int startNode;
  private static ArrayList<ArrayList<Node>> graph = new ArrayList<>();
  private static int[] shortestTable = new int[10001];

  /**
   * 실행 메서드
   */
  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    String s = bf.readLine();
    StringTokenizer st = new StringTokenizer(s);
    sizeOfNode = Integer.parseInt(st.nextToken());
    sizeOfEdge = Integer.parseInt(st.nextToken());
    startNode = Integer.parseInt(bf.readLine());
    for (int i = 0; i <= sizeOfNode; i++) {
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

    for (int i = 1; i <= sizeOfNode; i++) {

      //시작노드에서 도달할 수 없는 노드인 경우
      if (shortestTable[i] == Integer.MAX_VALUE) {
        System.out.println("INFINITE");
      } else {
        System.out.println(shortestTable[i]);
      }
    }
  }

  /**
   * 다익스트라 알고리즘 메서드
   */
  private static void solution() {
    //우선순위 큐 (최소힙으로 구현되어있다.)
    PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

    //최단거리 테이블 초기화
    Arrays.fill(shortestTable, Integer.MAX_VALUE);
    shortestTable[startNode] = 0;

    priorityQueue.offer(new Node(startNode, 0)); //우선순위 큐에 시작노드 데이터 집어넣기

    //우선순위 큐가 빌때까지 반복
    while (!priorityQueue.isEmpty()) {
      Node node = priorityQueue.poll(); //우선순위 큐에서 거리가 가장 짧은 노드 꺼내기
      int nowNodeIndex = node.getIndex(); //현재 노드 index
      int nowNodeDistance = node.getDistance(); //현재 노드 거리

      //최단거리 테이블 값보다 현재노드의 거리가 더 크다면, 해당 노드는 이미 처리가 된 것이다.
      /*
        "우선순위 큐에 저장된 데이터 == 과거에 최소거리 테이블을 갱신할 때 사용된 데이터" 이다.
        그러므로 '우선순위 큐에 저장된 데이터' 보다 현재의 '최소거리 테이블의 데이터' 가 작다면,
        해당 '우선순위 큐에 저장된 데이터' 는 필요없는 값이 된다.
       */
      if (shortestTable[nowNodeIndex] < nowNodeDistance) continue;

      //현재 노드와 연결된 다른 인접한 노드들을 확인
      for (int i = 0; i < graph.get(nowNodeIndex).size(); i++) {
        int nearNodeIndex = graph.get(nowNodeIndex).get(i).getIndex();
        int nearNodeDistance = graph.get(nowNodeIndex).get(i).getDistance();
        int distanceOfStartNodeToNearNode = shortestTable[nowNodeIndex] + nearNodeDistance;

        if (distanceOfStartNodeToNearNode < shortestTable[nearNodeIndex]) {
          shortestTable[nearNodeIndex] = distanceOfStartNodeToNearNode;
          priorityQueue.offer(new Node(nearNodeIndex, distanceOfStartNodeToNearNode));
        }
      } //내부 for문 종료

    } //외부 while문 종료

  }

  /**
   * Comparable 인터페이스를 상속받는다.
   * Comparable 인터페이스: 객체간 비교를 위한 인터페이스
   */
  private static class Node implements Comparable<Node> {
    private int index;
    private int distance;

    public Node(int index, int distance) {
      this.index = index;
      this.distance = distance;
    }

    public int getIndex() {
      return index;
    }

    public int getDistance() {
      return distance;
    }

    //우선순위 큐 내부에서 정렬될 때 사용된다.
    //거리가 짧은 것이 높은 우선순위를 가지도록 설정
    @Override
    public int compareTo(Node other) {
      if (this.distance < other.distance) {
        return -1; //음수 또는 0이면 객체의 자리가 유지된다. (우선순위 큐가 최소힙으로 구성되어있으므로)
      }
      return 1; //양수인 경우에는 두 객체의 자리가 바뀐다. (우선순위 큐가 최소힙으로 구성되어있으므로)
    }
  }
}
