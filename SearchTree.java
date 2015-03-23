import java.util.ArrayList;

/**
 * Created by TCulo_000 on 2015-03-12.
 */
public class SearchTree {
    private SearchNode root;
    private int depth;
    public static int evaluations;
    public ArrayList<SearchNode> frontier = new ArrayList<SearchNode>();

    public SearchTree(SearchNode N){
        root = N;
        evaluations = 0;
        calculateDepth();
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

    /**
     * expands the frontier of the tree depending on the depth will do a MIN or MAX move
     */
    public void expandFrontier(){
        ArrayList<SearchNode> newFrontier = new ArrayList<SearchNode>();
        if(depth % 2 ==0){
            for(SearchNode S: frontier)
                newFrontier.addAll(S.setAllChildren(true));
        }else{
            for(SearchNode S: frontier)
                newFrontier.addAll(S.setAllChildren(false));
        }

        //clearing the old frontier and setting the new one
        frontier.clear();
        frontier.addAll(newFrontier);
        depth++;

//        trimFrontier();
    }


    /**
     * removes all nodes in the frontier that are less than the average heuristic value
     */
    public void trimFrontier(){
        Double avg = 0.0;
        System.out.println("Frontier size:"+frontier.size());
        for (SearchNode S: frontier){
            S.setHeuristicValue();
            avg += S.getValue();
        }

        avg = avg/frontier.size();
        System.out.println("Average is: "+avg);
        ArrayList<SearchNode> toRemove = new ArrayList<SearchNode>();
        for(SearchNode S: frontier){
            if(S.getValue() < avg){
                toRemove.add(S);
            }
        }

        for(SearchNode S: toRemove){
            frontier.remove(S);
            S.parent.getChildren().remove(S);
        }
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
            root.getChildren().add(new SearchNode(null,null,null));
        }

        for(SearchNode S: root.getChildren()){
            for (int i = 0; i < 3; i++) {
                S.getChildren().add(new SearchNode(null,null,null));
            }
        }

        for (SearchNode S: root.getChildren()) {
            for(SearchNode H: S.getChildren()) {
                for (int i = 0; i < 3; i++) {
                    H.getChildren().add(new SearchNode(null,null,null));
                }
            }
        }

        int j = 1;
        for (SearchNode S: root.getChildren()) {
            for(SearchNode H: S.getChildren()) {
                for(SearchNode G: H.getChildren()){
                    for (int i = 0; i <3 ; i++) {
                        G.getChildren().add(new SearchNode(null,null,null, j));
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
        System.out.println("Starting test2 for expanding with frontier array list");
        System.out.println("----------------------------");
        System.out.println("Root: " + root.B.toString());
        frontier.addAll(root.setAllChildren(true));
        System.out.println("Frontier Size before: "+frontier.size());
        trimFrontier();
        System.out.println("Frontier Size after: " + frontier.size());
        System.out.println("----------------------------");
        System.out.println("starting second level two generation");
        expandFrontier();
        System.out.println("Frontier Size before: "+frontier.size());
        trimFrontier();
        System.out.println("Frontier Size after: " + frontier.size());


    }
}
