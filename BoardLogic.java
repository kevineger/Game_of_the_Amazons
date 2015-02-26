import java.util.ArrayList;



/**
 * KNOWN PROBLEMS:	1) for legal shots it holds all shots possible not just the ones for the piece we moved
 * 					2) not sure whether to hold legal shots list in GamePiece or not(I think probably)
 * 					3) When we move a queen does that queen have to shoot? or can any?
 * 					4) ...
 * 
 * WANT TO ADD:		1) method to move a queen given its legal move(XYPosition object holds which queen )
 * 					2) method to keep track of moves available to our opponent(want to use this in a heuristic
 * 					   where we weigh a move based on how many moves it gives us VS. restricts their moves)
 * 					3) ...
 */

/**
 * game board class made of GamePieces, to be called for each game of Amazons
 * played includes helper methods for state-space search as well as helper data
 * structures for searching for the enemy/yourself
 * 
 * GameBoard is made of GamePieces, null if open position
 * 
 * @author TCulos
 *
 */
public class BoardLogic {
	private GamePiece[][] board;
	private static Queen[] enemies;						// variables are declared as static here so that when we update
	private static Queen[] friendly;					// the legal moves or a queen is moved we don't have a method call for
	private static ArrayList<XYPosition> legalArrowShots;	// that outside this class its all called whenever an action occurs
	private static ArrayList<XYPosition> legalQueenMoves;	// PS: legal move lists created on instantiation

	GameBoard frame = null;
	/**
	 * creates a board depending on whether we are the starting player or not
	 * with top left corner being coordinate (0,0) and bottom right (9,9)
	 * 
	 * @param starttrue if we are first to move
	 */
	protected BoardLogic(boolean start) {
		if (start) {
			board = new GamePiece[][] {
					{ null, null, null, new Queen(0, 3, true), null, null, new Queen(0, 6, true), null, null, null },
					{ null, null, null, null, null, null, null, null, null, null },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ new Queen(3, 0, true), null, null, null, null, null, null, null, null, new Queen(3, 9, true) },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ new Queen(6, 0, false), null, null, null, null, null, null, null, null, new Queen(6, 9, false) },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ null, null, null, new Queen(9, 3, false), null, null, new Queen(9, 6, false), null, null, null } };

			// enemies is an array of the opponents queens while friendly is our own queens
			enemies = new Queen[] { (Queen) board[0][3], (Queen) board[0][6], (Queen) board[3][0], (Queen) board[3][9] };
			friendly = new Queen[] { (Queen) board[6][0], (Queen) board[6][9], (Queen) board[9][3],	(Queen) board[9][6] };
		} else {
			board = new GamePiece[][] {
					{ null, null, null, new Queen(0, 3, false), null, null, new Queen(0, 6, false), null, null, null },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ new Queen(3, 0, false), null, null, null, null, null, null, null, null, new Queen(3, 9, false) },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ new Queen(6, 0, true), null, null, null, null, null, null, null, null, new Queen(6, 9, true) },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ null, null, null, null, null, null, null, null, null,	null },
					{ null, null, null, new Queen(9, 3, true), null, null,	new Queen(9, 6, true), null, null, null } };

			friendly = new Queen[] { (Queen) board[0][3], (Queen) board[0][6], (Queen) board[3][0], (Queen) board[3][9] };
			enemies = new Queen[] { (Queen) board[6][0], (Queen) board[6][9], (Queen) board[9][3],	(Queen) board[9][6] };
		}
		
