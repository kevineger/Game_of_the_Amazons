import ubco.ai.games.Amazon;
import ubco.ai.games.GameClient;

import java.util.ArrayList;


public class Run {
//    Testing run

    public static void main(String[] args) {

//        BoardLogic B = new BoardLogic(false);
//        SearchNode S = new SearchNode(B);
//        SearchTree T = new SearchTree(S);
//
//        T.test2();

        GamePlayer gp = new GamePlayer("4","");

        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        GamePlayer gp2 = new GamePlayer("5", "5");
    }
}