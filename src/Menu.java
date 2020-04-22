import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;

public class Menu{


    public Menu(){

    }
    public Scene createMainMenu(){
        Image image = new Image("MainMenuBackground.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);
        Background background = new Background(backgroundImage);
        Pane mainMenu = new Pane();
        mainMenu.setBackground(background);

        //main Logo
        ImageView othelloLogo = new ImageView("Othello.png");
        othelloLogo.xProperty().bind(mainMenu.widthProperty().multiply(0.25));
        othelloLogo.yProperty().bind(mainMenu.heightProperty().multiply(0.15));
        // start button
        ImageView start = new ImageView("Start.png");
        start.xProperty().bind(mainMenu.widthProperty().multiply(0.35));
        start.yProperty().bind(mainMenu.heightProperty().multiply(0.50));
        start.setOnMouseEntered(e ->{
            start.setImage(new Image("StartOnHover.png"));
        });
        start.setOnMouseExited(e ->{
            start.setImage(new Image("Start.png"));
        });




        //add all children
        mainMenu.getChildren().addAll(othelloLogo, start);

        Scene mainMenuScene = new Scene(mainMenu, 1000, 815);

        return mainMenuScene;
    }
}