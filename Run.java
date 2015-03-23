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

        MinKingDistHeuristic h = new MinKingDistHeuristic(B);
        h.calculate();
        System.out.println("Owned by Us: "+h.ownedByUs);
        System.out.println("Owned by Them: "+h.ownedByThem);

        T.test2();

//        GamePlayer gp = new GamePlayer("1","");
    }
}