import java.util.ArrayList;

/**
 * Created by TCulo_000 on 2015-03-12.
 */
public class testTree{

    SearchNode root = new SearchNode(null);
    private ArrayList<SearchNode> children = new ArrayList<SearchNode>();

    public testTree(){}

    public void test1(){

        for (int i = 0; i < 3; i++) {
            root.getChildren().add(new SearchNode(null));
        }
        int j = 0;
        for(SearchNode S: root.getChildren()){

            for (int i = 0; i < 3; i++) {
                S.getChildren().add(new SearchNode(null ,j));
                j++;
            }
        }

        SearchTree T = new SearchTree(root);
        T.StartAlphaBeta();
    }
}
