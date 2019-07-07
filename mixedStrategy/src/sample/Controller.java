package sample;

import com.sun.deploy.net.cookie.CookieUnavailableException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.Console;
import java.io.IOException;

public class Controller {
    public int movePart2 = 0;
    private int pointP1 = 0;
    private int pointP2 = 0;
    private byte endPart = 0;
    private int moveValue = 100;
    private boolean movePlayer = true; // true - 1player false - 2player
    private String whoPlaying = "player vs pc";
    private String botOneMixTac, botTwoMixTac;

    private static final int TILE_SIZE = 40;
    private static final int W = 400;
    private static final int H = 400;

    private static final int X_TILES = W / TILE_SIZE;
    private static final int Y_TILES = H / TILE_SIZE;

    public Tile[][] grid = new Tile[X_TILES][Y_TILES];
    private HBox hbox = new HBox();

    private Player player1 = new Player(0, 0);
    private Player player2 = new Player(0, 0);
    private Bot botOne = new Bot("Diagonal", "Attack", 0, 0, X_TILES);
    private Bot botTwo = new Bot("Diagonal", "Attack", 0, 0, X_TILES);

    /** */
    @FXML
    public Label point1;
    public Label point2;
    public Label gamePart;
    public Menu Bot1;
    public Menu Bot2;
    public Menu playList;
    public Menu mixedStrategy;
    public AnchorPane timeToTable;
    public Button start;
    public HBox hBoxMoves;
    public Label MovesToTheEnd;
    public ToggleGroup MixedStrategy1Snake;

    private Main main;

    public Controller (Main main) {
        this.main = main;
    }
    public void setPlayers(ActionEvent actionEvent) {
        RadioMenuItem itemId = (RadioMenuItem) actionEvent.getSource();
        whoPlaying = itemId.getId();
        if (whoPlaying.equals("player vs pc")) {
            start.disableProperty().set(false);
            Bot1.disableProperty().set(true);
            Bot2.disableProperty().set(false);
            player1.setCoordinates(0, 0);
            botTwo.setCoordinates(9, 9);
        } else if (whoPlaying.equals("pc vs player")) {
            start.disableProperty().set(false);
            Bot1.disableProperty().set(false);
            Bot2.disableProperty().set(true);
            botOne.setCoordinates(0, 0);
            player1.setCoordinates(9, 9);

        } else if (whoPlaying.equals("pc vs pc")) {
            start.disableProperty().set(false);
            Bot1.disableProperty().set(false);
            Bot2.disableProperty().set(false);
            botOne.setCoordinates(0, 0);
            botTwo.setCoordinates(9, 9);
        } else if (whoPlaying.equals("player vs player")) {
            start.disableProperty().set(false);
            Bot1.disableProperty().set(true);
            Bot2.disableProperty().set(true);
            player1.setCoordinates(0, 0);
            player2.setCoordinates(9, 9);
        }

    }


    @FXML
    public void startGame(MouseEvent mouseEvent) throws IOException {
        Bot1.disableProperty().set(true);
        Bot2.disableProperty().set(true);
        playList.disableProperty().set(true);
        start.disableProperty().set(true);
        timeToTable.getChildren().addAll(hbox, createContent());
        if (whoPlaying.equals("pc vs player")) {
            grid[0][0].botGame(9, 9, 1, 1, botOne, player1.getMove());
        }
        if (whoPlaying.equals("pc vs pc")) {
            botVsBot();
        }

    }

    private void botVsBot() throws IOException {
        while (endPart != 2) {
            grid[0][0].botGame(botTwo.getX(), botTwo.getY(), 1, 1, botOne, botTwo.getMove());
            grid[0][0].botGame(botOne.getX(), botOne.getY(), -1, 1, botTwo, botOne.getMove());
        }
        grid[0][0].fromFirstPartToSecond();
    }


    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(400, 400);

