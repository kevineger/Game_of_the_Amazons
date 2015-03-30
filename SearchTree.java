import java.util.ArrayList;
import java.util.Random;

/**
 * Created by TCulos on 2015-03-12.
 */
public class SearchTree {

    private SearchNode root;
    private int numMoves;
    protected SearchNode bestMove = null;
    private int depth;
    public static int evaluations;
//    private GameTimer timer = new GameTimer();
    private ArrayList<SearchNode> frontier = new ArrayList<SearchNode>();
    private MinKingDistHeuristic heuristic = new MinKingDistHeuristic();
//    private MinQueenDistHeuristic heuristic = new MinQueenDistHeuristic();

    public SearchTree(SearchNode N){
        root = N;
        depth = 1;
        evaluations = 0;
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
        numMoves++;
        root.B.addArrow(a); // adds arrow to be shot
        if(M.Q.isOpponent)
            System.out.println("Number of enemy moves: " + numMoves);
        else
            System.out.println("Number of our moves: " +numMoves);
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
        this.clearTree();
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
        if(numMoves != 1 && numMoves != 0 && depth != 0){
            if(depth % 2 ==0){
                for(SearchNode S: frontier)
                    newFrontier.addAll(S.setAllChildren(true));
            }else{
                for(SearchNode S: frontier)
                    newFrontier.addAll(S.setAllChildren(false));
            }
        }else{
            newFrontier.addAll(root.setAllChildren(true));
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
        System.out.println("Frontier size:" + frontier.size());
        int avg = 0;
        for (SearchNode S: frontier){
            if(numMoves<40) {
                QueenHeuristic.calculate(S.B);
                S.setValue(QueenHeuristic.ownedByUs);
            }else{
                QueenHeuristic.calculate(S.B);
                S.setValue(QueenHeuristic.ownedByUs);
            }

                avg += S.getValue();
        }
        if(frontier.size() != 0)
            avg = avg/frontier.size();
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
        evaluations=0;
        calculateDepth();
        System.out.println("Starting Alpha Beta\nHeursitic Val: "+root.toString()+"\nDepth:"+depth+"\n");
        AlphaBeta(root, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        System.out.println("exiting alpha beta");
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


        if(D == 0 || N.getChildren().size() == 0) {
            evaluations++;
            if(numMoves<20) {
                QueenHeuristic.calculate(N.B);
                N.setValue(QueenHeuristic.ownedByUs);
            }else{
                QueenHeuristic.calculate(N.B);
                N.setValue(QueenHeuristic.ownedByUs);
            }
            int val = N.getValue();
            System.out.println(N.B.toString());
            System.out.println("Setting Frontier to Value "+val);
            return val;
        }

        if(maxPlayer){

            int V = Integer.MIN_VALUE;
            for(SearchNode S: N.getChildren()){

                V = Math.max(V, AlphaBeta(S,D - 1, alpha, beta, false ));
                alpha = Math.max(alpha, V);

                if(beta <= alpha)
                    break;
            }
            N.setValue(V);
            return V;
        }else{

            int V = Integer.MAX_VALUE;
            for(SearchNode S: N.getChildren()){

                V = Math.min(V, AlphaBeta(S,D - 1, alpha, beta, true ));
                beta = Math.min(beta, V);

                if(beta <= alpha)
                    break;
            }
            N.setValue(V);
            return V;
        }
    }

    public SearchNode sendMoveToServer(){
        /*
        if(numMoves >= 0 && numMoves <= 28){
            this.expandFrontier();
        }

        this.expandFrontier();

//        if(numMoves > 25 && numMoves <= 50) {
////            this.trimFrontier();
//            this.expandFrontier();
//        }else if(numMoves>50){
////            this.trimFrontier();
//            this.expandFrontier();
////            this.trimFrontier();
//            this.expandFrontier();
////            this.trimFrontier();
//        }

*/

        this.StartAlphaBeta();
        bestMove = this.getMoveAfterAlphaBeta();
        this.makeMoveOnRoot(bestMove.getMove(),bestMove.getArrowShot());
        return bestMove;
    }

    public void iterativeDeepening(){
        bestMove = null;
        while(true){

            this.StartAlphaBeta();
            bestMove = this.getMoveAfterIterative();
            this.trimFrontier();
            this.expandFrontier();

        }
    }

//    public SearchNode

    /**
     * returns the best move to be made according to alpha beta
     * @return serchnode to be parsed for the move and arrowShot
     */
    private SearchNode getMoveAfterAlphaBeta(){
        SearchNode R;
        Random rand = new Random();
        int max = 0;
        ArrayList<SearchNode> best = new ArrayList<SearchNode>();
        for(SearchNode S:root.getChildren()){
            if(max <= S.getValue()) {
                max =S.getValue();
            }
        }
        for (SearchNode S: root.getChildren() ) {
            if(max <= S.getValue()) {
                best.add(S);
            }
        }

        if(best.size() > 1)
            R = best.get(rand.nextInt(best.size()-1));
        else
            R = best.get(0);

        return R;
    }

    /**
     * returns the best move to be made according to alpha beta
     * @return searchnode to be parsed for the move and arrowShot
     */
    public SearchNode getMoveAfterIterative(){
        Random rand = new Random();
        int max = 0;
        ArrayList<SearchNode> best = new ArrayList<SearchNode>();
        for(SearchNode S:root.getChildren()){
            if(max <= S.getValue()) {
                max =S.getValue();
            }
        }
        for (SearchNode S: root.getChildren() ) {
            if(max <= S.getValue()) {
                best.add(S);
            }
        }
        if(best.size() > 1)
            return best.get(rand.nextInt(best.size()-1));
        else
            return best.get(0);
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