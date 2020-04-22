import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class GameMedia{
    String filename = "MainTheme.mp3";
    public GameMedia(){
        //default
    }
    public MediaPlayer createMediaPlayer(){
        Media mainTheme = new Media(new File(filename).toURI().toString());

        return new MediaPlayer(mainTheme);
    }
}