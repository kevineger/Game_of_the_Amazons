import ubco.ai.games.Amazon;
import ubco.ai.games.GameClient;

import java.util.ArrayList;


public class Run {

    public static void main(String[] args) {

        /*

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


//        GamePlayer gp = new GamePlayer("4","");
//
//        try {
//            Thread.sleep(2000);
//        } catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }



//        GamePlayer gp2 = new GamePlayer("5", "5");






        Queen[] friendly = new Queen[4];
        Queen[] enemies = new Queen[4];
        friendly = new Queen[] { new Queen(3,7, true), new Queen(6,1, true), new Queen(7,4, true), new Queen(7,7, true) };
        enemies = new Queen[] { new Queen(7,3, false), new Queen(7,5, false), new Queen(7,9, false), new Queen(8,1, false) };

        ArrayList<Arrow> arrows = new ArrayList<>();

        arrows.add(new Arrow(1,2));
        arrows.add(new Arrow(2,0));
        arrows.add(new Arrow(3,3));
        arrows.add(new Arrow(5,7));
        arrows.add(new Arrow(5,8));
        arrows.add(new Arrow(5,9));
        arrows.add(new Arrow(1,2));
        arrows.add(new Arrow(6,0));
        arrows.add(new Arrow(6,2));
        arrows.add(new Arrow(6,3));
        arrows.add(new Arrow(6,4));
        arrows.add(new Arrow(6,5));
        arrows.add(new Arrow(6,6));
        arrows.add(new Arrow(6,8));
        arrows.add(new Arrow(6,9));
        arrows.add(new Arrow(7,1));
        arrows.add(new Arrow(7,2));
        arrows.add(new Arrow(7,6));
        arrows.add(new Arrow(7,8));
        arrows.add(new Arrow(8,0));
        arrows.add(new Arrow(8,5));
        arrows.add(new Arrow(8,7));

        arrows.add(new Arrow(8,6));

        arrows.add(new Arrow(8,8));
        arrows.add(new Arrow(9,0));
        arrows.add(new Arrow(9,1));
        arrows.add(new Arrow(9,2));
        arrows.add(new Arrow(9,5));
        arrows.add(new Arrow(9,7));
        BoardLogic B = new BoardLogic(enemies, friendly, arrows);

        System.out.println("Starting Board: "+B.toString());

        SearchNode sn = new SearchNode(B);
        SearchTree st = new SearchTree(sn);
        st.expandFrontier();
        st.StartAlphaBeta();


    }
}