import ubco.ai.games.Amazon;

import java.util.ArrayList;


public class Run {
    public static void main(String[] args){
    	GamePlayer gp = new GamePlayer("","");
//    	System.out.println("List of Rooms:"+gp.getRooms());
//    	gp.joinRoom();

        ArrayList<moveData> moves = gp.bl.getLegalQueenMoves();
        Queen toMove = (Queen)moves.get(0).Q;
        System.out.print(moves.get(0).toString());
        toMove.move(moves.get(0).colPos,moves.get(0).rowPos);

        gp.bl.updateAfterMove();
    }
}