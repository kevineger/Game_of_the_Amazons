import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

/**
 * Created by TCulos on 2015-03-12.
 */
public class SearchTree {

    private SearchNode root;
    private int numMoves;
    protected SearchNode bestMove = null;
    private int depth;
    public static int evaluations;
    private GameTimer timer = new GameTimer();
    private ArrayList<SearchNode> frontier = new ArrayList<SearchNode>();
    private MinKingDistHeuristic kingHeuristic = new MinKingDistHeuristic();
    private MinQueenDistHeuristic queenHeuristic = new MinQueenDistHeuristic();

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
            if(numMoves>=14) {
                kingHeuristic.calculate(S.B);
                S.setValue(kingHeuristic.ownedByUs);
            }else{
                queenHeuristic.calculate(S.B);
                S.setValue(queenHeuristic.ownedByUs);
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


        if(D == 0 || N.getChildren().size() == 0) {
            evaluations++;
            if(numMoves<7) {
                queenHeuristic.calculate(N.B);
                N.setValue(queenHeuristic.ownedByUs - queenHeuristic.ownedByThem);

            }else{
                kingHeuristic.calculate(N.B);
                N.setValue(kingHeuristic.ownedByUs - kingHeuristic.ownedByThem);

            }
            int val = N.getValue();
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
    private boolean terminalNode(SearchNode n, int depth) {
        return(depth == 0 || n.getChildren().size() == 0);
    }

    public int miniMax(SearchNode node, int depth, String player) {
        //if end of the tree is reached calculate and set the heuristic value
        if(terminalNode(node, depth)) {

            if (numMoves < 7) {
                queenHeuristic.calculate(node.B);
                node.setValue(queenHeuristic.ownedByUs - queenHeuristic.ownedByThem);
            } else {
                kingHeuristic.calculate(node.B);
                node.setValue(kingHeuristic.ownedByUs - kingHeuristic.ownedByThem);

            }
            int heuristicValue = node.getValue();
            return heuristicValue;
        }
        //if it is a Max player
        if(player.equals("Max")){
            // set bestValue to equivalent of negative infinity
            int bestVal = Integer.MIN_VALUE;

            for(SearchNode sNode: node.getChildren()){
                bestVal = Math.max(bestVal,miniMax(sNode,depth-1,"Min"));

            }
            node.setValue(bestVal);
            return bestVal;

        }
        else{
            // set bestValue to equivalent of infinity
            int bestVal = Integer.MAX_VALUE;
            for(SearchNode sNode: node.getChildren()){
                bestVal = Math.min(bestVal, miniMax(sNode, depth-1,"Max"));
            }
            node.setValue(bestVal);
            return bestVal;
        }

    }

    public SearchNode sendMoveToServer(){
        /*
        if(numMoves >= 0 && numMoves <= 28){
            this.expandFrontier();
        }
        */
        this.expandFrontier();
//
//        if(numMoves > 30) {
////            this.trimFrontier();
//            this.expandFrontier();
//        }else if(numMoves>50 && numMoves<=60 ){
////            this.trimFrontier();
//            this.expandFrontier();
////            this.trimFrontier();
//            this.expandFrontier();
////            this.trimFrontier();
//        }else if(numMoves> 60){
//            this.expandFrontier();
////            this.trimFrontier();
//            this.expandFrontier();
////            this.trimFrontier();
//            this.expandFrontier();
//
//            this.expandFrontier();
//        }



        //this.StartAlphaBeta();
        calculateDepth();
        miniMax(root, depth, "Max");
        //will work the same for minimax
        bestMove = this.getMoveAfterAlphaBeta();
        this.makeMoveOnRoot(bestMove.getMove(),bestMove.getArrowShot());
        return bestMove;
    }

    public SearchNode iterativeDeepening(){
        timer.startClock();
        bestMove = null;
        this.expandFrontier();
        while(timer.validTime()){
            this.StartAlphaBeta();
            bestMove = this.getMoveAfterIterative();
//            this.trimFrontier();
            this.expandFrontier();
        }
        return this.getMoveAfterIterative();
    }

//    public SearchNode

    /**
     * returns the best move to be made according to alpha beta
     * @return serchnode to be parsed for the move and arrowShot
     */
    private SearchNode getMoveAfterAlphaBeta(){
        SearchNode R;
        Random rand = new Random();
        int max = Integer.MIN_VALUE;
        ArrayList<SearchNode> best = new ArrayList<SearchNode>();
        for(SearchNode S:root.getChildren()){
            if(max <= S.getValue()) {
                max = S.getValue();
            }
        }
        for (SearchNode S: root.getChildren() ) {
            if(max <= S.getValue()) {
                best.add(S);
            }
        }
        System.out.println(root.getChildren().size());
        System.out.println("Best Value Among Options: " + max);
        System.out.println("Number Of Moves Available: " + best.size());
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
        int max = Integer.MIN_VALUE;
        ArrayList<SearchNode> best = new ArrayList<SearchNode>();
        for(SearchNode S:root.getChildren()){
            max = Math.max(max, S.getValue());
        }
        for (SearchNode S: root.getChildren() ) {
            if(max <= S.getValue()) {
                max = S.getValue();
            }
        }
        System.out.println(root.getChildren().size());
        System.out.println("Best Value Amone Options: " + max);
        System.out.println("Number Of Moves Available: " + best.size());
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