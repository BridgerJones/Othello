import javafx.scene.Scene;
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



        //add all children
        mainMenu.getChildren().addAll(othelloLogo);

        Scene mainMenuScene = new Scene(mainMenu, 1000, 815);

        return mainMenuScene;
    }
}