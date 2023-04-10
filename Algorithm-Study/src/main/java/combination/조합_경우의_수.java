package combination;

public class 조합_경우의_수 {

  public int getCombinationCaseNum(int n, int r) {

    /*
     * n == r 이라면, 모두 뽑는 경우 하나만 존재한다.
     * r == 0 이라면, 모두 뽑지 않는 경우 경우 하나만 존재한다.
     */
    if (n == r || r == 0) {
      return 1;
    }

    return getCombinationCaseNum(n-1, r-1) + getCombinationCaseNum(n-1, r);
  }

}
