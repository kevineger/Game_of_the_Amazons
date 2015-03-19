import java.util.ArrayList;

public class BoardLogic {

    private boolean enemyHasMove;
	protected GamePiece[][] board = new GamePiece[10][10];
	private  Queen[] enemies;
	private  Queen[] friendly;
    protected  ArrayList<Arrow> arrows;
	private  ArrayList<moveData> legalArrowShots;
	private  ArrayList<moveData> legalQueenMoves;

//    GameBoard frame = null;
    Board frame = null;

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
//        frame = new Board();
//        frame.setVisible(true);

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
        frame = new Board();
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
        updateAfterMove();
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
     * returns the list of all arrows on the board
     */
    protected ArrayList<Arrow> getArrows() {return arrows;}

    protected BoardLogic deepCopy() {
        Queen[] newFriendly = new Queen[4];
        Queen[] newEnemies = new Queen[4];
        ArrayList<Arrow> newArrows = new ArrayList<Arrow>();
        for(int i=0; i<newEnemies.length; i++) {
            newFriendly[i]= friendly[i].clone();
            newEnemies[i]= enemies[i].clone();
        }
        if(arrows!=null) {
            for(Arrow a : arrows) {
                if(a != null)
                    newArrows.add(new Arrow(a.getRowPos(),a.getColumnPos()));
            }
        }

        BoardLogic newBoard = new BoardLogic(newEnemies, newFriendly, newArrows);
        return newBoard;
    }

    /**
     * @param G
     * @return list of all legal moves from game piece
     */

    protected ArrayList<moveData> getLegalMoves(GamePiece G) {
        ArrayList<moveData> legalMoves = new ArrayList<>();

        int curRow = G.getRowPos();
        int curCol = G.getColumnPos();

//        Legal Moves Left
        for(int i = 1; curCol-i>=0; i++) {
            if(board[curRow][curCol-i]==null) {
                legalMoves.add(new moveData(curRow,curCol-i,G));
            }
            else break;
        }

//        Legal Moves Diagonal Left/Up
        for(int i = 1; curRow-i>=0&&curCol-i>=0; i++) {
            if(board[curRow-i][curCol-i]==null) {
                legalMoves.add(new moveData(curRow-i,curCol-i,G));
            }
            else break;
        }

//        Legal Moves Up
        for(int i = 1; curRow-i>=0; i++) {
            if(board[curRow-i][curCol]==null) {
                legalMoves.add(new moveData(curRow-i,curCol,G));
            }
            else break;
        }

//        Legal Moves Diagonal Right/Up
        for(int i = 1; curRow-i>=0&&curCol+i<=9; i++) {
            if(board[curRow-i][curCol+i]==null) {
                legalMoves.add(new moveData(curRow-i,curCol+i,G));
            }
            else break;
        }

//        Legal Moves Right
        for(int i = 1; curCol+i<=9; i++) {
            if(board[curRow][curCol+i]==null) {
                legalMoves.add(new moveData(curRow,curCol+i,G));
            }
            else break;
        }

//        Legal Moves Diagonal Right/Down
        for(int i = 1; curRow+i<=9&&curCol+i<=9; i++) {
            if(board[curRow+i][curCol+i]==null) {
                legalMoves.add(new moveData(curRow+i,curCol+i,G));
            }
            else break;
        }

//        Legal Moves Down
        for(int i = 1; curRow+i<=9; i++) {
            if(board[curRow+i][curCol]==null) {
                legalMoves.add(new moveData(curRow+i,curCol,G));
            }
            else break;
        }

//        Legal Moves Diagonal Left/Down
        for(int i = 1; curRow+i<=9&&curCol-i>=0; i++) {
            if(board[curRow+i][curCol-i]==null) {
                legalMoves.add(new moveData(curRow+i,curCol-i,G));
            }
            else break;
        }
        return legalMoves;
    }

	/**
	 * @param G GamePiece to inspect
	 * @return list of legal moves of piece G
	 */
	protected ArrayList<moveData> getQueenMoves(GamePiece G) {
		return getLegalMoves(G);
    }