//		Start GUI
		frame = new GameBoard(friendly, enemies);
    	frame.pack();
    	frame.setResizable(false);
    	frame.setLocationRelativeTo( null );
    	frame.setVisible(true);

		legalArrowShots = new ArrayList<XYPosition>();
		legalQueenMoves = new ArrayList<XYPosition>();
		updateLegalQueenMoves();
		updateLegalArrowShots();

	}

	/**
	 * sets a board that is equal to another board to be used in successor
	 * function when we get there
	 * 
	 * @param newBoard the board state we wish to create
	 */
	protected BoardLogic(GamePiece[][] newBoard) {
		this.board = newBoard;

		legalArrowShots = new ArrayList<XYPosition>();
		legalQueenMoves = new ArrayList<XYPosition>();
		updateLegalQueenMoves();
		updateLegalArrowShots();
	}

	/**
	 * returns the list of all opponent pieces on the board
	 * 
	 * @return array of enemy GamePieces(have position variables)
	 */
	protected Queen[] getEnemies() {
		return enemies;
	}

	/**
	 * returns the list of all your pieces on the board
	 * 
	 * @return array of friendly GamePieces(have position variables)
	 */
	protected Queen[] getFriendly() {
		return friendly;
	}

	/**
	 * returns a list of XYPosition objects of legal moves for a given gamePiece G
	 * 
	 * @param G GamePiece to inspect
	 * @return list of legal moves of piece G
	 */
	private ArrayList<XYPosition> isLegalMove(GamePiece G) {
		// array list of positions to be returned
		ArrayList<XYPosition> legal = new ArrayList<XYPosition>();

		// starting position to check axis' if legal move
		int startRow = G.getRowPos();
		int startCol = G.getColumnPos();

		// get all legal moves from queen position going upwards
		for (int i = 1; startRow - i >= 0; i++) {
			if (board[startRow - i][startCol] == null)
				legal.add(new XYPosition(startRow - i, startCol, G));
			else
				break;
		}

		// get all legal moves from queen position going downwards
		for (int i = 1; startRow + i <= 9; i++) {
			if (board[startRow + i][startCol] == null)
				legal.add(new XYPosition(startCol, startRow + i, G));
			else
				break;
		}

		//getting all legal moves to the right of the queen
		for (int i = 1; startCol + i <= 9; i++) {
			if (board[startRow][startCol+i] == null)
				legal.add(new XYPosition(startCol + i, startRow, G));
			else
				break;
		}

		//getting all legal moves to the right of the queen
		for (int i = 1; startCol - i >= 0; i++) {
			if (board[startRow][startCol-i] == null)
				legal.add(new XYPosition(startCol - i, startRow, G));
			else
				break;
		}

		//get all legal moves down and to the right(diagonal) of the queen
		for (int i = 1; (startCol + i <= 9) && (startRow + i <= 9); i++) {
			if (board[startRow+i][startCol+i] == null){
				legal.add(new XYPosition(startCol + i, startRow + i, G));
			}else
				break;
		}

		//get all legal moves up and to the right(diagonal) of the queen
		for (int i = 1; (startRow - i >= 0) && (startCol + i <= 9); i++) {
			if (board[startRow - i][startCol+i] == null){
				legal.add(new XYPosition(startCol + i, startRow - i, G));
			}else
				break;
		}

		//get all legal moves down and to the left(diagonal) of queen
		for (int i = 1; (startRow + i <= 9) && (startCol - i >= 0); i++) {
			if (board[startRow + i][startCol - i] == null){
				legal.add(new XYPosition(startCol - i, startRow + i, G));
			}else
				break;
		}

		//get all legal moves up and to the left(diagonal) of queen
		for (int i = 1; (startRow - i >= 0) && (startCol - i >= 0); i++) {
			if (board[startRow - i][startCol - i] == null){
				legal.add(new XYPosition(startCol - i, startRow - i, G));
			}else
				break;
		}

		return legal;
	}

	/**
	 * returns list of all legal moves available for every piece
	 * @return ArrayList<XYPosition> of all moves available
	 */
	protected  ArrayList<XYPosition> getLegalQueenMoves(){
		return legalQueenMoves;
	}

	/**
	 * returns list of all legal moves available for every piece
	 * @return ArrayList<XYPosition> of all moves available
	 */
	protected  ArrayList<XYPosition> getLegalArrowShots(){
		return legalArrowShots;
	}

	/**
	 * method used to update where we can shoot after an amazon is moved or arrow shot
	 */
	private void updateLegalArrowShots(){
		legalArrowShots.clear();
		for(GamePiece G : friendly){
			legalArrowShots.addAll(isLegalMove(G));
		}
	}

	/**
	 * method used to update where we can shoot after an amazon is moved or arrow shot
	 */
	private void updateLegalQueenMoves(){
		legalQueenMoves.clear();
		for(GamePiece G : friendly){
			legalQueenMoves.addAll(isLegalMove(G));
		}
	}

	/**
	 * adds an arrow to the board
	 * @param columPos 
	 * @param rowPos
	 * @return boolean true if position null, false otherwise
	 */
//	protected boolean shootArrow(int columPos, int rowPos){
//		if(board[rowPos][columPos] == null){
//			board[rowPos][columPos] = new GamePiece(columPos, rowPos, false, true);
//			updateLegalQueenMoves();
//			updateLegalArrowShots();
//			return true;
//		}else{
//			System.out.println("Not a legal move");
//			return false;}
//	}

	public String toString(){
		String str = "Current board:\n";
		if(board != null){
			for(int i = 0; i <= 9; i++){
				str+= "\n";
				for(int j = 0; j <= 9; j++){
					str += String.format("%-11s", board[i][j]);
				}
				str += "\n";
			}
		}else
			str += "is null";

		return str;
	}
}