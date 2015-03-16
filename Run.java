
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;


public class Run {
//    Testing run
    public static void main(String[] args){

        BoardLogic B = new BoardLogic(false);
        SearchNode S = new SearchNode(B);
        SearchTree T = new SearchTree(S);

        T.test2();

    }
}