/**
 * Created by TCulo_000 on 2015-03-12.
 */
public class SearchTree {
    private SearchNode root;
    private int depth;
    public static int evaluations;

    public SearchTree(SearchNode N){
        evaluations = 0;
        root = N;
    }

    private void calculateDepth(){
        SearchNode N = root;
        int D=0;
        while(N != null){
            if(!N.getChildren().isEmpty())
                N = N.getChildren().get(0);
            else
                break;

            D++;
        }
        depth = D;
    }

    public SearchNode getRoot(){
        return root;
    }

    public void StartAlphaBeta(){
        calculateDepth();
        AlphaBeta(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
    }

    private int AlphaBeta(SearchNode N, int D, int alpha, int beta, boolean maxPlayer){

        if(D == 0) {
            evaluations++;
            N.setHeuristicValue();
            System.out.println(N.getValue());
            return N.getValue();
        }

        if(maxPlayer){

            int V = Integer.MIN_VALUE;
            for(SearchNode S: N.getChildren()){

                V = Math.max(V, AlphaBeta(S,D - 1, alpha, beta, false ));
                alpha = Math.max(alpha, V);

                if(beta < alpha)
                    break;
            }
            N.setValue(V);
            return V;
        }else{

            int V = Integer.MAX_VALUE;
            for(SearchNode S: N.getChildren()){

                V = Math.min(V, AlphaBeta(S,D - 1, alpha, beta, true ));
                beta = Math.min(beta, V);

                if(beta < alpha)
                    break;
            }
            N.setValue(V);
            return V;
        }
    }

    /**
     * test used before heuristic analysis implemented
     */
    public void test1(){
        System.out.println("Starting test 1 for frontier of increasing heuristic val 1-75");
        for (int i = 0; i < 3; i++) {
            root.getChildren().add(new SearchNode(null));
        }

        for(SearchNode S: root.getChildren()){
            for (int i = 0; i < 3; i++) {
                S.getChildren().add(new SearchNode(null));
            }
        }

        for (SearchNode S: root.getChildren()) {
            for(SearchNode H: S.getChildren()) {
                for (int i = 0; i < 3; i++) {
                    H.getChildren().add(new SearchNode(null));
                }
            }
        }

        int j = 1;
        for (SearchNode S: root.getChildren()) {
            for(SearchNode H: S.getChildren()) {
                for(SearchNode G: H.getChildren()){
                    for (int i = 0; i <3 ; i++) {
                        G.getChildren().add(new SearchNode(null, j));
                        j++;
                    }
                }
            }
        }

        evaluations = 0;

        this.StartAlphaBeta();
        System.out.println("Nodes Evaluated: " +evaluations);

        int i = 1;
        for(SearchNode S: root.getChildren()){
            System.out.println("Node: "+i +" value: " +S.getValue());
            i++;
        }
    }

    public void test2(){
        System.out.println("Starting test 2 for a tree of actual game data of depth 2");

        root.setAllChildren();
        for(SearchNode S: root.getChildren()){
            S.setAllChildren();
        }
        evaluations = 0;

//        System.out.println("Starting Alpha-Beta");
//        this.StartAlphaBeta();
//        System.out.println("Nodes Evaluated: " +evaluations);

    }
}