        for (int y = 0; y < X_TILES; y++) {
            for (int x = 0; x < Y_TILES; x++) {
                Tile tile = new Tile(x, y);
                grid[x][y] = tile;
                root.getChildren().add(tile);
            }
        }
        return root;
    }

    public void setTacticsBot1Part1(ActionEvent actionEvent) {
        Coordinate startCoordinate = new Coordinate(0,0);
        RadioMenuItem itemId = (RadioMenuItem) actionEvent.getSource();
        botOne.setTacticsPart1(itemId.getId(), startCoordinate);
        botOne.setMixedTactic("none", startCoordinate);
    }

    public void setMixedTacticB1Part2(ActionEvent actionEvent) {
        RadioMenuItem itemId = (RadioMenuItem) actionEvent.getSource();
        botOne.setMixedTacticPart2(itemId.getId());
    }

    public void setMixedTacticB2Part2(ActionEvent actionEvent) {
        RadioMenuItem itemId = (RadioMenuItem) actionEvent.getSource();
        botTwo.setMixedTacticPart2(itemId.getId());
    }


    public void setTacticsBot1Part2(ActionEvent actionEvent) {
        RadioMenuItem itemId = (RadioMenuItem) actionEvent.getSource();
        botOne.setTacticsPart2(itemId.getId());
        botOne.setMixedTacticPart2("none");
    }

    public void setTacticsBot1MixedStr (ActionEvent actionEvent) {
        Coordinate startCoordinate = new Coordinate(0,0);
        RadioMenuItem itemId = (RadioMenuItem) actionEvent.getSource();
        botOne.setMixedTactic(itemId.getId(), startCoordinate);
    }

    public void setTacticsBot2MixedStr (ActionEvent actionEvent) {
        start.disableProperty().set(false);
        Coordinate startCoordinate = new Coordinate(9,9);
        RadioMenuItem itemId = (RadioMenuItem) actionEvent.getSource();
        botTwo.setMixedTactic(itemId.getId(), startCoordinate);
    }
    public void setTacticsBot2Part1(ActionEvent actionEvent) {
        Coordinate startCoordinate = new Coordinate(9,9);
        RadioMenuItem itemId = (RadioMenuItem) actionEvent.getSource();
        botTwo.setTacticsPart1(itemId.getId(), startCoordinate);
        botTwo.setMixedTactic("none", startCoordinate);
    }

    public void setTacticsBot2Part2(ActionEvent actionEvent) {
        start.disableProperty().set(false);
        RadioMenuItem itemId = (RadioMenuItem) actionEvent.getSource();
        botTwo.setTacticsPart2(itemId.getId());
        botTwo.setMixedTacticPart2("none");
    }

    public void closeGame() {
        Platform.exit();
    }

    public void newGame() {
        try {
            main.loadScene();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showRules(){
        String rules = "Этап 1. Разделение территории.\n" +
                "Игроки по очереди совершают ходы; начинает первый игрок. Каждый ход представляет собой перемещение игрока в одну из смежных ячеек (по горизонтали или вертикали), при этом ячейка присоединяется к территории данного игрока. Ни одна клетка игрового поля не может быть пройдена дважды (т.е. игрок не может переместиться в клетку, если эта клетка уже посещалась им самим или его соперником). Раунд завершается тогда, когда ни один игрок не может сделать ход.\n" +
                "За каждую занятую ячейку игроку начисляется 3 очка.\n" +
                "\n" +
                "Этап 2. Набор дополнительных очков.\n" +
                "Игроки возобновляют передвижение по полю. Теперь ходить можно в любую смежную клетку кроме той, где в настоящий момент находится соперник. При проходе через ячейку она присоединяется к территории данного игрока. Первым начинает ходить тот игрок, который в 1 раунде остановился раньше. Всего каждый игрок совершает 100 ходов. Очки начисляются по следующим правилам:\n" +
                "– своя клетка – 0 очков;\n" +
                "– нейтральная клетка – 1 очко;\n" +
                "– чужая клетка – 2 очка.\n" +
                "Победителем становится игрок, набравший больше очков.\n";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Правила игры");
        alert.setHeaderText(null);
        alert.setContentText(rules);
        alert.showAndWait();
    }


    public class Tile extends StackPane {
        private int x, y;
        public int valueCell = 0; // 0 нетральная 1 селва вверху, -1 справа внизу
        private Rectangle border = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);
        private Text text = new Text();

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
            if (x == 0 && y == 0) {
                border.setFill(Color.LIGHTGRAY);
                pointP1 = 3;
                point1.setText(String.valueOf(pointP1));
                valueCell = 1;
            }
            if (x == 9 && y == 9) {
                border.setFill(Color.RED);
                pointP2 = 3;
                point2.setText(String.valueOf(pointP2));
                valueCell = -1;
            }
            border.setStroke(Color.LIGHTGRAY);
            getChildren().addAll(border, text);
            setTranslateX(x * TILE_SIZE);
            setTranslateY(y * TILE_SIZE);
            if (whoPlaying.equals("player vs pc")) {
                setOnMouseClicked(event -> {
                    try {
                        PLvsPC(1, -1, botTwo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (whoPlaying.equals("pc vs player")) {
                setOnMouseClicked(event -> {
                    try {
                        PLvsPC(-1, 1, botOne);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (whoPlaying.equals("pc vs pc")) {
                setOnMouseClicked(event -> {
                    try {
                        PCvsPC();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (whoPlaying.equals("player vs player")) {
                setOnMouseClicked(event -> {
                    try {
                        PLvsPL(-1, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

        }

        private void PLvsPL(int i, int i1) throws IOException {
            if (endPart != 2) {
                if (movePlayer) {
                    if (player1.getMove() == 0) {
                        if (checkClick(x, y, player1)) {
                            playerGame(x, y, 1, 1, player1);
                            movePlayer = false;
                            if (player2.getMove() != 0 && player1.getMove() != 0) {
                                fromFirstPartToSecond();
                            }
                        }
                    } else if (player2.getMove() == 0) {
                        if (checkClick(x, y, player2)) {
                            playerGame(x, y, -1, 1, player2);
                            movePlayer = true;
                            if (player2.getMove() != 0) {
                                fromFirstPartToSecond();
                            }
                        }
                    } else {
                        fromFirstPartToSecond();
                    }
                } else {
                    if (player2.getMove() == 0) {
                        if (checkClick(x, y, player2)) {
                            playerGame(x, y, -1, 1, player2);
                            movePlayer = true;
                            if (player2.getMove() != 0 && player1.getMove() != 0) {
                                fromFirstPartToSecond();
                            }
                        }
                    } else if (player1.getMove() == 0) {
                        if (checkClick(x, y, player1)) {
                            playerGame(x, y, 1, 1, player1);
                            movePlayer = false;
                            if (player1.getMove() != 0) {
                                fromFirstPartToSecond();
                            }
                        }
                    } else {
                        fromFirstPartToSecond();
                    }
                }
            } else {
                gamePart2ForPlayers();
            }
        }


        private void PCvsPC() throws IOException {
            while (moveValue != 0) {
                if (moveValue != 0) {
                    if (botOne.getMove() == 1) {
                        botGame(botTwo.getX(), botTwo.getY(), 1, 2, botOne, botTwo.getMove());
                        botGame(botOne.getX(), botOne.getY(), -1, 2, botTwo, botOne.getMove());
                        moveValue--;
                        MovesToTheEnd.setText(String.valueOf(moveValue));
                        if (moveValue == 0) {
                            endGame();
                        }
                    } else {
                        botGame(botOne.getX(), botOne.getY(), -1, 2, botTwo, botOne.getMove());
                        botGame(botTwo.getX(), botTwo.getY(), 1, 2, botOne, botTwo.getMove());
                        moveValue--;
                        MovesToTheEnd.setText(String.valueOf(moveValue));
                        if (moveValue == 0) {
                            endGame();
                        }
                    }
                }
            }
        }

        /**
         *
         */
        public void PLvsPC(int playerPosition, int botPosition, Bot bot) throws IOException {

            if (endPart != 2) {
                if (checkClick(x, y, player1)) {
                    playerGame(x, y, playerPosition, 1, player1);
                    if (player1.getMove() == 0) {
                        botGame(x, y, botPosition, 1, bot, player1.getMove());
                        if (bot.getMove() == 0) {//
                            player1.setMove(checkMove(x, y));
                            if (player1.getMove() != 0) {
                                while (bot.getMove() == 0) {
                                    botGame(x, y, botPosition, 1, bot, player1.getMove());
                                }
                                fromFirstPartToSecond();
                                firstMoveInSecondPart(playerPosition, botPosition, bot);
                            }
                        } else {
                            player1.setMove(checkMove(x, y));
                            if (player1.getMove() != 0) {
                                fromFirstPartToSecond();
                                firstMoveInSecondPart(playerPosition, botPosition, bot);
                            }
                        }
                    } else {
                        while (bot.getMove() == 0) {
                            botGame(x, y, botPosition, 1, bot, player1.getMove());
                        }
                        fromFirstPartToSecond();
                        firstMoveInSecondPart(playerPosition, botPosition, bot);
                    }
                }
            } else {
                gamePart2(playerPosition, botPosition, bot);
            }
        }


        /**
         *
         */
        private void gamePart2(int playerPosition, int botPosition, Bot bot) throws IOException {
            if (moveValue != 0) {
                if (whoPlaying.equals("player vs pc") || whoPlaying.equals("pc vs player")) {
                    if (player1.getMove() == 1) {
                        if (player1.checkPosition(x, y) && (x != bot.getX() || y != bot.getY())) {
                            playerGame(x, y, playerPosition, 2, player1);
                            botGame(x, y, botPosition, 2, bot, player1.getMove());
                            moveValue--;
                            MovesToTheEnd.setText(String.valueOf(moveValue));
                            if (moveValue == 0) {
                                endGame();
                            }
                        }
                    } else {
                        if (player1.checkPosition(x, y) && (x != bot.getX() || y != bot.getY())) {
                            playerGame(x, y, playerPosition, 2, player1);
                            moveValue--;
                            MovesToTheEnd.setText(String.valueOf(moveValue));
                            if (moveValue == 0) {
                                endGame();
                            }
                            if (moveValue != 0) {
                                botGame(x, y, botPosition, 2, bot, player1.getMove());
                            }
                        }
                    }
                }
            }
        }

        private void gamePart2ForPlayers(){

            if (whoPlaying.equals("player vs player")) {
                if (movePlayer) {
                    if (player1.checkPosition(x, y) && (x != player2.getX() || y != player2.getY())) {
                        playerGame(x, y, 1, 2, player1);
                        movePlayer = false;
                        moveForPlayers2Part();
                    }
                } else {
                    if (player2.checkPosition(x, y) && (x != player1.getX() || y != player1.getY())) {
                        playerGame(x, y, -1, 2, player2);
                        movePlayer = true;
                        moveForPlayers2Part();
                    }


                }
            }
        }

        private void moveForPlayers2Part() {
            if (movePart2 != 1) {
                movePart2++;
            } else {
                movePart2 = 0;
                moveValue--;
                MovesToTheEnd.setText(String.valueOf(moveValue));
                if (moveValue == 0) {
                    endGame();
                }
            }
        }

        private void endGame() {
            if (pointP1 > pointP2) {
                showWindow("Игра окончена. Победитель: Игрок 1", "Информация для игроков");
            } else if (pointP1 == pointP2) {
                showWindow("Игра окончена. Ничья", "Информация для игроков");
            } else {
                showWindow("Игра окончена. Победитель: Игрок 2", "Информация для игроков");
            }
        }

        /**
         * Invokes a window with a message about the beginning of the second stage of the game.
         * Also makes visible the number of moves to the end of the game
         */
        public void fromFirstPartToSecond() throws IOException {
            endPart = 2;
            hBoxMoves.visibleProperty().setValue(true);
            gamePart.setText("2");
            MovesToTheEnd.setText(String.valueOf(moveValue));
            showWindow("Закончился первый этап игры. \n Нажмите \"ok\", чтобы продолжить игру", "Информация для игроков");
        }

        /**
         * The first move of the second stage of the game
         */
        private void firstMoveInSecondPart(int playerPosition, int botPosition, Bot bot) throws IOException {
            if (player1.getMove() == 1) {
            } else {
                botGame(x, y, botPosition, 2, bot, player1.getMove());
            }
        }

        /**
         * Calls up a message box
         *
         * @param information message
         */
        public void showWindow(String information, String nameInfo) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(nameInfo);
            alert.setHeaderText(null);
            alert.setContentText(information);
            alert.showAndWait();
        }


        /**
         * Carries out the player's progress
         *
         * @param x
         * @param y
         * @param z Number of the player. 1 - upper left. 2 - bottom right
         */
        private void playerGame(int x, int y, int z, int partGame, Player player) {
            setFill(player.getX(), player.getY(), z);
            player.setCoordinates(x, y);
            if (partGame == 1) {
                grid[x][y].valueCell = z;
                setFillLight(x, y, z, partGame);
                player.setMove(checkMove(x, y));
            } else {
                setFillLight(x, y, z, partGame);
            }

        }

        /**
         * Performs bot call tactics
         *
         * @param x
         * @param y
         * @param z        Number of the player. 1 - upper left. 2 - bottom right
         * @param partGame
         */
        private void botGame(int x, int y, int z, int partGame, Bot bot, int enemyMobility) {
            if (partGame == 1) {
                if (bot.getMove() == 0) {
                    bot.setMove(checkMove(bot.getX(), bot.getY()));
                    if (bot.getMove() == 0) {
                        setFill(bot.getX(), bot.getY(), z);
                        bot.gameBotPart1(x, y, z);
                        bot.setMove(checkMove(bot.getX(),bot.getY()));
                        setFillLight(bot.getX(), bot.getY(), z, partGame);
                        grid[bot.getX()][bot.getY()].valueCell = z;
                    }
                }
            } else {
                setFill(bot.getX(), bot.getY(), z);
                bot.gameBotPart2(x, y, z, grid);
                setFillLight(bot.getX(), bot.getY(), z, partGame);
            }

        }


        /**
         * Paint the cage where the player was and adds points.
         *
         * @param a x coordinates
         * @param s y coordinates
         * @param z Number of the player. 1 - upper left. 2 - bottom right
         */
        private void setFill(int a, int s, int z) {
            if (z == 1) {
                grid[a][s].border.setFill(Color.WHITE);
            } else if (z == -1) {
                grid[a][s].border.setFill(Color.DARKRED);
            }
        }

        /**
         * Makes the cell bright. Current player location.
         *
         * @param a x coordinates
         * @param s y coordinates
         * @param z Number of the player. 1 - upper left. 2 - bottom right.
         */
        private void setFillLight(int a, int s, int z, int partGame) {
            if (partGame == 1) {
                setFillLightPart1(a, s, z);

            } else {
                setFillLightPart2(a, s, z);
            }
        }

        private void setFillLightPart1(int a, int s, int z) {
            if (z == 1) {
                grid[a][s].border.setFill(Color.LIGHTGRAY);
                pointP1 = pointP1 + 3;
                point1.setText(String.valueOf(pointP1));
            } else if (z == -1) {
                grid[a][s].border.setFill(Color.RED);
                pointP2 = pointP2 + 3;
                point2.setText(String.valueOf(pointP2));
            }
        }

        private void setFillLightPart2(int a, int s, int z) {
            if (z == 1) {
                if (grid[a][s].valueCell == 1) {
                    grid[a][s].border.setFill(Color.LIGHTGRAY);
                } else if (grid[a][s].valueCell == -1) {
                    grid[a][s].valueCell = 1;
                    grid[a][s].border.setFill(Color.LIGHTGRAY);
                    pointP1 = pointP1 + 2;
                    point1.setText(String.valueOf(pointP1));
                } else {
                    grid[a][s].valueCell = 1;
                    grid[a][s].border.setFill(Color.LIGHTGRAY);
                    pointP1 = pointP1 + 1;
                    point1.setText(String.valueOf(pointP1));
                }

            } else if (z == -1) {
                if (grid[a][s].valueCell == 1) {
                    grid[a][s].valueCell = -1;
                    grid[a][s].border.setFill(Color.RED);
                    pointP2 = pointP2 + 2;
                    point2.setText(String.valueOf(pointP2));
                } else if (grid[a][s].valueCell == -1) {
                    grid[a][s].border.setFill(Color.RED);
                } else {
                    grid[a][s].valueCell = -1;
                    grid[a][s].border.setFill(Color.RED);
                    pointP2 = pointP2 + 1;
                    point2.setText(String.valueOf(pointP2));
                }
            }
        }

        /**
         * Check the correctness of cell selection.
         *
         * @param x
         * @param y
         * @return true if cell selection is correct,
         * false another.
         */
        private boolean checkClick(int x, int y, Player player) {
            if (player.checkPosition(x, y)) {
                if (grid[x][y].valueCell == 0)
                    return true;
            }
            return false;
        }


        /**
         * Check for player movement.
         *
         * @param x
         * @param y
         * @return 0 if player can move,
         * 1 if he first cant move + endPart = 1,
         * 2 if he second cant move + endPart = 2.
         */
        private int checkMove(int x, int y) {
            int[] ax = new int[]{1, 0, -1, 0};
            int[] ay = new int[]{0, 1, 0, -1};

            for(int k = 0; k < 4; ++k) {
                int ix = x + ax[k];
                int iy = y + ay[k];
                if(Coordinate.isBorder(ix, iy, grid.length) && grid[ix][iy].valueCell == 0) {
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

    }
}