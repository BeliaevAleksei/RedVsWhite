package sample;

import java.util.Arrays;

public class Lee {
    private Coordinate aCoord;
    private int[][] grid;
    private int[] dx = new int[]{1, 0, -1, 0};
    private int[] dy = new int[]{0, 1, 0, -1};
    private int d;
    private int k;
    private boolean stop;
    private static final int WALL = -1;
    private static final int BLANK = -2;
    private Coordinate resultCord;

    public Lee(int[][] occupiedCells, int ax, int ay) {
         aCoord = new Coordinate(ax, ay);
         grid = Arrays.copyOf(occupiedCells, occupiedCells.length);
    }

    public Coordinate getResultCord() {
        return  resultCord;
    }

    public boolean getPathToPoint(int bx, int by) {
        Coordinate resCoord;
        int ax =  aCoord.getX();
        int ay =  aCoord.getY();
        grid[ax][ay] = 0;
        if ( grid[ax][ay] == WALL &&  grid[bx][by] == WALL) {
            return false;
        }
        resCoord =  propagationWave(bx, by);
        if (grid[bx][by] == BLANK) return false;
        resultCord =  recoveryPath(resCoord);
        return true;
    }

    public boolean getCoordWithLargerSector() {
        int ax =  aCoord.getX();
        int ay =  aCoord.getY();
        grid[ax][ay] = 0;
        Coordinate resCoord =  propagationWave(-1, -1);
        if (resCoord.getX() != -1) {
            resultCord =  recoveryPath(resCoord);
            return true;
        } else {
            return false;
        }
    }

    private Coordinate propagationWave(int bx, int by) {
        Coordinate fCoord = new Coordinate(-1, -1);
        int ax =  aCoord.getX();
        int ay =  aCoord.getY();
        d = 0;
        grid[ax][ay] = 0;

        do {
            stop = true;

            for(int x = 0; x <  grid.length; ++x) {
                for(int y = 0; y <  grid.length; ++y) {
                    if ( grid[x][y] ==  d) {
                        for( k = 0;  k < 4; ++ k) {
                            int iy = y +  dy[ k];
                            int ix = x +  dx[ k];
                            if (Coordinate.isBorder(ix, iy,  grid.length) &&  grid[ix][iy] == BLANK) {
                                stop = false;
                                grid[ix][iy] =  d + 1;
                                fCoord.setX(ix);
                                fCoord.setY(iy);
                            }
                        }
                    }
                }
            }

            d++;
        } while(!stop &&  (bx == -1 || grid[bx][by] == BLANK));

        return fCoord;
    }

    private Coordinate recoveryPath(Coordinate resCord) {
        int x = resCord.getX();
        int y = resCord.getY();

        while(true) {
            while( d > 1) {
                d--;
                for( k = 0;  k < 4; ++ k) {
                    int iy = y +  dy[ k];
                    int ix = x +  dx[ k];
                    if (Coordinate.isBorder(ix, iy,  grid.length) &&  grid[ix][iy] ==  d) {
                        x +=  dx[ k];
                        y +=  dy[ k];
                        break;
                    }
                }
            }

            resCord.setX(x);
            resCord.setY(y);
            return resCord;
        }
    }
}
