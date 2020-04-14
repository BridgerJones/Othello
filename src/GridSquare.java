import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GridSquare extends Rectangle{
    int row;
    int col;
    public GridSquare(int row, int col){
        this.setWidth(100);
        this.setHeight(100);
        this.setFill(Paint.valueOf("#24820e"));
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);
        this.row = row;
        this.col = col;

//        this.setOnMouseClicked(event -> {
//            int printRow = this.getCord()[0];
//            int printCol = this.getCord()[1];
//            System.out.printf("%d, %d\n", printRow, printCol);
//
//        });
    }

    public int[] getCord(){
        return new int[] {this.row, this.col};
    }


}