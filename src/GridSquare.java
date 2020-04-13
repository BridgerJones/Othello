import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GridSquare extends Rectangle{
    public GridSquare(){
        this.setWidth(100);
        this.setHeight(100);
        this.setFill(Paint.valueOf("#24820e"));
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);
    }
}