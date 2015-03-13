/**
 * @author Kevin
 *
 */
public class move {
    int newColPos;
    int newRowPos;
    int oldColPos;
    int oldRowPos;
    GamePiece Q;

    /**
     * creates and xy position object in terms of row/column position
     * @param x column position of a piece
     * @param y row position of a piece
     */
    public move(int x, int y, GamePiece Q){
        newColPos = x;
        newRowPos= y;
        oldColPos = Q.colPos;
        oldRowPos = Q.rowPos;
        this.Q = Q;
    }

    public String toString(){
        String str = "GamePiece: "+Q.toString()+" at: "+oldColPos+", "+oldRowPos+" moving to: "+newColPos+", "+newRowPos;
        return str;
    }

}
