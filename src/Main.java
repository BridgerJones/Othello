import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

    // numerical representation of the game logic
    int[][] gameLogic = {
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },
            { N, N, N, N, N, N, N, N, },

    };

    public void start(Stage stage){
        BorderPane gameCanvas = new BorderPane();
        // create instance of the game board
        GridPane gameBoard = createGameBoard();
        gameCanvas.setLeft(gameBoard);

        Pane rightHUD = new Pane();
        rightHUD.setMaxWidth(185);
        GridPane rightHudSections = new GridPane();
        Rectangle test = new Rectangle();
        test.setHeight(815);
        test.setWidth(185);
        test.setFill(Color.valueOf("#b56f05"));


        rightHudSections.add(test, 0, 1);
        rightHUD.getChildren().add(rightHudSections);

        gameCanvas.setRight(test);



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

        for(int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                gameBoard.add(new GridSquare(), row, col);
            }
        }

        return gameBoard;
    }


}