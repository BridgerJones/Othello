import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.regex.*;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

public class Main extends Application{
    // Neutral Space
    static final int N = 0;
    // Black Disk state
    static final int B = 1;
    // white Disk state
    static final int W = 2;
    //valid move black
    static final int validBlack = 3;
    //valid move white
    static final int validWhite = 4;

    static boolean isWhiteTurn = false;

    //player one stats
    static int blackTotal = 0;
    static IntegerProperty blackListener = new SimpleIntegerProperty();


    //player two stats
    static int whiteTotal = 0;



    // backgrounds
    Background diskLayerBackground = new Background(new BackgroundFill(Paint.valueOf("#c27817"), null, null));

    // numerical representation of the game logic
    public int[][] gameLogic = {
            { N,     N, N, N, N, N, N, N, N,     N, },

            { N,     N, N, N, N, N, N, N, N,     N, },
            { N,     N, N, N, N, N, N, N, N,     N, },
            { N,     N, N, N, N, N, N, N, N,     N, },
            { N,     N, N, N, W, B, N, N, N,     N, },
            { N,     N, N, N, B, W, N, N, N,     N, },
            { N,     N, N, N, N, N, N, N, N,     N, },
            { N,     N, N, N, N, N, N, N, N,     N, },
            { N,     N, N, N, N, N, N, N, N,     N, },

            { N,     N, N, N, N, N, N, N, N,     N, },

    };



