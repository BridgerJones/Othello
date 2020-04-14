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



    // backgrounds
    Background diskLayerBackground = new Background(new BackgroundFill(Paint.valueOf("#ab1bf3"), null, null));

    // numerical representation of the game logic
    int[][] gameLogic = {
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
        GridPane diskLayer = createDiskLayer();

        gameCanvas.getChildren().add(diskLayer);
        updateCanvas(gameLogic, diskLayer);


        updateValidMoves(gameLogic);
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
    public static GridPane createDiskLayer(){

        GridPane gameBoard = new GridPane();

        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                GridSquare temp = new GridSquare(row, col);
                temp.setOnMouseClicked(event ->{
                    int getRow = temp.getCord()[0];
                    int getCol = temp.getCord()[1];
                    System.out.printf("%d, %d\n", getRow, getCol);

                });
                temp.setFill(Color.TRANSPARENT);
                temp.setStroke(Color.TRANSPARENT);
                gameBoard.add(temp, row, col);
            }
        }

        return gameBoard;
    }

    public static void updateCanvas(int[][] gameLogic, GridPane diskLayer){


        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                if (gameLogic[row][col] == B){
                    diskLayer.add(new Disk(false), row, col);
                }
                else if (gameLogic[row][col] == W){
                    diskLayer.add(new Disk(true), row, col);
                }
            }
        }
    }

    public static void updateValidMoves(int[][] gameLogic){
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                //black disk logic
                if (gameLogic[row][col] == B){
                    int rowOffSet = 1;
                    int columnOffSet = 1;
                    //check N
                    if (gameLogic[row - rowOffSet][col] == W){
                        checkN(gameLogic, row, col);
                    }

                    //check NE

                    //check E

                    //check SE

                    //check S

                    //check SW

                    //check W

                    //check NW

                }
                //white disk logic

            }
        }
    }

    private static void checkN(int[][] gameLogic, int row, int col){
        int rowOffSet = 1;
        int colOffSet = 1;

        while(gameLogic[row - rowOffSet][col] == W){
            rowOffSet++;
        }
        if (gameLogic[row - rowOffSet][col] == N){
            gameLogic[row - rowOffSet][col] = validBlack;
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
    }
}