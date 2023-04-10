package datastructure;

import java.util.ArrayList;
import java.util.List;

public class Graph_List {
  private static final int INFINITY = Integer.MAX_VALUE;
  private static final int NODES_NUMBER = 3;


  ArrayList<ArrayList<Node>> graph = new ArrayList<>();

  /*
   * <인접행렬 방식으로 그래프 표현하기>
   * []: 노드
   * 숫자: 간선 weight
   *
   *         [0]
   *        /   \
   *       7     5
   *      /       \
   *    [1]       [2]
   */

  public void example() {
    for (int i = 0; i < 3; i++) {
      graph.add(new ArrayList<>());
    }

    graph.get(0).add(new Node(1, 7)); // [0]~[1]: weight 7
    graph.get(0).add(new Node(2, 5)); // [0]~[2]: weight 5

    graph.get(1).add(new Node(0, 7)); // [1]~[0]: weight 7

    graph.get(2).add(new Node(0, 5)); // [2]~[0]: weight 5

    /* <설명>
     * int[노드A][간선 weight] == 해당 배열의 Index(List에서의 위치) 노드와 노드A 사이의 간선 weight
     *
     * <예시>
     * ArrayList = [
     *       ArrayList = [Node(1, 7), Node(2, 5)],
     *       ArrayList = [Node(0, 7)],
     *       ArrayList = [Node(0, 5)]
     * ]
     */
  }

  static class Node {
    private int nodeNum;
    private int distance;

    public Node(int nodeNum, int distance) {
      this.nodeNum = nodeNum;
      this.distance = distance;
    }
  }
}
