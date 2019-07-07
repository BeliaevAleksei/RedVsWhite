package sample;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GetPoints {

    public static void main(String[] args) throws IOException {
        Game test = new Game();
        test.gameStart();
    }

}
class Game{
    private static int endPart;
    private static int[][] grid = new int[10][10];
    private int moveValue = 100;
    Bot botOne;
    Bot botTwo;
    private int[] p1 = new int[3];
    private int[] p2 = new int[3];
    private int pointP1 = 3, pointP2 = 3;

    public void gameStart() throws IOException {
        String points= "";
        String pointsWithOut2Part= "";
        clearFile();
        boolean isFirstTick = true;
        String tactics[] = {"Diagonal Offensive", "Mirror protection",
                "Diagonal", "Snake", "Around", "Row by Row", "Mirroring"};
        String tacticsP2[] = {"Attack", "Defence","Productive attack"};
        for(int k = 0; k < tacticsP2.length; k++) {
            for (int m = 0; m < tacticsP2.length; m++) {

                printToTxt(tacticsP2[k] + " " +tacticsP2[m]);

                for (int i = 0; i < tactics.length - 1; i++) {
                    for (int j = 0; j < tactics.length; j++) {
                        if(!(i == 1 || j == 0))
//                        {
//                            points += "-/-;" ;
//                            if (isFirstTick) {
//                                pointsWithOut2Part += "-/-;";
//                            }
//                        }
//                        else
                            {
                            grid[9][9] = -1;
                            grid[0][0] = 1;
                            botOne = new Bot("Diagonal", "Attack", 0, 0, 10);
                            botTwo = new Bot("Diagonal", "Attack", 9, 9, 10);

                            Coordinate b1 = new Coordinate(0, 0);
                            Coordinate b2 = new Coordinate(9, 9);
                            botOne.setCoordinates(0, 0);
                            botTwo.setCoordinates(9, 9);
                            if (i > 1) {
                                botOne.setTacticsPart1(tactics[i], b1);
                            } else {
                                botOne.setMixedTactic(tactics[i], b1);
                            }

                            if (j > 1) {
                                botTwo.setTacticsPart1(tactics[j], b2);
                            } else {
                                botTwo.setMixedTactic(tactics[j], b2);
                            }

                            if (k < 2) {
                                botOne.setTacticsPart2(tacticsP2[k]);
                            } else {
                                botOne.setMixedTacticPart2(tacticsP2[k]);
                            }

                            if (m < 2) {
                                botTwo.setTacticsPart2(tacticsP2[m]);
                            } else {
                                botTwo.setMixedTacticPart2(tacticsP2[m]);
                            }

                            while (endPart != 2) {
                                botGame(botTwo.getX(), botTwo.getY(), 1, 1, botOne, botTwo.getMove());
                                botGame(botOne.getX(), botOne.getY(), -1, 1, botTwo, botOne.getMove());
                            }
                            if (isFirstTick) {
                                pointsWithOut2Part += pointP1 + "/" + pointP2 + ";";
                            }
                            PCvsPC();
//                            points += pointP1 + "/" + pointP2 + ";";

                                if (pointP1 > pointP2){
                                    p1[0] ++;
                                }
                                else if (pointP1 == pointP2){
                                    p1[1] ++;
                                }
                                else {
                                    p1[2]++;
                                }
                            clearParam();
                        }
                    }
//                    points += "\n";
                    pointsWithOut2Part += "\n";
                    points += p1[0] + "/" + p1[1] + "/" + p1[2];
                    printToTxt(points);
                    points = "";
                    for(int l = 0; l < 3; l ++){
                        p1[l] = 0;
                    }
                }
//                if (isFirstTick) {
//                    printToTxt("Part 1");
//                    printToTxt(pointsWithOut2Part);
//                }
//                isFirstTick = false;
//                printToTxt(tacticsP2[k] + " " +tacticsP2[m]);
//                printToTxt(points);
//                points = "";
            }
        }



    }

    private void printToTxt(String txt) {
        try(FileWriter writer = new FileWriter("points.txt", true))
        {
            writer.write(txt);
            writer.append('\n');
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }


    private void clearFile() throws IOException {
        try(FileWriter writer = new FileWriter("points.txt")){
            writer.write("");
            writer.close();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void clearParam() {
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid.length; j++){
                grid[i][j] = 0;
            }
        }
        pointP1 = 3;
        pointP2 = 3;
        endPart = 0;
        moveValue = 100;
    }

    private void botGame(int x, int y, int z, int partGame, Bot bot, int enemyMobility) {
        if (partGame == 1) {
            if (bot.getMove() == 0) {
                bot.setMove(checkMove(bot.getX(), bot.getY()));
                if (bot.getMove() == 0) {
                    bot.gameBotPart1(x, y, z);
                    bot.setMove(checkMove(bot.getX(),bot.getY()));
                    setFillLight(bot.getX(), bot.getY(), z, partGame);
                    grid[bot.getX()][bot.getY()] = z;
                }
            }
        } else {
            bot.gameBotPart2(x, y, z, grid);
            setFillLight(bot.getX(), bot.getY(), z, partGame);
        }

    }

    private static int checkMove(int x, int y) {
        int[] ax = new int[]{1, 0, -1, 0};
        int[] ay = new int[]{0, 1, 0, -1};

        for(int k = 0; k < 4; ++k) {
            int ix = x + ax[k];
            int iy = y + ay[k];
            if(Coordinate.isBorder(ix, iy, 10) && grid[ix][iy] == 0) {
                return 0;
            }
        }
        if (endPart == 0) {
            endPart = 1;
            return 1;
        } else {
            endPart = 2;
            return 2;
        }

    }

    private void PCvsPC(){
        while (moveValue != 0) {
            if (botOne.getMove() == 1) {
                botGame(botTwo.getX(), botTwo.getY(), 1, 2, botOne, botTwo.getMove());
                botGame(botOne.getX(), botOne.getY(), -1, 2, botTwo, botOne.getMove());
                moveValue--;
            } else {
                botGame(botOne.getX(), botOne.getY(), -1, 2, botTwo, botOne.getMove());
                botGame(botTwo.getX(), botTwo.getY(), 1, 2, botOne, botTwo.getMove());
                moveValue--;

            }
        }
    }


    private void setFillLight(int a, int s, int z, int partGame) {
        if (partGame == 1) {
            setFillLightPart1(a, s, z);

        } else {
            setFillLightPart2(a, s, z);
        }
    }

    private void setFillLightPart1(int a, int s, int z) {
        if (z == 1) {
            pointP1 = pointP1 + 3;
        } else if (z == -1) {
            pointP2 = pointP2 + 3;
        }
    }

    private void setFillLightPart2(int a, int s, int z) {
        if (z == 1) {
            if (grid[a][s] == -1) {
                grid[a][s] = 1;
                pointP1 = pointP1 + 2;
            } else if (grid[a][s] == 0){
                grid[a][s] = 1;
                pointP1 = pointP1 + 1;
            }

        } else if (z == -1) {
            if (grid[a][s] == 1) {
                grid[a][s] = -1;
                pointP2 = pointP2 + 2;
            } else if (grid[a][s] == 0) {
                grid[a][s] = -1;
                pointP2 = pointP2 + 1;
            }
        }
    }

}
