package implementation;

import java.util.Arrays;

public class 게임_개발 {

    //가본적있는지 표시하는 배열
    private static boolean[][] wentMark; //true면 가본적있음

    private static int answer = 0;

    public static int solution1(int mapHeight, int mapWidth, int[][] map, int positionA, int positionB, int lookingSide) {
        int isThereAnyToGo = 0;
        initWentMarkArr(mapHeight, mapWidth, map);


        //좌측이 한번도 안간곳인지, 바다인지 확인
        while (true) {
            switch (lookingSide) {
                case 0 :
                    if (wentMark[positionA][positionB - 1]) {
                        //가본적이 있다면
                        lookingSide = turnLeft(lookingSide);
                        isThereAnyToGo++;
                    } else {
                        //가본적이 없다면
                        lookingSide = turnLeft(lookingSide);

                        //전진
                        int[] goForward = goForward(positionA, positionB, lookingSide);
                        positionA = goForward[0];
                        positionB = goForward[1];

                        //체크
                        wentMark[positionA][positionB] = true;
                    }
                    break;
                case 1 :
                    if (wentMark[positionA-1][positionB]) {
                        //가본적이 있다면
                        lookingSide = turnLeft(lookingSide);
                        isThereAnyToGo++;
                    } else {
                        //가본적이 없다면
                        lookingSide = turnLeft(lookingSide);

                        //전진
                        int[] goForward = goForward(positionA, positionB, lookingSide);
                        positionA = goForward[0];
                        positionB = goForward[1];

                        //체크
                        wentMark[positionA][positionB] = true;
                    }
                    break;
                case 2 :
                    if (wentMark[positionA][positionB+1]) {
                        //가본적이 있다면
                        lookingSide = turnLeft(lookingSide);
                        isThereAnyToGo++;
                    } else {
                        //가본적이 없다면
                        lookingSide = turnLeft(lookingSide);

                        //전진
                        int[] goForward = goForward(positionA, positionB, lookingSide);
                        positionA = goForward[0];
                        positionB = goForward[1];

                        //체크
                        wentMark[positionA][positionB] = true;
                    }
                    break;
                case 3 :
                    if (wentMark[positionA+1][positionB]) {
                        //가본적이 있다면
                        lookingSide = turnLeft(lookingSide);
                        isThereAnyToGo++;
                    } else {
                        //가본적이 없다면
                        lookingSide = turnLeft(lookingSide);

                        //전진
                        int[] goForward = goForward(positionA, positionB, lookingSide);
                        positionA = goForward[0];
                        positionB = goForward[1];

                        //체크
                        wentMark[positionA][positionB] = true;
                    }
                    break;
            }
            if (isThereAnyToGo == 4) {
                break;
            }
        }

        //뒤로 한칸 이동
        goBackIfCan(positionA, positionB, lookingSide, map);

        return answer;

    }

    public static int solution2(int mapHeight, int mapWidth, int[][] map, int positionA, int positionB, int lookingSide) {
        int isThereAnyToGo = 0;
        boolean isWent = false;
        initWentMarkArr(mapHeight, mapWidth, map);


        //좌측이 한번도 안간곳인지, 바다인지 확인
        while (true) {
            isWent = false;
            if (lookingSide == 0) {
                if (wentMark[positionA][positionB - 1]) {
                    //가본적이 있거나 바다라면
                    isWent = true;
                }
            } else if (lookingSide == 1) {
                if (wentMark[positionA-1][positionB]) {
                    //가본적이 있거나 바다라면
                    isWent = true;
                }
            } else if (lookingSide == 2) {
                if (wentMark[positionA][positionB+1]) {
                    //가본적이 있거나 바다라면
                    isWent = true;
                }
            } else if (lookingSide == 3) {
                if (wentMark[positionA+1][positionB]) {
                    //가본적이 있거나 바다라면
                    isWent = true;
                }
            }

            if (isWent) {
                //가본적이 있거나 바다라면
                lookingSide = turnLeft(lookingSide);
                isThereAnyToGo++;
            } else {
                //가본적이 없다면
                lookingSide = turnLeft(lookingSide);

                //전진
                int[] goForward = goForward(positionA, positionB, lookingSide);
                positionA = goForward[0];
                positionB = goForward[1];

                //체크
                wentMark[positionA][positionB] = true;
            }

            if (isThereAnyToGo == 4) {
                break;
            }
        }

        //뒤로 한칸 이동
        goBackIfCan(positionA, positionB, lookingSide, map);

        return answer;

    }

    private static void initWentMarkArr(int mapHeight, int mapWidth, int[][] map) {
        wentMark = new boolean[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++) {
            for (int u = 0; u < mapWidth; u++) {
                if (map[u][i] == 1) {
                    wentMark[i][u] = true;
                }
            }
        }
    }

    private static int turnLeft(int nowLookingSide) {
        if (nowLookingSide == 0) {
            return 3;
        }
        return nowLookingSide-1;
    }

    private static int[] goForward(int positionA, int positionB, int lookingSide) {
        switch (lookingSide) {
            case 0 :
                positionA--;
                break;
            case 1:
                positionB++;
                break;
            case 2:
                positionA++;
                break;
            case 3:
                positionB--;
                break;
        }
        answer++;
        return new int[] {positionA, positionB};
    }

    private static int[] goBackIfCan(int positionA, int positionB, int lookingSide, int[][] map) {
        switch (lookingSide) {
            case 0 :
                if (map[positionA+1][positionB] == 0) {
                    positionA++;
                    answer++;
                }
                break;
            case 1:
                if (map[positionA][positionB-1] == 0) {
                    positionB--;
                    answer++;
                }
                break;
            case 2:
                if (map[positionA-1][positionB] == 0) {
                    positionA--;
                    answer++;
                }
                break;
            case 3:
                if (map[positionA][positionB+1] == 0) {
                    positionB++;
                    answer++;
                }
                break;
        }
        return new int[] {positionA, positionB};
    }
}
