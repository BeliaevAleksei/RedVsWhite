package sample;

import sample.Controller.Tile;

public class Bot {
    private String tacticsPart1, tacticsPart2, mixedTactic = "none", mixedTacticP2 = "none";
    private int x, y, sizeTable, value, pitchStroke;
    private int[][] table;
    private String oneDirection;
    private int oldX, oldY, move, c, v, q, w;
    private Coordinate startCoordinate = new Coordinate(0,0);
    private boolean diagOfensChecker = true, flagCheck= true;
    private int[] sumCell;

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

    public void setTacticsPart1(String tactics, Coordinate startCoordinate) {
        tacticsPart1 = tactics;

        switch (tactics) {
            case "Diagonal": setDataForTactics(1,1,"Up", startCoordinate); break;
            case "Snake": setDataForTactics(1,1,"Down", startCoordinate); break;
            case "Around": setDataForTactics(sizeTable - 1,sizeTable - 1,"Right", startCoordinate); break;
            case "Row by Row": setDataForTactics(sizeTable - 1,sizeTable - 1,"Right", startCoordinate); break;
            default: break;
        }
    }

    private void setDataForTactics (int value, int pitchStroke, String direction, Coordinate startCoordinate) {
        this.value = value;
        this.pitchStroke = pitchStroke;
        this.oneDirection = direction;
        this.startCoordinate = startCoordinate;
    }

    public void setMixedTactic(String tactics, Coordinate startCoordinate) {
        mixedTactic = tactics;
        switch (tactics) {
            case "Mirror protection": setTacticsPart1("Mirroring", startCoordinate); break;
            case "Diagonal Offensive": setTacticsPart1("Diagonal", startCoordinate); break;
            default: break;
        }
        sumCell = new int[2];
    }

