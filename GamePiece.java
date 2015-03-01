/**
 * GamePiece class that is an object representation of queens and arrows on a game board
 * @author TCulos
 *
 */
public class GamePiece {
	//all possible pieces that will be on the board
	protected int rowPos;
	protected int colPos;
	
	/**
	 * sets our pieces
	 * @param columnPos 
	 * @param rowPos 
	 */
	public GamePiece(int columnPos, int rowPos){
		this.colPos= columnPos;
		this.rowPos = rowPos;
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