    /**
     * @param G
     * @return list of legal arrow shots from queen G
     */
    protected ArrayList<Arrow> getArrowShots(GamePiece G) {
        ArrayList<Arrow> legalShots = new ArrayList<>();

        int curRow = G.getRowPos();
        int curCol = G.getColumnPos();

//        Legal Moves Left
        for(int i = 1; curCol-i>=0; i++) {
            if(board[curRow][curCol-i]==null) {
                legalShots.add(new Arrow(curRow, curCol-i));
            }
            else break;
        }

//        Legal Moves Diagonal Left/Up
        for(int i = 1; curRow-i>=0&&curCol-i>=0; i++) {
            if(board[curRow-i][curCol-i]==null) {
                legalShots.add(new Arrow(curRow-i,curCol-i));
            }
            else break;
        }

//        Legal Moves Up
        for(int i = 1; curRow-i>=0; i++) {
            if(board[curRow-i][curCol]==null) {
                legalShots.add(new Arrow(curRow-i,curCol));
            }
            else break;
        }

//        Legal Moves Diagonal Right/Up
        for(int i = 1; curRow-i>=0&&curCol+i<=9; i++) {
            if(board[curRow-i][curCol+i]==null) {
                legalShots.add(new Arrow(curRow-i,curCol+i));
            }
            else break;
        }

//        Legal Moves Right
        for(int i = 1; curCol+i<=9; i++) {
            if(board[curRow][curCol+i]==null) {
                legalShots.add(new Arrow(curRow,curCol+i));
            }
            else break;
        }

//        Legal Moves Diagonal Right/Down
        for(int i = 1; curRow+i<=9&&curCol+i<=9; i++) {
            if(board[curRow+i][curCol+i]==null) {
                legalShots.add(new Arrow(curRow+i,curCol+i));
            }
            else break;
        }

//        Legal Moves Down
        for(int i = 1; curRow+i<=9; i++) {
            if(board[curRow+i][curCol]==null) {
                legalShots.add(new Arrow(curRow+i,curCol));
            }
            else break;
        }

//        Legal Moves Diagonal Left/Down
        for(int i = 1; curRow+i<=9&&curCol-i>=0; i++) {
            if(board[curRow+i][curCol-i]==null) {
                legalShots.add(new Arrow(curRow+i,curCol-i));
            }
            else break;
        }
        return legalShots;
    }

    /**
     * checks to see if enemy has any queen moves on the board
     */
    private void setEnemyHasMove(){
        System.out.println("BL Enemies: " + enemies.length);
        for (Queen q : enemies){
            int startRow = q.getRowPos();
            int startCol = q.getColumnPos();

            if(startRow - 1 >= 0 & board[startRow - 1][startCol] == null){
                enemyHasMove = true;
                break;
            }

            if(startRow + 1 <= 9 & board[startRow + 1][startCol] == null){
                enemyHasMove = true;
                break;
            }

            if(startCol - 1 >= 0 & board[startRow][startCol - 1] == null){
                enemyHasMove = true;
                break;
            }
            if(startCol + 1 <= 9 & board[startRow][startCol + 1] == null){
                enemyHasMove = true;
                break;
            }

            if((startRow - 1 >= 0 && startCol - 1 >= 0) & board[startRow - 1][startCol - 1] == null){
                enemyHasMove = true;
                break;
            }

            if((startRow + 1 <= 9 && startCol - 1 >= 0) & board[startRow + 1][startCol - 1] == null){
                enemyHasMove = true;
                break;
            }

            if((startRow + 1 <= 9 && startCol + 1 <= 9) & board[startRow + 1][startCol + 1] == null){
                enemyHasMove = true;
                break;
            }
            if((startRow - 1 >= 0 && startCol + 1 <= 9) & board[startRow - 1][startCol + 1] == null){
                enemyHasMove = true;
                break;
            }
        }
    }

	/**
	 * returns list of all legal moves available for every piece
	 * @return ArrayList<XYPosition> of all moves available
	 */
	protected  ArrayList<moveData> getAllLegalQueenMoves(){
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
			legalArrowShots.addAll(getQueenMoves(G));
		}
	}

	/**
	 * method used to update where we can shoot after an amazon is moved or arrow shot
	 */
	public void updateLegalQueenMoves(){
		legalQueenMoves.clear();
		for(GamePiece G : friendly){
			legalQueenMoves.addAll(getQueenMoves(G));
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
    protected void addArrow(int y, int x){
        arrows.add(new Arrow(y,x));
        updateAfterMove();
    }

    /**
     * after a move is done updates the board (action being queen move + arrow Shot)
     */
    protected void updateAfterMove(){
        clearBoard();

        //resetting each friendly queen
        for (Queen q : friendly) {
            board[q.getRowPos()][q.getColumnPos()] = q;
        }

        //resetting each enemy queen
        for(Queen q : enemies){
            board[q.getRowPos()][q.getColumnPos()] = q;
        }

        //resetting each arrow
        for(Arrow a : arrows){
            board[a.getRowPos()][a.getColumnPos()] = a;
        }
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

    /**
     * repaints the gui
     */
    protected void updateBoard(move queenMove, moveData arrowShot, boolean isEnemy) {
        frame.update(queenMove, arrowShot, isEnemy);
    }

    /**
     *
     * @return true == someone won
     * @return false == still playing
     */
    protected boolean goalTest(){
        if(enemyHasMove == false || legalQueenMoves.size() == 0)
            return true;
        else
            return false;
    }


	@Override
    public String toString(){
//		String str = "Current board:\n";
//		if(board != null){
//			for(int i = 0; i <= 9; i++){
//				str+= "\n";
//				for(int j = 0; j <= 9; j++){
//					str += String.format("%-11s", board[i][j]);
//				}
//				str += "\n";
//			}
//		}else
//			str += "is null";
//
//		return str;
        String s = "\n";
        String line = "\no--- --- --- --- --- --- --- --- --- ---o";
        for(int i=0; i<10; i++) {
            s+=line+"\n";
            for(int j=0; j<10; j++) {
                s+="| ";
                if(board[i][j]==null) s+="  ";
                else if(board[i][j].isQueen){
                    if(board[i][j].isOpponent) s+="B ";
                    else s+="W ";
                }
                else s+="a ";
            }
            s+="|";
        }
        s+=line;
        return s;
	}
}