import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Disk extends ImageView {
    boolean isBlack = false;
    boolean isWhite = false;

    public Disk(boolean isWhite){
        this.setFitHeight(100);
        this.setFitWidth(100);
        if (isWhite){
            this.setImage(new Image("WhiteDisk.png"));
        }
        else{
            this.setImage(new Image("BlackDisk.png"));

        }


    }


}