    public void start(Stage stage){

        Pane gameCanvas = new Pane();
        // create instance of the game board
        GridPane gameBoard = createGameBoard();
        gameCanvas.getChildren().add(gameBoard);
        // create instance of the disk layer
        GridPane diskLayer = createDiskLayer(gameLogic);


        gameCanvas.getChildren().add(diskLayer);
        updateCanvas(gameLogic, diskLayer);


        //log beginning state
        logGameLogicState(gameLogic);

        //hud
        Pane leftHud = new Pane();
        leftHud.setTranslateX(816);
        leftHud.setBackground(diskLayerBackground);

        //hud components
        VBox components = new VBox();
        components.setMinWidth(200);
        components.setMinHeight(1000);
        //Host Mod
        components.getChildren().add(new Label("Host Game"));
        Button hostGame = new Button("Host");

        components.getChildren().add(hostGame);
        //Join Module
        components.getChildren().add(new Label("Join a Game: Enter IP address"));
        TextField ipField = new TextField();
        ipField.setMaxWidth(150);
        Button joinGame = new Button("Join");
        components.getChildren().addAll(ipField, joinGame);
        // Player 1 Stats
        Label section = new Label("--------------------");
        Label playerOne = new Label("Player 1 Stats");
        Label blackDisks = new Label("Black Disk Total: " + blackTotal);
        Label section2 = new Label("--------------------");

        components.getChildren().addAll(section,playerOne, blackDisks, section2);

        // Player 2 Stats
        Label playerTwo = new Label("Player 2 Stats");
        Label whiteDisks = new Label("White Disk Total: " + whiteTotal);
        Label section3 = new Label("--------------------");

        components.getChildren().addAll(playerTwo, whiteDisks, section3);
        //add components

        // add hud to the gameCanvas
        leftHud.getChildren().add(components);
        gameCanvas.getChildren().add(leftHud);

        //initalize game scene
        Scene mainScene = new Scene(gameCanvas, 1000, 815);

        // main menu
        Menu mainMenu = new Menu();
        Scene mainMenuScene = mainMenu.createMainMenu();

        mainMenu.setStartTarget(stage, mainScene);


        // initialization of stage
        stage.setScene(mainMenuScene);
        stage.setResizable(false);
        stage.show();
        //play main theme
        mainMenu.playerLoc.play();

        //disk layer lambdas
        diskLayer.setOnMouseClicked(event ->{
            updateCanvas(gameLogic, diskLayer);
            blackDisks.setText("Black Disk Total: " + blackTotal);
            whiteDisks.setText("White Disk Total: " + whiteTotal);
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @author bridger
     * @return returns a gameboard
     */
    public static GridPane createGameBoard(){

        GridPane gameBoard = new GridPane();

        for(int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                gameBoard.add(new GridSquare(row, col), row, col);
            }
        }

        return gameBoard;
    }

    /**
     * @author bridger
     * @param gameLogic
     * @return this returns a disk layer filled with empty sprites to enforce the size of the grid.
     * this also is what holds the disk sprites as they are added on top.
     *
     */
    public static GridPane createDiskLayer(int[][]gameLogic){

        GridPane gameBoard = new GridPane();

        for(int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                GridSquare temp = new GridSquare(row, col);
                temp.setOnMouseEntered(event ->{
                    int getCol = temp.getCord()[0];
                    int getRow = temp.getCord()[1];

                    refreshCanvas(gameLogic);
                    updateValidMoves(gameLogic);
                    if (isWhiteTurn){
                        if (gameLogic[getRow][getCol] == validWhite){
                            temp.setFill(Color.WHITE);
                        }
                    }

                    else {
                        if (gameLogic[getRow][getCol] == validBlack){
                            temp.setFill(Color.BLACK);
                        }
                    }




                });
                temp.setOnMouseExited(event ->{
                    temp.setFill(Color.TRANSPARENT);
                });
                temp.setOnMouseClicked(event ->{
                    int getRow = temp.getCord()[1];
                    int getCol = temp.getCord()[0];
                    int rowOffSet = 1;
                    int colOffSet = 1;

                    if (isWhiteTurn){
                        if (gameLogic[getRow][getCol] == validWhite){
                            gameLogic[getRow][getCol] = W;

                            //riposte
                            riposte(gameLogic, isWhiteTurn, getRow, getCol);

                            isWhiteTurn = false;
                        }
                    }
                    else {
                        if(gameLogic[getRow][getCol] == validBlack){
                            gameLogic[getRow][getCol] = B;
                            //riposte
                            riposte(gameLogic, isWhiteTurn, getRow, getCol);
                            isWhiteTurn = true;
                        }
                    }
                    logGameLogicState(gameLogic);
                });


                //default settings
                temp.setFill(Color.TRANSPARENT);
                temp.setStroke(Color.TRANSPARENT);
                gameBoard.add(temp, row, col);
            }
        }

        return gameBoard;
    }

    /**
     * @author bridger
     * @param gameLogic
     * @param isWhiteTurn
     * @param row
     * @param col
     * this method is in charge of making sure that sandwiched cells are changed to the current players disk.
     * I named it riposte as a reference to the sword fighting technique in Dark Souls that when done against you usually
     * ends in choice words and death. LOL. This is because often in Othello you will think you are winning only to have your opponent make the next move
     * and totally destroy you and win the game. Especially common towards end game.
     */
    public static void riposte(int[][] gameLogic, boolean isWhiteTurn, int row, int col){

        int rowOffSet = 1;
        int colOffSet = 1;

        if (isWhiteTurn){
            //check n
            if (gameLogic[row - rowOffSet][col] == B){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row - rowOffSet][col] == B){
                    validRowCord.add(row - rowOffSet);
                    validColCord.add(col);
                    rowOffSet++;
                    if (gameLogic[row - rowOffSet][col] == W){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][col] = W;
                        }
                    }
                }
            }

            rowOffSet = 1;
            //check ne
            if (gameLogic[row - rowOffSet][col + colOffSet] == B){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row - rowOffSet][col + colOffSet] == B){


                    validRowCord.add(row - rowOffSet);
                    validColCord.add(col + colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row - rowOffSet][col + colOffSet] == W){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = W;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            // check e
            if (gameLogic[row][col + colOffSet] == B){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row][col + colOffSet] == B){


                    validRowCord.add(row);
                    validColCord.add(col + colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row][col + colOffSet] == W){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = W;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            // check se
            if (gameLogic[row + rowOffSet][col + colOffSet] == B){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row + rowOffSet][col + colOffSet] == B){


                    validRowCord.add(row + rowOffSet);
                    validColCord.add(col + colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row + rowOffSet][col + colOffSet] == W){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = W;
                        }
                    }
                }
            }

            rowOffSet  = 1;
            colOffSet = 1;
            //check s
            if (gameLogic[row + rowOffSet][col] == B){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row + rowOffSet][col] == B){


                    validRowCord.add(row + rowOffSet);
                    validColCord.add(col);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row + rowOffSet][col] == W){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = W;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            //check sw
            if (gameLogic[row + rowOffSet][col - colOffSet] == B) {
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row + rowOffSet][col - colOffSet] == B){


                    validRowCord.add(row + rowOffSet);
                    validColCord.add(col - colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row + rowOffSet][col - colOffSet] == W){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = W;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            //check w
            if (gameLogic[row][col - colOffSet] == B) {
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row][col - colOffSet] == B){


                    validRowCord.add(row);
                    validColCord.add(col - colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row][col - colOffSet] == W){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = W;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            // check nw
            if (gameLogic[row - rowOffSet][col - colOffSet] == B) {
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row - rowOffSet][col - colOffSet] == B){
                    validRowCord.add(row - rowOffSet);
                    validColCord.add(col - colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row - rowOffSet][col - colOffSet] == W){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = W;
                        }
                    }
                }
            }
        }

        //black disk turn
        else{
            //check n
            if (gameLogic[row - rowOffSet][col] == W){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row - rowOffSet][col] == W){
                    validRowCord.add(row - rowOffSet);
                    validColCord.add(col);
                    rowOffSet++;
                    if (gameLogic[row - rowOffSet][col] == B){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][col] = B;
                        }
                    }
                }
            }

            rowOffSet = 1;
            //check ne
            if (gameLogic[row - rowOffSet][col + colOffSet] == W){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row - rowOffSet][col + colOffSet] == W){


                    validRowCord.add(row - rowOffSet);
                    validColCord.add(col + colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row - rowOffSet][col + colOffSet] == B){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = B;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            // check e
            if (gameLogic[row][col + colOffSet] == W){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row][col + colOffSet] == W){


                    validRowCord.add(row);
                    validColCord.add(col + colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row][col + colOffSet] == B){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = B;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            // check se
            if (gameLogic[row + rowOffSet][col + colOffSet] == W){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row + rowOffSet][col + colOffSet] == W){


                    validRowCord.add(row + rowOffSet);
                    validColCord.add(col + colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row + rowOffSet][col + colOffSet] == B){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = B;
                        }
                    }
                }
            }

            rowOffSet  = 1;
            colOffSet = 1;
            //check s
            if (gameLogic[row + rowOffSet][col] == W){
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row + rowOffSet][col] == W){


                    validRowCord.add(row + rowOffSet);
                    validColCord.add(col);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row + rowOffSet][col] == B){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = B;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            //check sw
            if (gameLogic[row + rowOffSet][col - colOffSet] == W) {
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row + rowOffSet][col - colOffSet] == W){


                    validRowCord.add(row + rowOffSet);
                    validColCord.add(col - colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row + rowOffSet][col - colOffSet] == B){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = B;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            //check w
            if (gameLogic[row][col - colOffSet] == W) {
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row][col - colOffSet] == W){


                    validRowCord.add(row);
                    validColCord.add(col - colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row][col - colOffSet] == B){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = B;
                        }
                    }
                }
            }

            rowOffSet = 1;
            colOffSet = 1;
            // check nw
            if (gameLogic[row - rowOffSet][col - colOffSet] == W) {
                ArrayList<Integer> validRowCord = new ArrayList<>();
                ArrayList<Integer> validColCord = new ArrayList<>();
                while(gameLogic[row - rowOffSet][col - colOffSet] == W){
                    validRowCord.add(row - rowOffSet);
                    validColCord.add(col - colOffSet);
                    rowOffSet++;
                    colOffSet++;
                    if (gameLogic[row - rowOffSet][col - colOffSet] == B){
                        for (int i = 0; i < validRowCord.size(); i++){
                            gameLogic[validRowCord.get(i)][validColCord.get(i)] = B;
                        }
                    }
                }
            }

        }
    }

    /**
     * @author bridger
     * @param gameLogic
     * refreshes the numerical representation after a move to make sure no old valid moves are left behind
     */
    public static void refreshCanvas(int[][]gameLogic){
        for (int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                if (gameLogic[row][col] == validBlack || gameLogic[row][col] == validWhite){
                    gameLogic[row][col] = N;
                }
            }
        }
    }



    public static void updateCanvas(int[][] gameLogic, GridPane diskLayer){
        int blackCounter = 0;
        int whiteCounter = 0;

        for (int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                if (gameLogic[row][col] == B){
                    blackCounter++;

                    diskLayer.add(new Disk(false), col, row);
                }
                else if (gameLogic[row][col] == W){
                    diskLayer.add(new Disk(true), col, row);
                    whiteCounter++;
                }
            }
        }
        blackTotal = blackCounter;
        whiteTotal = whiteCounter;
    }

    /**
     * @author bridger
     * @param gameLogic
     * updates the valid moves on the numerical representation
     * depends on the private methods that check the different directions
     */

    public static void updateValidMoves(int[][] gameLogic){
        try{
            for (int row = 1; row < 9; row++){
                for (int col = 1; col < 9; col++){
                    if (isWhiteTurn){
                        //white disk logic
                        if (gameLogic[row][col] == W){
                            int rowOffSet = 1;
                            int columnOffSet = 1;
                            //check N
                            if (gameLogic[row - rowOffSet][col] == B){
                                checkN(gameLogic, row, col, true);
                            }
                            //check NE
                            if (gameLogic[row - rowOffSet][col + columnOffSet] == B){
                                checkNE(gameLogic, row, col, true);
                            }
                            //check E
                            if (gameLogic[row][col + columnOffSet] == B){
                                checkE(gameLogic, row, col, true);
                            }
                            //check SE
                            if (gameLogic[row + rowOffSet][col + columnOffSet] == B){
                                checkSE(gameLogic, row, col, true);
                            }
                            //check S
                            if (gameLogic[row + rowOffSet][col] == B){
                                checkS(gameLogic, row, col, true);
                            }
                            //check SW
                            if (gameLogic[row + rowOffSet][col - columnOffSet] == B){
                                checkSW(gameLogic, row, col, true);
                            }
                            //check W
                            if (gameLogic[row][col - columnOffSet] == B){
                                checkW(gameLogic, row, col, true);
                            }
                            //check NW
                            if (gameLogic[row - rowOffSet][col - columnOffSet] == B){
                                checkNW(gameLogic, row, col, true);
                            }
                        }
                    }
                    else {
                        // black disk logic
                        if (gameLogic[row][col] == B){
                            int rowOffSet = 1;
                            int columnOffSet = 1;
                            //check N
                            if (gameLogic[row - rowOffSet][col] == W){
                                checkN(gameLogic, row, col, false);
                            }
                            //check NE
                            if (gameLogic[row - rowOffSet][col + columnOffSet] == W){
                                checkNE(gameLogic, row, col, false);
                            }
                            //check E
                            if (gameLogic[row][col + columnOffSet] == W){
                                checkE(gameLogic, row, col, false);
                            }
                            //check SE
                            if (gameLogic[row + rowOffSet][col + columnOffSet] == W){
                                checkSE(gameLogic, row, col, false);
                            }
                            //check S
                            if (gameLogic[row + rowOffSet][col] == W){
                                checkS(gameLogic, row, col, false);
                            }
                            //check SW
                            if (gameLogic[row + rowOffSet][col - columnOffSet] == W){
                                checkSW(gameLogic, row, col, false);
                            }
                            //check W
                            if (gameLogic[row][col - columnOffSet] == W){
                                checkW(gameLogic, row, col, false);
                            }
                            //check NW
                            if (gameLogic[row - rowOffSet][col - columnOffSet] == W){
                                checkNW(gameLogic, row, col, false);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ignore){
            System.out.println("Out of Bounds Error");
        }
    }

    //All of these private methods check the number grid and update valid moves
    private static void checkN(int[][] gameLogic, int row, int col, boolean isWhiteTurn){
        int rowOffSet = 1;
        int colOffSet = 1;

        if (isWhiteTurn){
            while(gameLogic[row - rowOffSet][col] == B){
                rowOffSet++;
            }
            if (gameLogic[row - rowOffSet][col] == N){
                gameLogic[row - rowOffSet][col] = validWhite;
            }
        }
        else {
            while(gameLogic[row - rowOffSet][col] == W){
                rowOffSet++;
            }
            if (gameLogic[row - rowOffSet][col] == N){
                gameLogic[row - rowOffSet][col] = validBlack;
//            for (int i = 0; i <= rowOffSet; i++){
//                gameLogic[row - i][col] = B;
//            }
            }
        }
    }

    private static void checkS(int[][] gameLogic, int row, int col, boolean isWhiteTurn){
        int rowOffSet = 1;
        int colOffSet = 1;

        if (isWhiteTurn){
            while(gameLogic[row + rowOffSet][col] == B){
                rowOffSet++;
            }
            if (gameLogic[row + rowOffSet][col] == N){
                gameLogic[row + rowOffSet][col] = validWhite;
            }
        }
        else {
            while(gameLogic[row + rowOffSet][col] == W){
                rowOffSet++;
            }
            if (gameLogic[row + rowOffSet][col] == N){
                gameLogic[row + rowOffSet][col] = validBlack;
            }
        }
    }

    private static void checkE(int[][] gameLogic, int row, int col, boolean isWhiteTurn){
        int rowOffSet = 1;
        int colOffSet = 1;

        if (isWhiteTurn){
            while(gameLogic[row][col + colOffSet] == B){
                colOffSet++;
            }
            if (gameLogic[row][col + colOffSet] == N){
                gameLogic[row][col + colOffSet] = validWhite;
//            for (int i = 0; i <= colOffSet; i++){
//                gameLogic[row][col + i] = B;
//            }
            }
        }
        else{
            while(gameLogic[row][col + colOffSet] == W){
                colOffSet++;
            }
            if (gameLogic[row][col + colOffSet] == N){
                gameLogic[row][col + colOffSet] = validBlack;
//            for (int i = 0; i <= colOffSet; i++){
//                gameLogic[row][col + i] = B;
//            }
            }
        }

    }

    private static void checkW(int[][] gameLogic, int row, int col, boolean isWhiteTurn){
        int rowOffSet = 1;
        int colOffSet = 1;

        if (isWhiteTurn){
            while (gameLogic[row][col - colOffSet] == B){
                colOffSet++;
            }
            if (gameLogic[row][col - colOffSet] == N){
                gameLogic[row][col - colOffSet] = validWhite;
            }
        }
        else {
            while (gameLogic[row][col - colOffSet] == W){
                colOffSet++;
            }
            if (gameLogic[row][col - colOffSet] == N){
                gameLogic[row][col - colOffSet] = validBlack;
            }
        }

    }

    private static void checkNE(int[][] gameLogic, int row, int col, boolean isWhiteTurn){
        int rowOffSet = 1;
        int colOffSet = 1;

        if (isWhiteTurn){
            while (gameLogic[row - rowOffSet][col + colOffSet] == B){
                rowOffSet++;
                colOffSet++;
            }
            if (gameLogic[row - rowOffSet][col + colOffSet] == N){
                gameLogic[row - rowOffSet][col + colOffSet] = validWhite;
            }
        }
        else {
            while (gameLogic[row - rowOffSet][col + colOffSet] == W){
                rowOffSet++;
                colOffSet++;
            }
            if (gameLogic[row - rowOffSet][col + colOffSet] == N){
                gameLogic[row - rowOffSet][col + colOffSet] = validBlack;
            }
        }

    }
    private static void checkSE(int[][] gameLogic, int row, int col, boolean isWhiteTurn){
        int rowOffSet = 1;
        int colOffSet = 1;

        if (isWhiteTurn){
            while (gameLogic[row + rowOffSet][col + colOffSet] == B){
                rowOffSet++;
                colOffSet++;
            }
            if (gameLogic[row + rowOffSet][col + colOffSet] == N){
                gameLogic[row + rowOffSet][col + colOffSet] = validWhite;
            }
        }
        else {
            while (gameLogic[row + rowOffSet][col + colOffSet] == W){
                rowOffSet++;
                colOffSet++;
            }
            if (gameLogic[row + rowOffSet][col + colOffSet] == N){
                gameLogic[row + rowOffSet][col + colOffSet] = validBlack;
            }
        }
    }

    private static void checkSW(int[][] gameLogic, int row, int col, boolean isWhiteTurn){
        int rowOffSet = 1;
        int colOffSet = 1;

        if (isWhiteTurn){
            while (gameLogic[row + rowOffSet][col - colOffSet] == B){
                rowOffSet++;
                colOffSet++;
            }
            if (gameLogic[row + rowOffSet][col - colOffSet] == N){
                gameLogic[row + rowOffSet][col - colOffSet] = validWhite;
            }
        }
        else {
            while (gameLogic[row + rowOffSet][col - colOffSet] == W){
                rowOffSet++;
                colOffSet++;
            }
            if (gameLogic[row + rowOffSet][col - colOffSet] == N){
                gameLogic[row + rowOffSet][col - colOffSet] = validBlack;
            }
        }

    }

    private static void checkNW(int[][] gameLogic, int row, int col, boolean isWhiteTurn){
        int rowOffSet = 1;
        int colOffSet = 1;

        if (isWhiteTurn){
            while (gameLogic[row - rowOffSet][col - colOffSet] == B){
                rowOffSet++;
                colOffSet++;
            }
            if (gameLogic[row - rowOffSet][col - colOffSet] == N){
                gameLogic[row - rowOffSet][col - colOffSet] = validWhite;
            }
        }
        else {
            while (gameLogic[row - rowOffSet][col - colOffSet] == W){
                rowOffSet++;
                colOffSet++;
            }
            if (gameLogic[row - rowOffSet][col - colOffSet] == N){
                gameLogic[row - rowOffSet][col - colOffSet] = validBlack;
            }
        }

    }
    //end of check valid move dependancies

    /**
     * @author bridger
     * @param gameLogic
     * logs the numerical representation of the game
     */
    public static void logGameLogicState(int[][] gameLogic){
        for(int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                if (col == 8){
                    System.out.printf("%d,\n", gameLogic[row][col]);

                }
                else {
                    System.out.printf("%d, ", gameLogic[row][col]);
                }
            }
        }
        System.out.println("\n\n");;
    }


}