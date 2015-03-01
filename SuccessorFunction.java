import java.util.ArrayList;

/**
 * Created by Kevin on 2015-02-28.
 */
public class SuccessorFunction {
    public ArrayList<BoardLogic> getSuccessors(BoardLogic state) {
        ArrayList<BoardLogic> successors = new ArrayList<BoardLogic>();
        Queen[] queens = state.getFriendly();

//        For each queen
            for(int i=0; i<queens.length; i++) {
                ArrayList<moveData> curQueenMoves = new ArrayList<moveData>();
                curQueenMoves = state.getQueenMoves(queens[i]);
                System.out.println("\nSF: Evaluating queen: "+i);

//                For each possible queen move
                for(int j=0; j<curQueenMoves.size(); j ++) {
                    System.out.println("SF: Evaluating state: "+j+" for queen: "+i);

//                    Make copy of board
                    BoardLogic newBoard = state.deepCopy();
//                    Perform move
                    Queen[] newBoardQueens = newBoard.getFriendly();
                    System.out.println("SF: Moving queen: " + i + " to " + curQueenMoves.get(j).colPos + ", " + curQueenMoves.get(j).rowPos);
                    newBoardQueens[i].move(curQueenMoves.get(j).colPos, curQueenMoves.get(j).rowPos);
//                    Update board
                    newBoard.updateAfterMove();

//                    For each possible arrow shot
                    for(int k=0; k<newBoard.getArrowShots(newBoardQueens[i]).size(); k++) {
                        System.out.println("SF: Evaluating arrow: "+k+" for state: "+j+" for queen "+i);

//                        Add shot to board
                        System.out.println("SF: THROWING ARROW to: "+ newBoard.getArrowShots(newBoardQueens[i]).get(k).colPos+", "+newBoard.getArrowShots(newBoardQueens[i]).get(k).rowPos);
                        newBoard.addArrow(newBoard.getArrowShots(newBoardQueens[i]).get(k).colPos,newBoard.getArrowShots(newBoardQueens[i]).get(k).rowPos);
//                        Update board
                        newBoard.updateAfterMove();
//                        Add copied board with move made to successors
                        successors.add(newBoard);
                    }
                }
            }
        return successors;
    }
}
