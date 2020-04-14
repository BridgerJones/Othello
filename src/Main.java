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
    private final int N = 0;
    // Black Disk state
    private final int B = 1;
    // white Disk state
    private final int W = 2;

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
                temp.setFill(Color.TRANSPARENT);
                temp.setStroke(Color.TRANSPARENT);
                gameBoard.add(temp, row, col);
            }
        }

        return gameBoard;
    }


}