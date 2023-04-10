package shortestpath;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class 전보 {
  private static final int INFINITE = (int) 1e9; //가능한 최대값이 3억까지이므로
  private static ArrayList<ArrayList<Node>> graph = new ArrayList<>();
  private static int[] shortestTable = new int[30001];
  private static int sizeOfNode;
  private static int sizeOfEdge;
  private static int startNode;

  /**
   * 실행 메서드
   */
  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    String s = bf.readLine();
    StringTokenizer st = new StringTokenizer(s);
    sizeOfNode = Integer.parseInt(st.nextToken());
    sizeOfEdge = Integer.parseInt(st.nextToken());
    startNode = Integer.parseInt(st.nextToken());

    for (int i = 0; i <= sizeOfNode; i++) {
      graph.add(new ArrayList<>());
    }

    for (int i = 0; i < sizeOfEdge; i++) {
      s = bf.readLine();
      st = new StringTokenizer(s);
      int fromNode = Integer.parseInt(st.nextToken());
      int toNode = Integer.parseInt(st.nextToken());
      int distance = Integer.parseInt(st.nextToken());
      graph.get(fromNode).add(new Node(toNode, distance));
    }

    int[] answer = solution1();

    for (int a : answer) {
      System.out.print(a + " ");
    }

  }

  /**
   * 다익스트라 알고리즘 수행 메서드
   */
  private static int[] solution1() {
    PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

    Arrays.fill(shortestTable, INFINITE);
    shortestTable[startNode] = 0;
    priorityQueue.offer(new Node(startNode, 0));

    while (!priorityQueue.isEmpty()) {

      Node currentNode = priorityQueue.poll();
      int currentNodeIndex = currentNode.getNodeIndex();
      int currentDistance = currentNode.getDistance();

      if (shortestTable[currentNodeIndex] < currentDistance) {
        continue;
      }

      for (int i = 0; i < graph.get(currentNodeIndex).size(); i++) {
        int indexOfNearNode = graph.get(currentNodeIndex).get(i).getNodeIndex();
        int distanceOfCurrentToNear = graph.get(currentNodeIndex).get(i).getDistance();
        int distanceOfStartToNear = distanceOfCurrentToNear + shortestTable[currentNodeIndex];
        if (shortestTable[indexOfNearNode] > distanceOfStartToNear) {
          shortestTable[indexOfNearNode] = distanceOfStartToNear;
          priorityQueue.offer(new Node(indexOfNearNode, distanceOfStartToNear));
        }
      }

    }

    return getAnswer();
  }

  private static int[] getAnswer() {
    int cityCount = 0;
    int timeCount = 0;

    for (int d : shortestTable) {
      if (d != INFINITE) {
        cityCount++;
        timeCount = Math.max(timeCount, d);
      }
    }

    return new int[] {cityCount-1, timeCount}; //시작노드는 정답에서 제외되므로 '-1'
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
