import java.util.ArrayList;
import java.util.Random;

/**
 * Created by TCulos on 2015-03-12.
 */
public class SearchTree {

    private SearchNode root;
    private int numMoves;
    private int depth;
    public static boolean done = false; //used for iterative deepening
    public static int evaluations;
    private GameTimer timer = new GameTimer();
    private ArrayList<SearchNode> frontier = new ArrayList<SearchNode>();
    private MinKingDistHeuristic heuristic = new MinKingDistHeuristic();

    public SearchTree(SearchNode N){
        root = N;
        depth = 1;
        evaluations = 0;
        frontier.addAll(root.setAllChildren(true));
        timer.StartClock();
    }

    /**
     * calculates depth of tree instead of having to increment everywere
     */
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

    /**
     * makes a move for either us or them on the root board
     * @param M move
     * @param a arrow to be shot
     */
    public void makeMoveOnRoot(move M, Arrow a){
        root.B.addArrow(a); // adds arrow to be shot

        //makes a move for the queen ours or theirs
        if(M.Q.isOpponent){
            for(Queen Q:root.B.enemies){
                if(M.oldColPos == Q.colPos && M.oldRowPos == Q.rowPos)
                    Q.move(M.newRowPos,M.newColPos);
            }
        }else{
            for(Queen Q:root.B.friendly){
                if(M.oldColPos == Q.colPos && M.oldRowPos == Q.rowPos)
                    Q.move(M.newRowPos,M.newColPos);
            }
        }
        root.B.updateAfterMove();
    }


    /**
     * returns the root of this search tree
     * @return
     */
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
    }


    /**
     * removes all nodes in the frontier that are less than the average heuristic value
     */
    public void trimFrontier(){
        Double avg = 0.0;
        System.out.println("Frontier size:" + frontier.size());
        for (SearchNode S: frontier){
            heuristic.calculate(S.B);
            S.setValue(heuristic.ownedByUs);
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

    /**
     * starts alpha beta at the default values
     */
    public void StartAlphaBeta(){
        calculateDepth();
        AlphaBeta(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
    }

    /**
     * alpha beta pruning as described in class
     * @param N searchnode we are inspecting(default root)
     * @param D depth of our tree
     * @param alpha (default to -infinity)
     * @param beta (default to infinity)
     * @param maxPlayer
     * @return value of its move according to the value of its children
     */
    private int AlphaBeta(SearchNode N, int D, int alpha, int beta, boolean maxPlayer){

        if(D == 0) {
            evaluations++;
            heuristic.calculate(N.B);
            N.setValue(heuristic.ownedByUs);
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


    public SearchNode iterativeDeepening(){
        SearchNode bestMove = null;
        while(!done){
            this.trimFrontier();
            this.expandFrontier();
            this.StartAlphaBeta();
            bestMove = this.getMoveAfterIterative();
        }

        this.makeMoveOnRoot(bestMove.getMove(),bestMove.getArrowShot());
        return bestMove;
    }

    /**
     * returns the best move to be made according to alpha beta
     * @return serchnode to be parsed for the move and arrowShot
     */
    public SearchNode getMoveAfterAlphaBeta(){
        Random rand = new Random();
        int max = 0;
        numMoves++;
        ArrayList<SearchNode> best = new ArrayList<SearchNode>();
        for(SearchNode S:root.getChildren()){
            if(max <= S.getValue()) {
                max =S.getValue();
            }
        }
        for (SearchNode S: root.getChildren() ) {
            if(max <= S.getValue()) {
                System.out.println(S.B.toString());
               best.add(S);
            }
        }
        clearTree(); // delete all unecessary data
        if(best.size() > 1)
            return best.get(rand.nextInt(best.size()-1));
        else
            return best.get(1);
    }

    /**
     * returns the best move to be made according to alpha beta
     * @return searchnode to be parsed for the move and arrowShot
     */
    public SearchNode getMoveAfterIterative(){
        Random rand = new Random();
        int max = 0;
        numMoves++;
        ArrayList<SearchNode> best = new ArrayList<SearchNode>();
        for(SearchNode S:root.getChildren()){
            if(max <= S.getValue()) {
                max =S.getValue();
            }
        }
        for (SearchNode S: root.getChildren() ) {
            if(max <= S.getValue()) {
                System.out.println(S.B.toString());
                best.add(S);
            }
        }
        if(best.size() > 1)
            return best.get(rand.nextInt(best.size()-1));
        else
            return best.get(1);
    }

    /**
     * used to clear the tree apart from the root
     */
    private void clearTree(){
        depth = 0;
        frontier.clear();
        root.getChildren().clear();
    }
}