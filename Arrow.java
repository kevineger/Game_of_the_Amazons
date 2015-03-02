/**
 * arrow object to shoot after a queen is moved
 *
 * Created by TCulos on 2015-02-26.
 */
public class Arrow extends GamePiece {

    public Arrow(int x, int y){
        super(x,y);
    }

    protected Arrow clone() {
        Arrow aNew = new Arrow(colPos, rowPos);
        return aNew;
    }

    public String toString(){
        return "Arrow";
    }

}
