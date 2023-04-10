package dp;

import time.TimeCheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class 효율적인_화폐_구성 {

  static int[] d = new int[10000];

  /** 점화식 (내 풀이)
    d[i] = min(d[i-n[0]], d[i-n[1]], ..., d[i-n[n.length-1]])
   */
  private static int solution1(int[] n, int m) {
    //d[i] = 금액 i를 표현할 수 있는 최소 화폐 수

    //배열 d 초기화
    Arrays.fill(d, -1);
    d[0] = 0;

    for (int nowCost = 0; nowCost <= m; nowCost++) {

      int tmp = Integer.MAX_VALUE;
      for (int moneyUnit : n) {

//        if (nowCost - moneyUnit >= 0 && d[nowCost-moneyUnit] != -1 && tmp > d[nowCost-moneyUnit]+1) {
//          tmp = d[nowCost-moneyUnit]+1;
//          d[nowCost] = tmp;
//        }
//        아래와 동일한 로직

        if (nowCost - moneyUnit >= 0) { //존재 가능한 index이고
          if (d[nowCost - moneyUnit] != - 1) { //화폐단위로 표현할 수 있었던 것이고
            if (tmp > d[nowCost - moneyUnit] + 1) { //그것이 최소값이라면
              tmp = d[nowCost-moneyUnit]+1;
              d[nowCost] = tmp; //금액 i를 표현할 수 있는 최소 화폐 수를 저장한다.
            }
          }
        }

      } //내부 for문 종료

    } //외부 for문 종료

    return d[m];
  }


  /** 점화식 (교재 풀이)
    금액 i를 만들 수 있다면: d[i] = min(d[i], d[i-k]+1), 단 k는 화폐 단위
    금액 i를 만들 수 없다면: d[i] = 10001
   */
  private static int solution2(int[] n, int m) {
    //d[i] = 금액 i를 만드는데 필요한 최소 화폐 수
    Arrays.fill(d, 10001); //m의 최대값이 10000이므로, 필요한 화폐 개수가 10000을 넘어간다면 만들 수 없는 금액이라는 뜻이다.
    d[0] = 0;

    //각 화폐단위를 하나씩 적용하며, 해당 화폐단위로 만들 수 있는 금액의 최소 화폐 수를 갱신한다.
    for (int moneyUnit : n) {

      for (int nowMoney = moneyUnit; nowMoney <= m; nowMoney++) {
        if (d[nowMoney - moneyUnit] != 10001) {
          d[nowMoney] = Math.min(d[nowMoney], d[nowMoney - moneyUnit] + 1);
        }
      }

    }

    if (d[m] == 10001) {
      return -1;
    }
    return d[m];
  }

  public static void execute() throws Exception {
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    String s = bf.readLine();
    StringTokenizer st = new StringTokenizer(s);
    int sizeOfN = Integer.parseInt(st.nextToken());
    int m = Integer.parseInt(st.nextToken());
    int[] n = new int[sizeOfN];

    for (int i = 0; i < sizeOfN; i++) {
      n[i] = Integer.parseInt(bf.readLine());
    }

    TimeCheck.start();

    int answer = solution2(n, m);

    TimeCheck.end();

    System.out.println(answer);
  }
}
