package sample;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static boolean isBorder(int x, int y, int sizeGrid) {
        return x >= 0 && x < sizeGrid && y >= 0 && y < sizeGrid;
    }

    public void printCoord() {
        System.out.printf("x: %d , y: %d", x, y);
    }
}
