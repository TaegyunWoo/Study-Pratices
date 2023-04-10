package shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class 벨만포드 {

  public static final int INF = (int) 1e9;
  public List<Edge> edgeList; //간선 리스트
  int n; //노드 개수
  int m; //간선 개수
  int start; //출발지
  int destination; //도착지

  public 벨만포드(List<Edge> edgeList, int n, int start, int destination) {
    this.edgeList = edgeList;
    this.n = n;
    this.m = edgeList.size();
    this.start = start;
    this.destination = destination;
  }

  public void bellmanFord() {
    int[] table = new int[n]; //최단거리 테이블
    Arrays.fill(table, INF); //최단거리 테이블 초기화

    table[start] = 0; //시작노드 -> 시작노드는 거리가 0이므로

    //노드 개수만큼 반복
    for (int i = 0; i < n; i++) {

      //매 반복마다 모든 간선 확인
      for (int u = 0; u < m; u++) {
        int current = edgeList.get(u).getFrom();
        int next = edgeList.get(u).getTo();
        int distance = edgeList.get(u).getDistance();

        //만약 간선(u)의 출발노드(current)가 아직 한번도 갱신되지 않았다면, 생략
        if (table[current] == INF) continue;

        //만약 "최단거리 테이블에 저장된 값"이 "start노드 ~ next노드" 값보다 작다면
        if (table[next] > table[current] + distance) {

          //n번째 반복에서도 갱신된다면, 음수간선 사이클이 존재하는 것이다.
          if (i == n-1) {
            System.out.println("음수간선 사이클이 존재합니다!");
            return;
          }

          table[next] = table[current] + distance; //갱신한다.
        }
      }

    }

    //테이블 출력
    for (int i = 0; i < n; i++) {
      System.out.print(table[i] + " ");
    }
    System.out.println();

    //최단거리 출력
    System.out.println("최단거리: " + table[destination]);
  }

  /**
   * 실행 메서드
   */
  public static void execute() {
    System.out.println("<<음수간선 사이클이 없는 경우>>");
    List<Edge> edgeListA = new ArrayList<>();
    edgeListA.add(new Edge(0, 1, -4));
    edgeListA.add(new Edge(0, 2, 5));
    edgeListA.add(new Edge(0, 3, 2));
    edgeListA.add(new Edge(1, 3, -1));
    edgeListA.add(new Edge(2, 3, -7));
    edgeListA.add(new Edge(3, 4, 6));
    edgeListA.add(new Edge(4, 3, -4));

    벨만포드 b1 = new 벨만포드(edgeListA, 5, 0, 4);
    b1.bellmanFord();

    System.out.println("<<음수간선 사이클이 있는 경우>>");
    List<Edge> edgeListB = new ArrayList<>();
    edgeListB.add(new Edge(0, 1, -4));
    edgeListB.add(new Edge(0, 2, 5));
    edgeListB.add(new Edge(0, 3, 2));
    edgeListB.add(new Edge(1, 3, -1));
    edgeListB.add(new Edge(2, 3, -7));
    edgeListB.add(new Edge(3, 4, 3));
    edgeListB.add(new Edge(4, 2, 2));

    벨만포드 b2 = new 벨만포드(edgeListB, 5, 0, 4);
    b2.bellmanFord();
  }

  public static class Edge {
    private int from;
    private int to;
    private int distance;

    public Edge() {}

    public Edge(int from, int to, int distance) {
      this.from = from;
      this.to = to;
      this.distance = distance;
    }

    public int getFrom() {
      return from;
    }

    public int getTo() {
      return to;
    }

    public int getDistance() {
      return distance;
    }
  }

}
