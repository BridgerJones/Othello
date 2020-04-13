import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application{

    public void start(Stage stage){
        Pane main = new Pane();
        Circle circle = new Circle();
        circle.setRadius(25);
        circle.setCenterX(400);
        circle.setCenterY(400);
        main.getChildren().add(circle);

        Scene mainScene = new Scene(main, 800, 800);
        stage.setScene(mainScene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}