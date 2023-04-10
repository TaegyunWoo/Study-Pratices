package shortestpath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class 미래도시 {
  private static final int INFINITE = (int) 1e9;
  private static int sizeOfNode;
  private static int sizeOfEdge;
  private static int x;
  private static int k;
  private static ArrayList<ArrayList<Node>> graph = new ArrayList<>();
  private static int[] shortestTable;
  private static int[] answer = new int[2];

  /**
   * 실행 메서드
   */
  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    String s = bf.readLine();
    StringTokenizer st = new StringTokenizer(s);
    sizeOfNode = Integer.parseInt(st.nextToken());
    sizeOfEdge = Integer.parseInt(st.nextToken());

    shortestTable = new int[sizeOfNode + 1];

    for (int i = 0; i <= sizeOfNode; i++) {
      graph.add(new ArrayList<>());
    }

    for (int i = 0; i < sizeOfEdge; i++) {
      s = bf.readLine();
      st = new StringTokenizer(s);
      int nodeA = Integer.parseInt(st.nextToken());
      int nodeB = Integer.parseInt(st.nextToken());
      int distance = 1;
      graph.get(nodeA).add(new Node(nodeB, distance)); //양방향이므로
      graph.get(nodeB).add(new Node(nodeA, distance)); //양방향이므로
    }

    s = bf.readLine();
    st = new StringTokenizer(s);
    x = Integer.parseInt(st.nextToken());
    k = Integer.parseInt(st.nextToken());

    //다익스트라 사용시 (총 2번 호출된다.)
//    int distanceOfK = solution1(1, k);
//    int distanceOfX = solution1(k, x);

    //플로이드 워셜 사용시 (1번만 호출해도 된다.)
    int[][] floyedGraph = solution2();
    int distanceOfK = floyedGraph[1][k];
    int distanceOfX = floyedGraph[k][x];

    if (distanceOfK >= INFINITE || distanceOfX >= INFINITE) {
      System.out.println(-1);
    } else {
      System.out.println(distanceOfX + distanceOfK);
    }

  }

  /**
   * 다익스트라 수행 메서드
   */
  private static int solution1(int startNode, int targetNode) {

    PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

    Arrays.fill(shortestTable, INFINITE);
    shortestTable[startNode] = 0;
    priorityQueue.offer(new Node(startNode, 0));


    while (!priorityQueue.isEmpty()) {
      Node currentNode = priorityQueue.poll();
      int currentNodeIndex = currentNode.getNodeIndex();
      int currentNodeDistance = currentNode.getDistance();

      if (currentNodeDistance > shortestTable[currentNodeIndex]) continue;

      for (int i = 0; i < graph.get(currentNodeIndex).size(); i++) {
        int indexOfNearNode = graph.get(currentNodeIndex).get(i).getNodeIndex();
        int distanceOfCurrentToNear = graph.get(currentNodeIndex).get(i).getDistance();
        int distanceOfStartToNear = currentNodeDistance + distanceOfCurrentToNear;

        if (shortestTable[indexOfNearNode] > distanceOfStartToNear) {
          shortestTable[indexOfNearNode] = distanceOfStartToNear;
          priorityQueue.offer(new Node(indexOfNearNode, distanceOfStartToNear));
        }
      }
    }

    return shortestTable[targetNode];
  }

  /**
   * 플로이드 워셜 수행 메서드
   */
  private static int[][] solution2() {
    int[][] floyedGraph = new int[sizeOfNode+1][sizeOfNode+1];

    // ------------ 플로이드 워셜에 사용될 그래프 초기화 ---------
    for (int i = 0; i < floyedGraph.length; i++) {
      Arrays.fill(floyedGraph[i], INFINITE);
    }

    for (int i = 1; i < floyedGraph.length; i++) {
      for (int u = 1; u < floyedGraph[i].length; u++) {
        if (i == u) floyedGraph[i][u] = 0;
      }
    }

    for (int i = 0; i < graph.size(); i++) {
      for (int u = 0; u < graph.get(i).size(); u++) {
        floyedGraph[i][graph.get(i).get(u).getNodeIndex()] = graph.get(i).get(u).getDistance();
      }
    }
    // ------------ 플로이드 워셜에 사용될 그래프 초기화 (끝) ---------

    for (int k = 1; k <= sizeOfNode; k++) {
      for (int a = 1; a <= sizeOfNode; a++) {
        for (int b = 1; b <= sizeOfNode; b++) {
          floyedGraph[a][b] = Math.min(floyedGraph[a][b], floyedGraph[a][k] + floyedGraph[k][b]);
        }
      }
    }

    return floyedGraph;

  }

  private static class Node implements Comparable<Node> {
    private int nodeIndex;
    private int distance;

    public Node(int nodeIndex, int distance) {
      this.nodeIndex = nodeIndex;
      this.distance = distance;
    }

    public int getNodeIndex() {
      return nodeIndex;
    }

    public int getDistance() {
      return distance;
    }

    @Override
    public int compareTo(Node other) {
      if (this.distance < other.distance) {
        return -1;
      }
      return 1;
    }

  }
}
