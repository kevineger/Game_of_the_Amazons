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

    public void StartAlphaBeta(){
        calculateDepth();
        AlphaBeta(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
    }

    private int AlphaBeta(SearchNode N, int D, int alpha, int beta, boolean maxPlayer){

        if(D == 0) {
            evaluations++;
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

            return V;
        }else{

            int V = Integer.MAX_VALUE;
            for(SearchNode S: N.getChildren()){

                V = Math.min(V, AlphaBeta(S,D - 1, alpha, beta, true ));
                beta = Math.min(beta, V);

                if(beta < alpha)
                    break;
            }

            return V;
        }
    }

    public void test1(){
        System.out.println("Starting test 1 for frontier of increasing heuristic val 1-9");
        for (int i = 0; i < 3; i++) {
            root.getChildren().add(new SearchNode(null));
        }

        int j = 1;
        for(SearchNode S: root.getChildren()){

            for (int i = 0; i < 3; i++) {
                S.getChildren().add(new SearchNode(null ,j));
                j++;
            }
        }

        this.StartAlphaBeta();
        System.out.println("Nodes Evaluated: " +evaluations);
    }
}
