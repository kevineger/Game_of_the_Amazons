/**
 * queen piece for the game board
 */
public class Queen extends GamePiece {
	
	protected Queen(int y, int x, boolean b) {
		super(y, x);
        this.isQueen=true;
        isOpponent = b;
	}

    /**
     * moves a queen from move data to point (x,y)
     * @param x column number(0-9)
     * @param y row number (0-9)
     */
    protected void move(int y , int x){
        this.colPos =x;
        this.rowPos =y;
    }

    /**
     * creates a copy of the queen that is not just a reference
     * @return
     */
    protected Queen clone() {
        Queen qNew = new Queen( rowPos, colPos, isOpponent);
        return qNew;
    }

    public String toString(){
        String str;
        if(isOpponent)
            str = "Enemy Queen at row " + rowPos + " column " + colPos;
        else
            str = "Our Queen at row " + rowPos + " column " + colPos;

        return str;
    }
}