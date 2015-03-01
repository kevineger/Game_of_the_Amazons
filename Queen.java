/**
 * queen piece for the game board
 */
public class Queen extends GamePiece {
	
	boolean isOpponent;
	
	protected Queen(int x, int y, boolean b) {
		super(x, y);
		isOpponent = b;
	}

    /**
     * moves a queen from move data to point (x,y)
     * @param x column number(0-9)
     * @param y row number (0-9)
     */
    protected void move(int x , int y){
        this.colPos =x;
        this.rowPos =y;
    }

    public String toString(){
        String str;
        if(isOpponent)
            str = "Enemy Queen";
        else
            str = "Our Queen";

        return str;
    }
}
