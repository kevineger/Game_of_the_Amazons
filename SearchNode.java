import java.util.ArrayList;

/**
 * node class used to create the search tree
 * Created by TCulos on 2015-03-11.
 */
public class SearchNode {
    private BoardLogic B;
    private Integer value = 0;
    private  ArrayList<SearchNode> children = new ArrayList<SearchNode>();

    /**
     * creates a search node from a board and an evaluation
     * @param board
     * @param heuristicValue
     */
    public SearchNode(BoardLogic board, int heuristicValue){
        B = board;
        value = heuristicValue;
    }

    /**
     * creates a node with only a baord, value to be set later
     * @param board
     */
    public SearchNode(BoardLogic board){
        B = board;
    }


    /**
     * instantiates the list of children of this board state
     */
    public void expand(){
        SuccessorFunction funct = new SuccessorFunction();
        ArrayList<BoardLogic> expanded = funct.getSuccessors(B);

        for(BoardLogic child:expanded){
            children.add(new SearchNode(child));
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
     * adds a child to this node
     * @param S
     */
    public void addChild(SearchNode S){
        children.add(S);
    }

    /**
     * returns the heuristic value
     * @return
     */
    public int getValue(){
        return value;
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
