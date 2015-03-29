import java.util.ArrayList;

/**
 * node class used to create the search tree
 * Created by TCulos on 2015-03-11.
 */
public class SearchNode {

    protected SearchNode parent;
    private move queenMove;
    private Arrow arrowShot;
    protected BoardLogic B;
    private Integer value = 0;
    private ArrayList<SearchNode> children = new ArrayList<SearchNode>();
    private SuccessorFunction2 funct = new SuccessorFunction2();

    /**
     * search node used for determining a move with all values being instantiated
     * @param P parent of this search node
     * @param board game state
     * @param M queen move
     * @param A arrow show
     * @param heuristicValue
     */
    public SearchNode(SearchNode P,BoardLogic board,move M, Arrow A, int heuristicValue){
        parent = P;
        B = board;
        queenMove = M;
        arrowShot = A;
        value = heuristicValue;
    }

    /**
     * search node used for determining a move with all values being instantiated
     * @param board game state
     * @param M queen move
     * @param A arrow show
     * @param heuristicValue
     */
    public SearchNode(BoardLogic board,move M, Arrow A, int heuristicValue){
        B = board;
        queenMove = M;
        arrowShot = A;
        value = heuristicValue;
    }

    /**
     * search node with hueristic value to be set later
     * @param board game state
     * @param M queen move
     * @param A arrow show
     */
    public SearchNode(BoardLogic board,move M, Arrow A){
        B = board;
        queenMove = M;
        arrowShot = A;
    }

    /**
     * construtor used to set root of search tree
     * @param board
     */
    public SearchNode(BoardLogic board){
        B = board;
    }

    /**
     * instantiates the list of children of this board state expanding the frontier and returning it
     */
    public ArrayList<SearchNode> setAllChildren(boolean Move){

        if(Move) {
            ArrayList<SearchNode> expanded = funct.getSuccessors(B, true);
            for (SearchNode S: expanded){
                S.setParent(this);
                children.add(S);
            }
            return children;
        }else{
            ArrayList<SearchNode> expanded = funct.getSuccessors(B, false);
            for (SearchNode S: expanded) {
                S.setParent(this);
                children.add(S);
            }
            return children;
        }

    }

    /**
     * returns arraylist of the children of this node
     * @return
     */
    public ArrayList<SearchNode> getChildren(){
        return children;
    }

    /**
     * returns the move that got to this gamestate
     * @return
     */
    public move getMove(){
        return queenMove;
    }

    /**
     * returns the arrow shot to get from its parent to this state
     * @return
     */
    public Arrow getArrowShot(){
        return  arrowShot;
    }

    /**
     * returns the heuristic value
     * @return
     */
    public int getValue(){
        this.B.toString();
        return value;
    }

    /**
     * sets the parent of the node for use in set all children
     * @param S
     */
    public void setParent(SearchNode S){
        parent = S;
    }

    /**
     * allows you to set the heuristic value of a game state
     * @param V
     */
    public void setValue(int V){value = V;}

    public String toString(){
        if(value == null)
            return "Heuristic value not set";
        else
            return "Heuristic value = "+value;
    }
}
