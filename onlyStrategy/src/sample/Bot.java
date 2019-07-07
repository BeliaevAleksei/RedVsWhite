package sample;

import java.io.IOException;
import sample.Controller.Tile;

public class Bot {
    private String tacticsPart1, tacticsPart2;
    private int x, y, sizeTable, value, pitchStroke;
    private int[][] table;
    private String oneDirection;
    private int move, q, w, c, v;

    public Bot(String tacticsPart1, String tacticsPart2, int x, int y, int sizeTable) {
        c = 0;
        v = 0;
        this.tacticsPart1 = tacticsPart1;
        this.tacticsPart2 = tacticsPart2;
        this.x = x;
        this.y = y;
        this.sizeTable = sizeTable;
        this.table = new int[sizeTable][sizeTable];
        table[0][0] = 1;
        table[9][9] = -1;
        move = 0;
        value = 1;
        pitchStroke = 1;
        oneDirection = "Up";
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getTacticsPart2() {
        return tacticsPart2;
    }

    public void setTacticsPart1(String tactics, int z) {
        tacticsPart1 = tactics;
        if (tacticsPart1.equals("Diagonal")) {
            value = 1;
            pitchStroke = 1;
            oneDirection = "Up";
        }

        if (tactics.equals("Snake")) {
            value = 1;
            pitchStroke = 1;
            oneDirection = "Down";
        }

        if (tactics.equals("Around")) {
            value = sizeTable - 1;
            pitchStroke = sizeTable - 1;
            oneDirection = "Right";
        }

        if (tacticsPart1.equals("Row by Row")) {
            value = sizeTable - 1;
            pitchStroke = sizeTable - 1;
            oneDirection = "Right";
        }

    }

    public void setTacticsPart2(String tactics) {
        tacticsPart2 = tactics;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int c) {
        move = c;
    }

    public String getTacticsPart1() {
        return tacticsPart1;
    }

    public int gameBotPart1(int a, int s, int z) {
        int check = 0;
        if (table[a][s] != -z) {
            table[a][s] = -z;
        }

        if (tacticsPart1.equals("Diagonal")) {
            check = diagonal(z);
        }

        if (tacticsPart1.equals("Snake")) {
            check = snake(z);
        }

        if (tacticsPart1.equals("Around")) {
            check = around(z);
        }

        if (tacticsPart1.equals("Row by Row")) {
            check = rowByRow(z);
        }

        if (tacticsPart1.equals("Mirroring")) {
            x = sizeTable - a - 1;
            y = sizeTable - s - 1;
        }

        if (tacticsPart1.equals("Hot filling")) {
            check = hotFilling();
        }

        table[x][y] = z;
        return check;
    }

    private int hotFilling() {
        int flag = moveInTable();
        if (flag != -1) {
            return flag;
        } else {
            table[c][v] = 0;
            return endMove(c, v);
        }
    }

    /**
     *
     * @return -1 - if the computer can only go to a dead end or result from method endMove
     */
    private int moveInTable() {
        byte side;
        if (y >= x) {
            side = 1;
        } else {
            side = -1;
        }

        Coordinate result;
        do {
            if (isBorderAndFree(side, "first")) { //вверх || вниз
                result = getCoordinate(x, y, x, y - side);
            } else if (x != 0 && table[x - 1][y] == 0) { //влево
                result = getCoordinate(x, y, x - 1, y);
            } else if (isBorderAndFree(side, "third")) { // вниз
                result = getCoordinate(x, y, x, y + side);
            } else {
                if (x == sizeTable - 1 || table[x + 1][y] != 0) {
                    return -1;
                }
                result = getCoordinate(x, y, x + 1, y); //вправо
            }
        } while(result.getY() == -1 || result.getX() == -1); //пока не изменится координата

        return endMove(result.getX(), result.getY());
    }

    /**
     *
     * @param ax x coordinate of the start cell
     * @param ay y coordinate of the start cell
     * @param bx x coordinate of the end cell
     * @param by y coordinate of the end cell
     * @return coordinate next cell. (-1, -1) - if (ax, ay) - dead end
     */
    private Coordinate getCoordinate(int ax, int ay, int bx, int by) {
        Coordinate resVertex = new Coordinate(-1, -1);
        if (getDegreeVertex(bx, by) == 0) {
            rememberCellAndModifyTable(bx, by);
            return resVertex;
        } else {
            if (getDegreeVertex(bx, by) == 1) {
                Lee result = new Lee(makeLeeTable(), ax, ay);
                if (!oneSector(ax, ay, bx, by)) {
                    if (result.getCoordWithLargerSector()) {
                        resVertex = result.getResultCord();
                        return resVertex;
                    }
                }
            }

            resVertex.setX(bx);
            resVertex.setY(by);
            return resVertex;
        }
    }

    /**
     *
     * @param ax x coordinate of the start cell
     * @param ay y coordinate of the start cell
     * @param bx x coordinate of the end cell
     * @param by y coordinate of the end cell
     * @return true - if you can route from the start to the end point and else false
     */
    private boolean oneSector(int ax, int ay, int bx, int by) {
        Lee result = new Lee(makeLeeTable(), bx, by);
        int[] dx = new int[]{1, 0, -1, 0};
        int[] dy = new int[]{0, 1, 0, -1};

        for(int k = 0; k < 4; ++k) {
            int ix = ax + dx[k];
            int iy = ay + dy[k];
            if (ix != bx && iy != by && check(ix, iy)) {
                if (result.getPathToPoint(ix, iy)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *  Create grid for Lee algorithm
     * @return grid for Lee algorithm
     */
    private int[][] makeLeeTable() {
        int[][] leeTable = new int[sizeTable][sizeTable];

        for(int i = 0; i < sizeTable; ++i) {
            for(int j = 0; j < sizeTable; ++j) {
                if (table[i][j] != 1 && table[i][j] != -1 && table[i][j] != 2) {
                    leeTable[i][j] = -2;
                } else {
                    leeTable[i][j] = -1;
                }
            }
        }

        return leeTable;
    }

    /**
     * Remember dead end cell in c and v coordinate
     * @param q x coordinate cell
     * @param w y coordinate cell
     */
    private void rememberCellAndModifyTable(int q, int w) {
        table[q][w] = 2;
        c = q;
        v = w;
    }

    private boolean isBorderAndFree(int side, String part) {
        if (side == -1 && part.equals("first") && y != sizeTable - 1 && table[x][y - side] == 0) {
            return true;
        } else if (side == -1 && part.equals("third") && y != 0 && table[x][y + side] == 0) {
            return true;
        } else if (side == 1 && part.equals("first") && y != 0 && table[x][y - side] == 0) {
            return true;
        } else {
            return side == 1 && part.equals("third") && y != sizeTable - 1 && table[x][y + side] == 0;
        }
    }

    private int getDegreeVertex(int q, int w) {
        int degreeVertex = 0;
        int[] dx = new int[]{1, 0, -1, 0};
        int[] dy = new int[]{0, 1, 0, -1};

        for(int k = 0; k < 4; ++k) {
            int ix = q + dx[k];
            int iy = w + dy[k];
            degreeVertex += check(ix, iy) ? 1 : 0;
        }

        return degreeVertex;
    }

    private int diagonal(int z) {
        int q = x;
        int w = y;
        if (oneDirection.equals("Up")) {
            w = y + z;
            oneDirection = "Left";
        } else if (oneDirection.equals("Left")) {
            q = x + z;
            oneDirection = "Up";
        }

        return endMove(q, w);
    }

    private int snake(int z) {
        int q = x;
        int w = y;
        if (oneDirection.equals("Down")) {
            w = y + z;
            value--;
            if (value == 0) {
                if ((z == 1 && x == 0) || (z == -1 && x == 9)) {
                    oneDirection = "Right";
                    if (pitchStroke != 1) {
                        pitchStroke++;
                    }
                    value = pitchStroke;
                } else {
                    oneDirection = "Left";
                    value = pitchStroke;
                }
            }
        } else if (oneDirection.equals("Right")) {
            q = x + z;
            value--;
            if (value == 0) {
                if ((z == 1 && y == 0) || (z == -1 && y == 9)) {
                    oneDirection = "Down";
                    pitchStroke++;
                    value = pitchStroke;
                } else {
                    oneDirection = "Up";
                    value = pitchStroke;
                }
            }
        } else if (oneDirection.equals("Up")) {
            w = y - z;
            value--;
            if (value == 0) {
                oneDirection = "Right";
                value = 1;
            }
        } else if (oneDirection.equals("Left")) {
            q = x - z;
            value--;
            if (value == 0) {
                oneDirection = "Down";
                value = 1;
            }
        }
        return (endMove(q, w));
    }

    private int around(int z) {
        int q = x;
        int w = y;
        if (oneDirection.equals("Right")) {
            q = x + z;
            value--;
            if (value == 0) {
                if (pitchStroke == 9) {
                    pitchStroke--;
                } else {
                    pitchStroke = pitchStroke - 2;
                }
                oneDirection = "Down";
                value = pitchStroke;
            }
        } else if (oneDirection.equals("Down")) {
            w = y + z;
            value--;
            if (value == 0) {
                oneDirection = "Left";
                value = pitchStroke;
            }
        } else if (oneDirection.equals("Left")) {
            q = x - z;
            value--;
            if (value == 0) {
                oneDirection = "Up";
                pitchStroke = pitchStroke - 2;
                value = pitchStroke;
            }
        } else if (oneDirection.equals("Up")) {
            w = y - z;
            value--;
            if (value == 0) {
                oneDirection = "Right";
                value = pitchStroke;
            }
        }
        return (endMove(q, w));
    }

    private int rowByRow(int z) {
        int q = x;
        int w = y;
        if (oneDirection.equals("Right")) {
            if (value == 0) {
                w = y + z;
                oneDirection = "Left";
                value = pitchStroke;
            } else {
                q = x + z;
                value--;
            }
        } else if (oneDirection.equals("Left")) {
            if (value == 0) {
                w = y + z;
                oneDirection = "Right";
                value = pitchStroke;
            } else {
                q = x - z;
                value--;
            }
        }
        return (endMove(q, w));
    }

    private boolean check(int a, int s) {
        try {
            return table[a][s] == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private int endMove(int q, int w) {
        if (q > -1 && w > -1 && q < sizeTable && w < sizeTable) {
            if (check(q, w)) {
                x = q;
                y = w;
                return 0;
            }
        }
        return 3;
    }

    public void gameBotPart2(int x, int y, int z, Tile[][] massiv){
        for(int i = 0; i < sizeTable; ++i) {
            for(int j = 0; j < sizeTable; ++j) {
                table[i][j] = massiv[i][j].valueCell;
            }
        }

        if (table[x][y] != -z) {
            table[x][y] = -z;
        }

        if (tacticsPart2.equals("Attack")) {
            attack(x, y, z);
        }

        if (tacticsPart2.equals("Defence")) {
            defence(x, y, z);
        }

    }

    private void defence(int a, int s, int z) {
        q = x - a;
        w = y - s;
        convergence(q, w, a, s, z);
    }

    private void convergence(int q, int w, int a, int s, int z) {
        if (q > 0 && w > 0) { // слева вверху
            if (x - 1 == a && y == s) {
                table[x][y - 1] = z;
                y = y - 1;
            } else {
                table[x - 1][y] = z;
                x = x - 1;
            }
        } else if (q > 0 && w == 0) {//слева
            if (x - 1 == a && y == s) {
                if (y - 1 >= 0) {
                    table[x][y - 1] = z;
                    y = y - 1;
                } else {
                    table[x][y + 1] = z;
                    y = y + 1;
                }
            } else {
                table[x - 1][y] = z;
                x = x - 1;
            }

        } else if (q > 0 && w < 0) {//слева внизу
            if (x - 1 == a && y == s) {
                table[x][y + 1] = z;
                y = y + 1;

            } else {
                table[x - 1][y] = z;
                x = x - 1;
            }
        } else if (q == 0 && w < 0) {//снизу
            if (y + 1 == s && x == a) {
                if (x + 1 < sizeTable) {
                    table[x + 1][y] = z;
                    x = x + 1;
                } else {
                    table[x - 1][y] = z;
                    x = x - 1;
                }
            } else {
                table[x][y + 1] = z;
                y = y + 1;
            }

        } else if (q < 0 && w < 0) {//справа внизу
            if (x + 1 == a && y == s) {
                table[x][y + 1] = z;
                y = y + 1;

            } else {
                table[x + 1][y] = z;
                x = x + 1;
            }
        } else if (q < 0 && w == 0) {//справа
            if (x + 1 == a && y == s) {
                if (y - 1 >= 0) {
                    table[x][y - 1] = z;
                    y = y - 1;
                } else {
                    table[x][y + 1] = z;
                    y = y + 1;
                }
            } else {
                table[x + 1][y] = z;
                x = x + 1;
            }
        } else if (q < 0 && w > 0) {//справа вверху
            if (x + 1 == a && y == s) {
                table[x][y - 1] = z;
                y = y - 1;

            } else {
                table[x + 1][y] = z;
                x = x + 1;
            }
        } else if (q == 0 && w > 0) {//сверху
            if (y - 1 == s && x == a) {
                if (x + 1 < sizeTable) {
                    table[x + 1][y] = z;
                    x = x + 1;
                } else {
                    table[x - 1][y] = z;
                    x = x - 1;
                }
            } else {
                table[x][y - 1] = z;
                y = y - 1;
            }
        }

    }

    private void attack(int a, int s, int z){
        int minValue = table.length + table.length + 5;
        for (int j = 0; j < table.length; j++) {
                for (int i = 0; i < table.length; i++)
                    if (table[i][j] == -z && !(i == a && j == s)) {
                        if (minValue > Math.abs(x - i) + Math.abs(y - j)) {
                            minValue = Math.abs(x - i) + Math.abs(y - j);
                            q = i;
                            w = j;
                        }
                    }
        }
        q = x - q;
        w = y - w;
        convergence(q, w, a, s, z);
    }
}
