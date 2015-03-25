import java.util.ArrayList;

public class Run {

    public static void main(String[] args) {


        Queen[] friendly;
        Queen[] enemies;
        friendly = new Queen[] { new Queen(0,3, false), new Queen(0,6, false), new Queen(3,0, false), new Queen(3,9, false) };
        enemies = new Queen[] { new Queen(6,0, true), new Queen(6,9, true), new Queen(9,3, true), new Queen(9,6, true) };

        ArrayList<Arrow> arrows = new ArrayList<>();
        for(int i=0; i<10; i++) {
            arrows.add(new Arrow(i,i));
        }
        for(int i=9; i>=0; i--) {
            arrows.add(new Arrow(i,9-i));
        }

//        BoardLogic B = new BoardLogic(enemies, friendly, arrows);


        BoardLogic B = new BoardLogic(true);
        SearchNode S = new SearchNode(B);
        SearchTree T = new SearchTree(S);
        System.out.println("Starting Board for iterative deepening: " + T.getRoot().B.toString());
        SearchNode S2 = T.iterativeDeepening();
        System.out.println("Chosen board was " + S2.B.toString());
        System.out.println("Must do further testing against another player");
    }
}