import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Kevin on 2015-03-12.
 */
public class MinKingDistHeuristic {
    BoardLogic bl = null;
    Queen[] queens = new Queen[8];
    int ownedByUs, ownedByThem;

    public MinKingDistHeuristic(BoardLogic b) {
        bl = b;
        // queens <- all queens (friendly and enemy)
        for(int i=0; i<8; i++) {
            if(i<4) {
                queens[i]=b.getFriendly()[i];
            }
            else {
                queens[i]=b.getEnemies()[i-4];
            }
        }
        ownedByUs=0;
        ownedByThem=0;
    }

    public void calculate() {
        // For every tile in the board
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                // If tile is empty, check who 'owns it'
                if(bl.board[i][j]==null){
                    findNearestQueen(i, j);
                }
            }
        }
    }
    public void findNearestQueen(int row, int col) {
        boolean[][] checked = new boolean[10][10];    // 2d board representation of if a spot has been checked
        checked[row][col]=true; // Mark starting tile as checked
        boolean isFound = false;
        // Initialize the queue
        Queue<LocationData> q = new LinkedList<>();
        q = addNeighbours(q, row, col, checked);
        outerloop:
        while(!isFound) {
            Queue<LocationData> tempQ = new LinkedList<>();
            for(int i=0; i<q.size(); i++) {
                LocationData currentTile = q.remove();
                // If queen is found, increase count and break
                if(bl.board[currentTile.rowPos][currentTile.colPos]!=null && bl.board[currentTile.rowPos][currentTile.colPos].isQueen) {
                    isFound=true;
                    // Determine who's queen it is, increase count accordingly
                    if(bl.board[currentTile.rowPos][currentTile.colPos].isOpponent) ownedByThem++;
                    else ownedByUs++;
                    break outerloop;
                }
                // If not a queen, mark location as checked
                else checked[currentTile.rowPos][currentTile.colPos]=true;
                // If free square (not arrow), add neighbours of it to tempQ
                if(bl.board[currentTile.rowPos][currentTile.colPos]==null) {
                    tempQ = addNeighbours(tempQ, currentTile.rowPos, currentTile.colPos, checked);
                }
            }
            q = tempQ;
        }
    }
    public Queue<LocationData> addNeighbours(Queue q, int row, int col, boolean[][] checked) {

        // If has l
        if(col>0) {
            LocationData lData = new LocationData(row,col-1);
            q.add(lData);
        }

        // If has l/u
        if(row>0 && col>0) {
            LocationData luData = new LocationData(row-1,col-1);
            q.add(luData);
        }

        // If has u
        if(row>0) {
            LocationData uData = new LocationData(row-1,col);
            q.add(uData);
        }

        // If has r/u
        if(row>0 && col<9) {
            LocationData ruData = new LocationData(row-1,col+1);
            q.add(ruData);
        }

        // If has r
        if(col<9) {
            LocationData rData = new LocationData(row,col+1);
            q.add(rData);
        }

        // If has r/d
        if(row<9 && col<9) {
            LocationData rdData = new LocationData(row+1,col+1);
            q.add(rdData);
        }

        // If has d
        if(row<9) {
            LocationData dData = new LocationData(row+1,col);
            q.add(dData);
        }

        // If has l/d
        if(row<9 && col>0) {
            LocationData ldData = new LocationData(row+1,col-1);
            q.add(ldData);
        }

        return q;
    }
}