import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

/**
 * Created by TCulos on 2015-03-12.
 */
public class SearchTree {

    private SearchNode root;                                                    //the root object of our searchnode tree
    private int numMoves;                                                       //number of moves made so far in a game
    private int depth;                                                          //depth level of our tree
    public static int evaluations;                                              //number of the evaluations done in alpha beta
    private ArrayList<SearchNode> frontier = new ArrayList<SearchNode>();                //Frontier of our tree for expansion
    private MinKingDistHeuristic kingHeuristic = new MinKingDistHeuristic();    //two hueristics we use in conjustion with each
    private MinQueenDistHeuristic queenHeuristic = new MinQueenDistHeuristic(); //other to play Amazons

    public SearchTree(SearchNode N){
        root = N;
        depth = 0;
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
        if(depth != 0){
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
        for(SearchNode S: newFrontier){
            SearchNode newNode =new SearchNode(S.B.deepCopy());
            frontier.add(newNode);
        }
        depth++;
    }


    /**
     * removes all nodes in the frontier that are less than the average heuristic value
     */
    public void trimFrontier(){
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

    public SearchNode sendMoveToServer(){
        this.expandFrontier();
        if(numMoves > 20 && numMoves<=40) {
            this.trimFrontier();
            this.expandFrontier();
        }else if(numMoves >40 && numMoves<=70 ){
            this.trimFrontier();
            this.expandFrontier();
            this.trimFrontier();
            this.expandFrontier();
        }else if(numMoves> 70){
            this.expandFrontier();
            this.trimFrontier();
            this.expandFrontier();
            this.trimFrontier();
            this.expandFrontier();
            this.trimFrontier();
            this.expandFrontier();
            this.trimFrontier();
            this.expandFrontier();
        }



        this.StartAlphaBeta();
        SearchNode bestMove = this.getMoveAfterAlphaBeta();
        this.makeMoveOnRoot(bestMove.getMove(),bestMove.getArrowShot());
        return bestMove;
    }

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
        System.out.println("Best Value Among Options: " + max);
        System.out.println("Number Of Best Moves Available: " + best.size());
        if(best.size() > 1)
            R = best.get(rand.nextInt(best.size()-1));
        else
            R = best.get(0);

        return R;
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