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
            ArrayList<moveData> curQueenMoves = new ArrayList<>();
//                Make a deep copy of all queen moves at specific queen
            for(moveData md : state.getQueenMoves(queens[i])) {
                curQueenMoves.add(md);
//                System.out.println("Queen: "+i+" has move "+md.rowPos+"(row), "+md.colPos+"(col)");
            }

//                For each possible queen move
            for(int j=0; j<curQueenMoves.size(); j ++) {

                BoardLogic placeHolderBoard = state.deepCopy();
                ArrayList<Arrow> numshots = new ArrayList<>();

                Queen[] placeholderBoardQueens = placeHolderBoard.getFriendly();
                placeholderBoardQueens[i].move(curQueenMoves.get(j).rowPos, curQueenMoves.get(j).colPos);
//                    Update board
                placeHolderBoard.updateAfterMove();

                numshots = placeHolderBoard.getArrowShots(placeHolderBoard.getFriendly()[i]);

                //for each arrow shot of a queen
                for(int k=0; k<numshots.size(); k++) {

                    BoardLogic newBoard = state.deepCopy();
//                    Perform move
                    Queen[] newBoardQueens = newBoard.getFriendly();
                    newBoardQueens[i].move(curQueenMoves.get(j).rowPos, curQueenMoves.get(j).colPos);
//                    Update board
                    newBoard.updateAfterMove();

//                    For each possible arrow shot
                    ArrayList<Arrow> legalArrowShots = new ArrayList<>();
                    legalArrowShots = newBoard.getArrowShots(newBoardQueens[i]);
//                        Add shot to board
//                    System.out.println("The legal arrow shot is: "+legalArrowShots.get(k).rowPos+"(row), "+legalArrowShots.get(k).colPos+"(col)");
                    newBoard.addArrow(legalArrowShots.get(k).rowPos, legalArrowShots.get(k).colPos);

//                        PRINT THE STATE
//                    System.out.println("\nQueen "+i+" at ("+queens[i].getRowPos()+", "+queens[i].getColumnPos()+") moving to ("+ curQueenMoves.get(j).rowPos + ", " + curQueenMoves.get(j).colPos+")");
//                    System.out.println("State Generated: Arrow Throw #"+k+"\nQueen: " + i + " || Location: (" + curQueenMoves.get(j).rowPos + ", " + curQueenMoves.get(j).colPos + ") || Arrow: (" + legalArrowShots.get(k).rowPos + ", " +legalArrowShots.get(k).colPos+")");


//                        Update board
                    newBoard.updateAfterMove();

//                        Add copied board with move made to successors
                    stateCount ++;
                    successors.add(newBoard);
                    int size = newBoard.arrows.size();
                    newBoard.arrows.set(size-1,null);
                }
            }
        }
        System.out.println("Total States: " + stateCount);
        return successors;
    }
}
