/**
 * arrow object to shoot after a queen is moved
 *
 * Created by TCulos on 2015-02-26.
 */
public class Arrow extends GamePiece {

    public Arrow(int y, int x){
        super(y,x);
    }

    protected Arrow clone() {
        Arrow aNew = new Arrow(rowPos, colPos);
        return aNew;
    }

    public String toString(){
//        return "\nRow: "+rowPos+", Col: "+colPos;
        return "Arrow";
    }

}