    public void setMixedTacticPart2(String tactic){
        this.mixedTacticP2 = tactic;
        switch (tactic){
            case "Productive attack": setTacticsPart2("Attack");
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

    public void gameBotPart1(int a, int s, int z) {
        setEnemyCellInTable(a, s, z);

        if (mixedTactic.equals("none")) {
            clearGame(a, s, z);
        }
        else {
            mixedGame(a, s, z);
        }
        setTableValueAndCoordinate(a, s, z);
    }

    private void clearGame(int a, int s, int z) {
        int check;
        check = clearStrategy(a, s, z);
        if (check != 0 && !(tacticsPart1.equals("Hot filling"))) {
            tacticsPart1 = "Hot filling";
            clearStrategy(a, s, z);
        }
    }


    private int clearStrategy(int ax, int ay, int z) {
        int check = 0;

        switch (tacticsPart1) {
            case "Diagonal": check = diagonal(z); break;
            case "Snake": check = snake(z); break;
            case "Around": check = around(z); break;
            case "Row by Row": check = rowByRow(z); break;
            case "Mirroring": mirroring(ax, ay, false); break;
            case "Hot filling": check = hotFilling(); break;
            default: break;
        }
        return check;
    }

    private void setTableValueAndCoordinate(int a, int s, int z) {
        table[x][y] = z;
        oldX = a;
        oldY = s;
    }

    private void setEnemyCellInTable(int ax, int ay, int z) {
        if (table[ax][ay] != -z) {
            table[ax][ay] = -z;
        }
    }

    private void mixedGame(int a, int s, int z) {
        switch (mixedTactic) {
            case "Mirror protection" : mirrorProtection(a, s, z); break;
            case "Diagonal Offensive": diagonalOffensive(a, s, z); break;
            default: break;
        }
        checkLastMoveMixTactics(a, s, z, q, w);
    }

    private void diagonalOffensive(int ax, int ay, int z) {
        if(tacticsPart1.equals("Hot filling")){
            clearStrategy(ax, ay, z);
            return;
        }
        int rangeEnemy = rangeFrom(ax, ay);

        if(Math.abs(ax - x) == 1 && Math.abs(ay - y) == 1) {
            blockEnemy();
        }
        else {
            flagCheck = true;
            if (rangeEnemy > -2 && rangeEnemy < 2) {
                addPointToTrinangle(ax, ay);
                if (diagOfensChecker) {
                    if (x - y != 0 && ax != sizeTable - x && ay != sizeTable - y) {
                        diagOfensChecker = false;
                    }
                } else {
                    if (oneDirection.equals("Up")) {
                        oneDirection = "Left";
                    } else {
                        oneDirection = "Up";
                    }
                    diagOfensChecker = true;
                }
                q = x;
                w = y;
                clearStrategy(ax, ay, z);
                checkAndFindLargeSector(ax, ay, z);

            } else {
                startCoordinate.setY(y);
                startCoordinate.setX(x);
                q = x;
                w = y;
                chase(ax, ay, z);
                if (x > y) {
                    oneDirection = "Up";
                } else {
                    oneDirection = "Left";
                }
                diagOfensChecker = true;
            }
        }
        addPointToTrinangle(x, y);
    }

    private void blockEnemy() {
        q = x;
        w = y;
        if(flagCheck) {
            if (x > y) {
                oneDirection = "Up";
            } else if (x < y) {
                oneDirection = "Left";
            }
            flagCheck = false;
        }
        if (oneDirection.equals("Up")){
            w = y;
            y = y + 1;
        }
        else {
            q = x;
            x = x + 1;
        }
    }


    private void addPointToTrinangle(int ax, int ay){
        if(ax - ay > 0) {
            sumCell[0] ++;
        }
        else if(ax - ay < 0) {
            sumCell[1] ++;
        }
    }
    private void checkAndFindLargeSector(int ax, int ay, int z) {
        if (checkEnemyCell(x, y, z)) {
            if(sumCell[0] < sumCell[1]){
                if(x - y <= 0){
                    x = q + 1;
                    y = w;
                }
            }
            else {
                if (sumCell[0] > sumCell[1]) {
                    if(x - y >= 0){
                        x = q;
                        y = w + 1;
                    }
                }
            }
        }
    }

    private void checkLastMoveMixTactics(int ax, int ay, int z, int q, int w) {
        if (checkEnemyCell(x, y, z) && !mixedTactic.equals("Diagonal Offensive")){
            startCoordinate.setX(x);
            startCoordinate.setY(y);
            setTacticsPart1("Hot filling", startCoordinate);
            setMixedTactic("none", startCoordinate);
        }
        if(endMove(x, y) != 0 || getDegreeVertex(x, y) == 0){
            x = q;
            y = w;
            startCoordinate.setX(x);
            startCoordinate.setY(y);
            setTacticsPart1("Hot filling", startCoordinate);
            clearStrategy(ax, ay, z);
            setMixedTactic("none", startCoordinate);
        }
    }

    private void mirrorProtection(int ax, int ay, int z) {
        if(tacticsPart1.equals("Hot filling")){
            clearStrategy(ax, ay, z);
            return;
        }
        int rangeEnemy = rangeFrom(ax, ay);

        if (rangeEnemy > -2 && rangeEnemy < 2) {
            q = x;
            w = y;
            mirroring(ax, ay, false);
            if (rangeEnemy + rangeFrom(x, y) != 0) {
                x = q;
                y = w;
                mirroring(ax, ay, true);
            }
        } else  {
            q = x;
            w = y;
            chase(ax, ay, z);
        }
    }

    private void chase(int ax, int ay, int z) {
        switch (z) {
            case -1 :if (ax > ay && oldY <= ay){ mirroring(ax, ay, true); break;}
                    if (ax > ay && oldY > ay){ mirroring(ax, ay + 2, false); break;}
                    if (ax <= ay && oldX <= ax){ mirroring(ax, ay, true); break;}
                    if (ax <= ay && oldX > ax) {mirroring(ax + 2, ay, false);break;}

            case 1:
                    if (ax > ay && oldX < ax && oldY == ay){ mirroring(ax - 2, ay, false); break;}//слева лево-низ
                    if (ax > ay && oldX == ax && oldY < ay){ mirroring(ax, ay - 2, false); break;}//слева право-низ
                    if (ax > ay && oldX >= ax){ mirroring(ax, ay, true); break;} //слева вверх

                    if (ax <= ay && oldX == ax && oldY < ay){ mirroring(ax, ay - 2, false); break;}//справа пр-низ
                    if (ax <= ay && oldX < ax && oldY == ay){ mirroring(ax - 2, ay, false); break;}//справа лв-низ
                    if (ax <= ay && oldY >= ay) {mirroring(ax, ay, true);break;}//справа выше
        }
    }

    private int rangeFrom(int ax, int ay) {
        return ax - ay;
    }


    private boolean checkEnemyCell(int ax, int ay, int z) {
        int[] dx = new int[]{1, 0, -1, 0};
        int[] dy = new int[]{0, 1, 0, -1};
        int enemyZ = z * -1;
        for (int k = 0; k < 4; ++k) {
            int ix = ax + dx[k];
            int iy = ay + dy[k];
            if (Coordinate.isBorder(ix, iy, table.length) && table[ix][iy] == enemyZ) {
                return true;
            }
        }
        return false;
    }



    private void mirroring(int ax, int ay, boolean invert) {
        if(invert){
              x = x + oldY - ay;
            y = y + oldX - ax;
        }
        else {
            x = x + oldX - ax;
            y = y + oldY - ay;
        }
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
     * @return coordinate next cell. (-1, -1) - if (ax, ay) - end point
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
        switch (oneDirection) {
            case "Down":
                w = y + z; // Down|Up <- z == -1 | 1

                value--;
                if (value == 0) {
                    if ((z == 1 && x == startCoordinate.getX()) || (z == -1 && x == startCoordinate.getX())) {
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
                break;
            case "Right":
                q = x + z;
                value--;
                if (value == 0) {
                    if ((z == 1 && y == startCoordinate.getY()) || (z == -1 && y == startCoordinate.getY())) {
                        oneDirection = "Down";
                        pitchStroke++;
                        value = pitchStroke;
                    } else {
                        oneDirection = "Up";
                        value = pitchStroke;
                    }
                }
                break;
            case "Up":
                w = y - z;
                value--;
                if (value == 0) {
                    oneDirection = "Right";
                    value = 1;
                }
                break;
            case "Left":
                q = x - z;
                value--;
                if (value == 0) {
                    oneDirection = "Down";
                    value = 1;
                }
                break;
        }
        return (endMove(q, w));
    }

    private int around(int z) {
        int q = x;
        int w = y;
        switch (oneDirection) {
            case "Right":
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
                break;
            case "Down":
                w = y + z;
                value--;
                if (value == 0) {
                    oneDirection = "Left";
                    value = pitchStroke;
                }
                break;
            case "Left":
                q = x - z;
                value--;
                if (value == 0) {
                    oneDirection = "Up";
                    pitchStroke = pitchStroke - 2;
                    value = pitchStroke;
                }
                break;
            case "Up":
                w = y - z;
                value--;
                if (value == 0) {
                    oneDirection = "Right";
                    value = pitchStroke;
                }
                break;
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

    public void gameBotPart2(int a, int s, int z, Tile[][] massiv){
        for(int i = 0; i < sizeTable; ++i) {
            for(int j = 0; j < sizeTable; ++j) {
                table[i][j] = massiv[i][j].valueCell;
            }
        }

        if (table[a][s] != -z) {
            table[a][s] = -z;
        }

        playTacticsPart2(a, s, z);

    }

    private void playTacticsPart2(int a, int s, int z) {
        if(mixedTacticP2.equals("Productive attack")) {
            productiveAttack(a, s, z);
        }
        else if (tacticsPart2.equals("Attack")) {
            attack(a, s, z);
        }

        else if (tacticsPart2.equals("Defence")) {
            defence(a, s, z);
        }
    }

    public void gameBotPart2(int a, int s, int z, int[][] massiv){
        for(int i = 0; i < sizeTable; ++i) {
            System.arraycopy(massiv[i], 0, table[i], 0, sizeTable);
        }

        if (table[a][s] != -z) {
            table[a][s] = -z;
        }
        playTacticsPart2(a, s, z);

    }

    private void productiveAttack(int ax, int ay, int z) {
        attack(ax, ay, z);
        if(Math.abs(ax - x) == 1 && Math.abs(ay - y) == 0 || Math.abs(ax - x) == 0 && Math.abs(ay - y) == 1){
            tacticsPart2 = "Defence";
            setMixedTacticPart2("none");
        }
    }


    private void defence(int a, int s, int z) {
        int rangeToX = x - a;
        int rangeToY = y - s;
        convergence(rangeToX, rangeToY, a, s, z);
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

        } else if (q > 0) {//слева внизу
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
        int rangeToX;
        int rangeToY;
        int ax = 0, ay = 0;
        int minValue = table.length + table.length + 5;
        for (int j = 0; j < table.length; j++) {
                for (int i = 0; i < table.length; i++)
                    if (table[i][j] == -z && !(i == a && j == s)) {
                        if (minValue > Math.abs(x - i) + Math.abs(y - j)) {
                            minValue = Math.abs(x - i) + Math.abs(y - j);
                            ax = i;
                            ay = j;
                        }
                    }
        }
        rangeToX = x - ax;
        rangeToY = y - ay;
        convergence(rangeToX, rangeToY, a, s, z);
    }
}
