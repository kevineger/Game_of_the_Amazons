import java.util.ArrayList;

/**
 * Created by Kevin on 2015-02-28.
 */
public class SuccessorFunction2 {

    public int count=0;

    public ArrayList<BoardLogic> getSuccessors(BoardLogic state) {
        ArrayList<BoardLogic> successors = new ArrayList<BoardLogic>();
        Queen[] queens = state.getFriendly();
        int stateCount = 0;

        for(int i=0; i<queens.length; i++) {
            ArrayList<moveData> queenMoves = new ArrayList<>();
            // Deep copy of queen moves
            for(moveData md : state.getQueenMoves(queens[i])) {
                queenMoves.add(md);
            }
            // For each queen move
            for(int j=0; j<queenMoves.size(); j++) {
                // Temp board with moved queen
                BoardLogic tempBoard = state.deepCopy();
                // System.out.println("Deep Copy is: \n"+tempBoard.toString());
                tempBoard.friendly[i].rowPos = queenMoves.get(j).rowPos;
                tempBoard.friendly[i].colPos = queenMoves.get(j).colPos;
                tempBoard.updateAfterMove();

                // For each arrow shot at that locations
                ArrayList<Arrow> arrowShots = new ArrayList<>();
                // Add legal arrow shots from given position
                for(Arrow a : tempBoard.getArrowShots(queenMoves.get(j).rowPos,queenMoves.get(j).colPos)) {
                    arrowShots.add(a.clone());
                }
                for(int k=0; k<arrowShots.size(); k++) {
                    BoardLogic newState = tempBoard.deepCopy();
                    newState.addArrow(arrowShots.get(k));
                    newState.updateAfterMove();
                    stateCount++;
                    successors.add(newState);
                    count++;
                    // Remove added arrow for next state calc
//                    int size = newState.arrows.size();
//                    newState.arrows.set(size-1,null);
                }
            }
        }
        System.out.println("Total States: " + stateCount);
        return successors;
    }
}
