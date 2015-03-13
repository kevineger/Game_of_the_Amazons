import java.util.ArrayList;

/**
 * Created by Kevin on 2015-02-28.
 */
public class SuccessorFunction {
    public ArrayList<BoardLogic> getSuccessors(BoardLogic state) {
        ArrayList<BoardLogic> successors = new ArrayList<BoardLogic>();
        Queen[] queens = state.getFriendly();

        int stateCount = 0;

//        For each queen
            for(int i=0; i<queens.length; i++) {
                ArrayList<moveData> curQueenMoves = new ArrayList<moveData>();
//                Make a deep copy of all queen moves at specific queen
                for(moveData md : state.getQueenMoves(queens[i])) {
                    curQueenMoves.add(md);
                }

//                For each possible queen move
                for(int j=0; j<curQueenMoves.size(); j ++) {

//                    Make copy of board
                    BoardLogic newBoard = state.deepCopy();
//                    Perform move
                    Queen[] newBoardQueens = newBoard.getFriendly();
                    newBoardQueens[i].move(curQueenMoves.get(j).colPos, curQueenMoves.get(j).rowPos);
//                    Update board
                    newBoard.updateAfterMove();

//                    For each possible arrow shot
                    ArrayList<moveData> legalArrowShots = new ArrayList<>();
                    legalArrowShots = newBoard.getArrowShots(newBoardQueens[i]);
                    for(int k=0; k<legalArrowShots.size(); k++) {

//                        Add shot to board
                        newBoard.addArrow(legalArrowShots.get(k).colPos,legalArrowShots.get(k).rowPos);

//                        PRINT THE STATE
                        System.out.println("\nQueen "+i+" at ("+queens[i].getRowPos()+", "+queens[i].getColumnPos()+") moving to ("+ curQueenMoves.get(j).rowPos + ", " + curQueenMoves.get(j).colPos+")");
                        System.out.println("State Generated: Arrow Throw #"+k+"\nQueen: " + i + " || Location: (" + curQueenMoves.get(j).rowPos + ", " + curQueenMoves.get(j).colPos + ") || Arrow: (" + legalArrowShots.get(k).rowPos + ", " +legalArrowShots.get(k).colPos+")");

//                        Update board
                        newBoard.updateAfterMove();

//                        Add copied board with move made to successors
                        stateCount ++;
                        successors.add(newBoard);
                    }
                }
            }
        System.out.println("Total States: " + stateCount);
        return successors;
    }
}
