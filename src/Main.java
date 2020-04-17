import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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



    // backgrounds
    Background diskLayerBackground = new Background(new BackgroundFill(Paint.valueOf("#ab1bf3"), null, null));

    // numerical representation of the game logic
    public int[][] gameLogic = {
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, W, B, N, N, N, },
            { N, N, N, B, W, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
    };



    public void start(Stage stage){

        Pane gameCanvas = new Pane();
        // create instance of the game board
        GridPane gameBoard = createGameBoard();
        gameCanvas.getChildren().add(gameBoard);
        // create instance of the disk layer
        GridPane diskLayer = createDiskLayer(gameLogic);
        diskLayer.setOnMouseClicked(event ->{
            updateCanvas(gameLogic, diskLayer);
        });

        gameCanvas.getChildren().add(diskLayer);
        updateCanvas(gameLogic, diskLayer);



        logGameLogicState(gameLogic);


//        Pane rightHUD = new Pane();
//        rightHUD.setMaxWidth(185);
//        GridPane rightHudSections = new GridPane();
//        Rectangle test = new Rectangle();
//        test.setHeight(815);
//        test.setWidth(185);
//        test.setFill(Color.valueOf("#b56f05"));


//        rightHudSections.add(test, 0, 1);
//        rightHUD.getChildren().add(rightHudSections);
//
//        gameCanvas.setRight(test);



        // initialization of stage
        Scene mainScene = new Scene(gameCanvas, 1000, 815);
        stage.setScene(mainScene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static GridPane createGameBoard(){

        GridPane gameBoard = new GridPane();

        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                gameBoard.add(new GridSquare(row, col), row, col);
            }
        }

        return gameBoard;
    }
    public static GridPane createDiskLayer(int[][]gameLogic){

        GridPane gameBoard = new GridPane();

        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
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

    public static void refreshCanvas(int[][]gameLogic){
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                if (gameLogic[row][col] == validBlack || gameLogic[row][col] == validWhite){
                    gameLogic[row][col] = N;
                }
            }
        }
    }



    public static void updateCanvas(int[][] gameLogic, GridPane diskLayer){


        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                if (gameLogic[row][col] == B){

                    diskLayer.add(new Disk(false), col, row);
                }
                else if (gameLogic[row][col] == W){
                    diskLayer.add(new Disk(true), col, row);
                }
            }
        }
    }

    public static void updateValidMoves(int[][] gameLogic){
        try{
            for (int row = 0; row < 8; row++){
                for (int col = 0; col < 8; col++){
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

        }
    }

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

    public static void logGameLogicState(int[][] gameLogic){
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                if (col == 7){
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