import ubco.ai.games.Amazon;
import ubco.ai.games.GameClient;

import java.util.ArrayList;


public class Run {
//    Testing run

    public static void main(String[] args) {

        Queen[] friendly = new Queen[4];
        Queen[] enemies = new Queen[4];
        friendly = new Queen[] { new Queen(0,3, true), new Queen(0,6, true), new Queen(3,0, true), new Queen(3,9, true) };
        enemies = new Queen[] { new Queen(6,0, false), new Queen(6,9, false), new Queen(9,3, false), new Queen(9,6, false) };

        ArrayList<Arrow> arrows = new ArrayList<>();
        for(int i=0; i<10; i++) {
            arrows.add(new Arrow(i,i));
        }
        for(int i=9; i>=0; i--) {
            arrows.add(new Arrow(i,9-i));
        }

        BoardLogic B = new BoardLogic(enemies, friendly, arrows);
//        System.out.println("Filled test board:"+B.toString());


//        BoardLogic B = new BoardLogic(true);
        SearchNode S = new SearchNode(B);
        SearchTree T = new SearchTree(S);

        T.test2();
        
        //Random Player Test
        
        GamePlayer gp = new GamePlayer("4","");

        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        GamePlayer gp2 = new GamePlayer("5", "5");
    }
}