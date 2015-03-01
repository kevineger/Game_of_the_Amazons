import java.util.ArrayList;

/**
 * KNOWN PROBLEMS:	1) ...
 *
 * 
 * WANT TO ADD:		1) method to keep track of moves available to our opponent(want to use this in a heuristic
 * 					   where we weigh a move based on how many moves it gives us VS. restricts their moves)
 * 					2) ...
 */

/**
 * game board class made of GamePieces, to be called for each game of Amazons
 * played includes helper methods for state-space search as well as helper data
 * structures for searching for the enemy/yourself
 * 
 * boardLogic is made of GamePieces, null if open position on board
 * 
 * @author TCulos
 *
 */
public class BoardLogic {
	private GamePiece[][] board;
	private  Queen[] enemies;
	private  Queen[] friendly;
    private  ArrayList<Arrow> arrows;
	private  ArrayList<moveData> legalArrowShots;
	private  ArrayList<moveData> legalQueenMoves;

	GameBoard frame = null;
	/**
	 * creates a board depending on whether we are the starting player or not
	 * with top left corner being coordinate (0,0) and bottom right (9,9)
	 * 
	 * @param start true if we are first to move
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

        arrows = new ArrayList<>();
        legalArrowShots = new ArrayList<>();
		legalQueenMoves = new ArrayList<>();
		updateLegalQueenMoves();

	}

	/**
	 * sets a board that is equal to another board to be used in successor
	 * function when we get there
	 * 
	 * @param newBoard the board state we wish to create
	 */
	protected BoardLogic(GamePiece[][] newBoard) {
		this.board = newBoard;

//      Start GUI
        frame = new GameBoard(friendly, enemies);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);


        legalArrowShots = new ArrayList<moveData>();
		legalQueenMoves = new ArrayList<moveData>();
		updateLegalQueenMoves();
	}

    /**
     * constructor takes board pieces and builds logic for it
     * @param enemies opponent queen positions
     * @param friendly our queen positions
     * @param arrow stone positions
     */
    protected BoardLogic(Queen[] enemies, Queen[] friendly, ArrayList<Arrow> arrow){
        this.enemies = enemies;
        this.friendly = friendly;
        this.arrows = arrow;
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
	 * returns a list of legal move information objects of legal moves for a given gamePiece G
	 * 
	 * @param G GamePiece to inspect
	 * @return list of legal moves of piece G
	 */
	private ArrayList<moveData> isLegalMove(GamePiece G) {
		// array list of positions to be returned
		ArrayList<moveData> legal = new ArrayList<moveData>();

		// starting position to check axis' if legal move
		int startRow = G.getRowPos();
		int startCol = G.getColumnPos();

		// get all legal moves from queen position going upwards
		for (int i = 1; startRow - i >= 0; i++) {
			if (board[startRow - i][startCol] == null)
				legal.add(new moveData(startRow - i, startCol, G));
			else
				break;
		}

		// get all legal moves from queen position going downwards
		for (int i = 1; startRow + i <= 9; i++) {
			if (board[startRow + i][startCol] == null)
				legal.add(new moveData(startCol, startRow + i, G));
			else
				break;
		}

		//getting all legal moves to the right of the queen
		for (int i = 1; startCol + i <= 9; i++) {
			if (board[startRow][startCol+i] == null)
				legal.add(new moveData(startCol + i, startRow, G));
			else
				break;
		}

		//getting all legal moves to the right of the queen
		for (int i = 1; startCol - i >= 0; i++) {
			if (board[startRow][startCol-i] == null)
				legal.add(new moveData(startCol - i, startRow, G));
			else
				break;
		}

		//get all legal moves down and to the right(diagonal) of the queen
		for (int i = 1; (startCol + i <= 9) && (startRow + i <= 9); i++) {
			if (board[startRow+i][startCol+i] == null){
				legal.add(new moveData(startCol + i, startRow + i, G));
			}else
				break;
		}

		//get all legal moves up and to the right(diagonal) of the queen
		for (int i = 1; (startRow - i >= 0) && (startCol + i <= 9); i++) {
			if (board[startRow - i][startCol+i] == null){
				legal.add(new moveData(startCol + i, startRow - i, G));
			}else
				break;
		}

		//get all legal moves down and to the left(diagonal) of queen
		for (int i = 1; (startRow + i <= 9) && (startCol - i >= 0); i++) {
			if (board[startRow + i][startCol - i] == null){
				legal.add(new moveData(startCol - i, startRow + i, G));
			}else
				break;
		}

		//get all legal moves up and to the left(diagonal) of queen
		for (int i = 1; (startRow - i >= 0) && (startCol - i >= 0); i++) {
			if (board[startRow - i][startCol - i] == null){
				legal.add(new moveData(startCol - i, startRow - i, G));
			}else
				break;
		}

		return legal;
	}

	/**
	 * returns list of all legal moves available for every piece
	 * @return ArrayList<XYPosition> of all moves available
	 */
	protected  ArrayList<moveData> getLegalQueenMoves(){
		return legalQueenMoves;
	}

	/**
	 * returns list of all legal moves available for every piece
	 * @return ArrayList<XYPosition> of all moves available
	 */
	protected  ArrayList<moveData> getLegalArrowShots(){
		return legalArrowShots;
	}

	/**
	 * method used to update where we can shoot after an amazon is moved or arrow shot
	 */
	private void updateLegalArrowShots(){
        if(legalArrowShots != null)
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
     * adds an arrow to the board and updates board
     * @param a arrow to be added
     */
    protected void addArrow(Arrow a){
        arrows.add(a);
        updateAfterMove();
    }

    /**
     * adds an arrow to the board at the given position
     * and then updates the board
     * @param x co-ordinate
     * @param y co-ordinate
     */
    protected void addArrow(int x, int y){
        arrows.add(new Arrow(x,y));
        updateAfterMove();
    }

    /**
     * after a move is done updates the board (action being queen move + arrow Shot)
     */
    protected void updateAfterMove(){
        this.clearBoard();

        //reseting each friendly queen
        for (Queen q : friendly) {
            board[q.getRowPos()][q.getColumnPos()] = q;
        }

        //reseting each enemy queen
        for(Queen q : enemies){
            board[q.getRowPos()][q.getColumnPos()] = q;
        }

        //reseting each arrow
        for(Arrow a : arrows){
            board[a.getRowPos()][a.getColumnPos()] = a;
        }

        //re-making GUI
        frame = new GameBoard(friendly, enemies);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);

    }

    /**
     * makes all positions on a board null so positions can be updated
     */
    private void clearBoard(){
        for (int i = 0; i <= 9 ; i++) {
            for (int j = 0; j <= 9 ; j++) {
                board[i][j] = null;
            }
        }
    }

	@Override
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