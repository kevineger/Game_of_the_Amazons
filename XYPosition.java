/**
 * helper object to create a list of XYPositions 
 * @author TCulos
 *
 */
public class XYPosition {
	int colPos;
	int rowPos;
	GamePiece Q;
	
	
	/**
	 * creates and xy position object in terms of row/column position
	 * @param x column position of a piece
	 * @param y row position of a piece
	 */
	public XYPosition(int x, int y, GamePiece Q){
		colPos = x;
		rowPos= y;
		this.Q = Q;
	}
	
	public String toString(){
		String str = "GamePiece:"+Q.toString()+"\ncol: "+colPos+", row: "+rowPos;
		return str;
	}

}
