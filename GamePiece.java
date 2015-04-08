/**
 * GamePiece class that is an object representation of queens and arrows on a game board
 * @author TCulos
 *
 */
public class GamePiece {
	//all possible pieces that will be on the board
	protected int rowPos;
	protected int colPos;
    protected boolean isQueen;
    protected boolean isOpponent;
	
	/**
	 * sets our pieces
	 * @param x row position
	 * @param y column position
	 */
	public GamePiece(int y, int x){
		this.colPos= x;
		this.rowPos = y;
	}
	
	/**
	 * @return the column position of a game piece
	 */
	public int getColumnPos(){
		return colPos;
	}
	
	/**
	 * @return the row position of a game piece
	 */
	public int getRowPos(){
		return rowPos;
	}

    /**
     *used to draw game board
     */
	public int getConcatPos() {
		return Integer.parseInt(Integer.toString(colPos) + Integer.toString(rowPos));
	}
}