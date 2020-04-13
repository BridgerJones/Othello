import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
        GridPane gameBoard = createGameBoard();



        Scene mainScene = new Scene(gameBoard, 1000, 1000);
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