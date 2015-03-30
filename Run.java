import ubco.ai.games.Amazon;
import ubco.ai.games.GameClient;

import java.util.ArrayList;


public class Run {

    public static void main(String[] args) {


//        Queen[] friendly = new Queen[4];
//        Queen[] enemies = new Queen[4];
//        friendly = new Queen[] { new Queen(5,4, false), new Queen(5,8, false), new Queen(6,2, false), new Queen(8,9, false) };
//        enemies = new Queen[] { new Queen(1,9, true), new Queen(4,6, true), new Queen(6,1, true), new Queen(7,4, true) };
//
//        ArrayList<Arrow> arrows = new ArrayList<Arrow>();
//        arrows.add(new Arrow(0,1));
//        arrows.add(new Arrow(0,9));
//        arrows.add(new Arrow(1,1));
//        arrows.add(new Arrow(1,2));
//        arrows.add(new Arrow(1,8));
//        arrows.add(new Arrow(2,0));
//        arrows.add(new Arrow(2,2));
//        arrows.add(new Arrow(2,8));
//        arrows.add(new Arrow(3,2));
//        arrows.add(new Arrow(3,3));
//        arrows.add(new Arrow(3,4));
//        arrows.add(new Arrow(3,8));
//        arrows.add(new Arrow(4,2));
//        arrows.add(new Arrow(4,4));
//        arrows.add(new Arrow(4,8));
//        arrows.add(new Arrow(4,9));
//        arrows.add(new Arrow(5,1));
//        arrows.add(new Arrow(5,2));
//        arrows.add(new Arrow(5,3));
//        arrows.add(new Arrow(5,6));
//        arrows.add(new Arrow(5,7));
//        arrows.add(new Arrow(6,0));
//        arrows.add(new Arrow(6,4));
//        arrows.add(new Arrow(6,5));
//        arrows.add(new Arrow(6,8));
//        arrows.add(new Arrow(6,9));
//        arrows.add(new Arrow(7,2));
//        arrows.add(new Arrow(7,3));
//        arrows.add(new Arrow(7,5));
//        arrows.add(new Arrow(7,6));
//        arrows.add(new Arrow(7,7));
//        arrows.add(new Arrow(8,3));
//        arrows.add(new Arrow(8,4));
//        arrows.add(new Arrow(8,7));
//        arrows.add(new Arrow(9,3));
//        arrows.add(new Arrow(8,2));
//        arrows.add(new Arrow(9,7));
//        BoardLogic BL = new BoardLogic(enemies,friendly, arrows);
//        System.out.println(BL.toString());
//
//        MinKingDistHeuristic mk = new MinKingDistHeuristic();
//        mk.calculate(BL);
//        System.out.println("Board Heuristic value " + mk.ownedByThem);


/*
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
        GamePlayer gp = new GamePlayer("asn","asdf");





    }
}