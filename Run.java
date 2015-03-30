import ubco.ai.games.Amazon;
import ubco.ai.games.GameClient;

import java.util.ArrayList;


public class Run {

    public static void main(String[] args) {

/*


        Queen[] friendly = new Queen[4];
        Queen[] enemies = new Queen[4];
        friendly = new Queen[] { new Queen(6,2, true), new Queen(5,4, true), new Queen(5,8, true), new Queen(3,9, true) };
        enemies = new Queen[] { new Queen(6,1, false), new Queen(8,1, false), new Queen(6,6, false), new Queen(4,6, false) };



        ArrayList<Arrow> arrows = new ArrayList<>();
        for(int i=0; i<10; i++) {
            arrows.add(new Arrow(i,i));
        }
        for(int i=9; i>=0; i--) {
            arrows.add(new Arrow(i,9-i));
        }

        BoardLogic B = new BoardLogic(enemies, friendly, arrows);


//        BoardLogic B = new BoardLogic(true);
//        SearchNode S = new SearchNode(B);
//        SearchTree T = new SearchTree(S);
//
//        T.test2();

*/


        //Random Player Test
//        BoardLogic bl = new BoardLogic(true);
//        SearchNode S = new SearchNode(bl);
//        SearchTree T = new SearchTree(S);
//
//        T.expandFrontier();
//        T.StartAlphaBeta();


        GamePlayer gp = new GamePlayer("asdf","asdf");
//
//        try {
//            Thread.sleep(2000);
//        } catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }

//        GamePlayer gp2 = new GamePlayer("5", "5");


    }
}