package sample;

public class Player {
    private int x;
    private int y;
    private int move;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.move = 0;
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setMove(int c) {
        this.move = c;
    }

    public int getMove() {
        return this.move;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean checkPosition(int w, int h) {
        if ((this.x - 1 == w || this.x + 1 == w || this.x == w) && (this.y - 1 == h || this.y + 1 == h || this.y == h)) {
            if (this.x - 1 == w && this.y == h) {
                return true;
            }

            if (this.x == w && this.y - 1 == h) {
                return true;
            }

            if (this.x == w && this.y + 1 == h) {
                return true;
            }

            if (this.x + 1 == w && this.y == h) {
                return true;
            }
        }

        return false;
    }
}
