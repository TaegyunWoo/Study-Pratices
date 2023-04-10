package datastructure;

public class Graph_Matrix {
  private static final int INFINITY = Integer.MAX_VALUE;
  private static final int NODES_NUMBER = 3;


  int[][] graph;

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
    graph = new int[][] {
      {0, 7, 5},
      {7, 0, INFINITY},
      {5, INFINITY, 0}
    };

    /* <설명>
     * int[노드A][노드B] == 노드A와 노드B 사이의 간선 weight
     *
     * <예시>
     * {
     *   {'노드0과 노드0 사이의 간선 weight', '노드0과 노드1 사이의 간선 weight', '노드0과 노드2 사이의 간선 weight'},
     *   {'노드1과 노드0 사이의 간선 weight', '노드1과 노드1 사이의 간선 weight', '노드1과 노드2 사이의 간선 weight'},
     *   {'노드2와 노드0 사이의 간선 weight', '노드2와 노드1 사이의 간선 weight', '노드2와 노드2 사이의 간선 weight'},
     * }
     */
  }
}
