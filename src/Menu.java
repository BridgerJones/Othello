import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.Key;

public class Menu{

    Stage nextStage;
    Scene nextScene;

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

        //start animation
        Timeline startAnimation = new Timeline(new KeyFrame(Duration.millis(250), e ->{
            if (start.getImage().getUrl().equals("file:/Users/Bridger/Desktop/Othello/out/production/Othello/Start.png")){
                start.setImage(new Image("StartOnHover.png"));
            }
            else {
                start.setImage(new Image("Start.png"));
            }
        }));
        startAnimation.setCycleCount(Timeline.INDEFINITE);
        start.setOnMouseEntered(e ->{
            startAnimation.play();
            System.out.println(start.getImage().getUrl());
        });
        start.setOnMouseExited(e ->{
            startAnimation.stop();
            start.setImage(new Image("Start.png"));
        });
        start.setOnMouseClicked(event ->{
            nextStage.setScene(nextScene);
            nextStage.show();
        });





        //add all children
        mainMenu.getChildren().addAll(othelloLogo, start);

        Scene mainMenuScene = new Scene(mainMenu, 1000, 815);

        return mainMenuScene;
    }
    public void setStartTarget(Stage stage, Scene scene){
        this.nextStage = stage;
        this.nextScene = scene;
    }

}