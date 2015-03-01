/**
 * GamePiece class that is an object representation of queens and arrows on a game board
 * @author TCulos
 *
 */
public class GamePiece {
	//all possible pieces that will be on the board
	private int rowPos;
	private int colPos;
	
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
	
	public int getConcatPos() {
		System.out.println(Integer.parseInt(Integer.toString(colPos) + Integer.toString(rowPos)));
		return Integer.parseInt(Integer.toString(colPos) + Integer.toString(rowPos));
	}
}
