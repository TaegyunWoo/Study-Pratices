package union_find;

public class UnionFind {
  int[] array = new int[] {0, 1, 2, 3, 4, 5, 6, 7};

  /**
   * Find 메서드
   * @param nodeNumber 찾을 노드번호
   * @return 찾은 루트노드 번호
   */
  public int find(int nodeNumber) {
    if (nodeNumber == array[nodeNumber]) { //노드 번호와 값이 같다면
      return nodeNumber; //해당 노드가 루트노드인 트리에 속해있다.
    }

    array[nodeNumber] = find(array[nodeNumber]); //경로 압축을 위해, 상단 루트노드로 저장한다.
    return array[nodeNumber];
  }

  /**
   * Union 메서드 (Union은 예약어인 경우가 많아, 보통 merge로 명명한다.)
   * @param x 합칠 노드 1
   * @param y 합칠 노드 2
   */
  public void merge(int x, int y) {
    int rootNodeOfX = find(x); //x 노드의 루트노드 번호
    int rootNodeOfY = find(y); //y 노드의 루트노드 번호

    //만약 두 노드의 루트노드가 같다면, 이미 연결되어있는 것이므로 종료
    if (x == y) {
      return;
    }

    //작은 번호가 루트 노드가 되도록
    if (rootNodeOfX < rootNodeOfY) {
      //루트노드 번호로 갱신
      array[rootNodeOfY] = rootNodeOfX;
    } else {
      //루트노드 번호로 갱신
      array[rootNodeOfX] = rootNodeOfY;
    }
  }

  /**
   * 두 노드가 연결되어있는지 판별하는 연산
   * @param x
   * @param y
   * @return
   */
  public boolean isUnion(int x, int y) {
    int rootNodeOfX = find(x);
    int rootNodeOfY = find(y);

    if (rootNodeOfX == rootNodeOfY) {
      return true;
    }

    return false;
  }
}
