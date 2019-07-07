package sample;

public class test {
    public test() {
    }

    public static void main(String[] args) {
        int[][] array = new int[4][4];
        array[0][0] = -2;
        array[0][1] = -2;
        array[0][2] = -2;
        array[0][3] = -2;
        array[1][0] = -1;
        array[1][1] = -1;
        array[1][2] = -2;
        array[1][3] = -1;
        array[2][0] = -2;
        array[2][1] = -1;
        array[2][2] = -1;
        array[2][3] = -2;
        array[3][0] = -1;
        array[3][1] = -1;
        array[3][2] = -1;
        array[3][3] = -1;
        Coordinate b = new Coordinate(-2, -2);
        Lee res = new Lee(array, 2, 0);
        if (res.getPathToPoint(0, 3)) {
            b = res.getResultCord();
        }

        b.printCoord();
    }
}
