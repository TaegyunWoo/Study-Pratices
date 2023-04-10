package implementation;

public class 왕실의_나이트 {

    private static int answer = 0;

    public static int solution1(int row, char col) {
        //위로 이동하는 경우
        if (row > 2) {
            //왼쪽으로 이동하는 경우
            if (col > 'a') {
                answer++;
            }
            //오른쪽으로 이동하는 경우
            if (col < 'h') {
                answer++;
            }
        }
        //아래로 이동하는 경우
        if (row < 7) {
            //왼쪽으로 이동하는 경우
            if (col > 'a') {
                answer++;
            }
            //오른쪽으로 이동하는 경우
            if (col < 'h') {
                answer++;
            }
        }
        //왼쪽으로 이동하는 경우
        if (col > 'b') {
            //위로 이동하는 경우
            if (row > 1) {
                answer++;
            }
            //아래로 이동하는 경우
            if (row < 8) {
                answer++;
            }
        }
        //오른쪽으로 이동하는 경우
        if (col < 'g') {
            //위로 이동하는 경우
            if (row > 1) {
                answer++;
            }
            //아래로 이동하는 경우
            if (row < 8) {
                answer++;
            }
        }

        return answer;
    }
}
