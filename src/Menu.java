import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.security.Key;

public class Menu{

    Stage nextStage;
    Scene nextScene;
    MediaPlayer playerLoc;

    public Menu(){

    }
    public Scene createMainMenu(){
        Image image = new Image("MainMenuBackground.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, null);
        Background background = new Background(backgroundImage);
        Pane mainMenu = new Pane();
        mainMenu.setBackground(background);

        //setup media player
        GameMedia mediaObject = new GameMedia();
        MediaPlayer player = mediaObject.createMediaPlayer();
        this.playerLoc = player;
        MediaView playerView = new MediaView(player);
        BooleanProperty isPlaying = new SimpleBooleanProperty();
        isPlaying.setValue(true);
        isPlaying.addListener(ov ->{
            this.playerLoc.stop();
        });

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
        });
        start.setOnMouseExited(e ->{
            startAnimation.stop();
            start.setImage(new Image("Start.png"));
        });
        start.setOnMouseClicked(event ->{
            nextStage.setScene(nextScene);
            nextStage.show();
            isPlaying.setValue(false);
        });




        //add all children
        mainMenu.getChildren().addAll(othelloLogo, start, playerView);

        Scene mainMenuScene = new Scene(mainMenu, 1000, 815);

        return mainMenuScene;
    }
    public void setStartTarget(Stage stage, Scene scene){
        this.nextStage = stage;
        this.nextScene = scene;
    }
    public void playTheme(){
        this.playerLoc.play();
    }

}