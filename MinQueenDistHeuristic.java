import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Kevin on 2015-03-12.
 */
public class MinQueenDistHeuristic {
    BoardLogic bl = null;
    Queen[] queens = new Queen[8];
    int ownedByUs, ownedByThem;

    public MinQueenDistHeuristic(BoardLogic b) {
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
                    System.out.println("For tile: "+i+" (row), "+j+" (col)");
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
        q = addQueenMoves(q, row, col, checked);
        while(!isFound) {
            Queue<LocationData> tempQ = new LinkedList<>();
            int index = q.size();
            for(int i=0; i<index; i++) {
                LocationData currentTile = q.poll();
                // If queen is found, increase count and break
                if(bl.board[currentTile.rowPos][currentTile.colPos]!=null && bl.board[currentTile.rowPos][currentTile.colPos].isQueen) {
                    isFound=true;
                    boolean enemyQueen=bl.board[currentTile.rowPos][currentTile.colPos].isOpponent;
                    boolean contested=false;
                    // Check if tile is contested
                    for(LocationData shell : q) {
                        // If a queen is found in the rest of the queue, tile is contested
                        if(bl.board[shell.rowPos][shell.colPos]!=null && bl.board[shell.rowPos][shell.colPos].isQueen && !bl.board[shell.rowPos][shell.colPos].isOpponent==enemyQueen)
                            contested=true;
                    }
                    if(contested)
                        break;
                    // Determine who's queen it is, increase count accordingly
                    if(bl.board[currentTile.rowPos][currentTile.colPos].isOpponent){
                        ownedByThem++;
                        System.out.println("Owned by them");
//                        System.out.println("Enemy queen owns tile: "+currentTile.rowPos+" (row), "+currentTile.colPos+" (col)");
                    }
                    else {
                        ownedByUs++;
                        System.out.println("Owned by us");
//                        System.out.println("Friendly queen owns tile: "+currentTile.rowPos+" (row), "+currentTile.colPos+" (col)");
                    }
                    // If element is the last in the queue (ie: current 'layer') then break
                    break;
                }
                // If not a queen, mark location as checked
                else checked[currentTile.rowPos][currentTile.colPos]=true;
                // If free square (not arrow), add neighbours of it to tempQ
                if(bl.board[currentTile.rowPos][currentTile.colPos]==null) {
                    tempQ = addQueenMoves(tempQ, currentTile.rowPos, currentTile.colPos, checked);
                }
            }
            q = tempQ;
        }
    }
    public Queue<LocationData> addQueenMoves(Queue q, int curRow, int curCol, boolean[][] checked) {

        // Legal Moves Left
        for(int i = 1; curCol-i>=0; i++) {
            LocationData lData = new LocationData(curRow,curCol-1);
            if(!checked[curRow][curCol-1])
                q.add(lData);
            // If a Game piece was hit, break adding queen moves
            if(bl.board[curRow][curCol-i]!=null)
                break;
        }

        // Legal Moves Diagonal Left/Up
        for(int i = 1; curRow-i>=0&&curCol-i>=0; i++) {
            LocationData lData = new LocationData(curRow-i,curCol-i);
            if(!checked[curRow-i][curCol-i])
                q.add(lData);
            // If a Game piece was hit, break adding queen moves
            if(bl.board[curRow-i][curCol-i]!=null)
                break;
        }

        // Legal Moves Up
        for(int i = 1; curRow-i>=0; i++) {
            LocationData lData = new LocationData(curRow-i,curCol);
            if(!checked[curRow-i][curCol])
                q.add(lData);
            // If a Game piece was hit, break adding queen moves
            if(bl.board[curRow-i][curCol]!=null)
                break;
        }

        // Legal Moves Diagonal Right/Up
        for(int i = 1; curRow-i>=0&&curCol+i<=9; i++) {
            LocationData lData = new LocationData(curRow-i,curCol+i);
            if(!checked[curRow-i][curCol+i])
                q.add(lData);
            // If a Game piece was hit, break adding queen moves
            if(bl.board[curRow-i][curCol+i]!=null)
                break;
        }

        // Legal Moves Right
        for(int i = 1; curCol+i<=9; i++) {
            LocationData lData = new LocationData(curRow,curCol+i);
            if(!checked[curRow][curCol+i])
                q.add(lData);
            // If a Game piece was hit, break adding queen moves
            if(bl.board[curRow][curCol+i]!=null)
                break;
        }

        // Legal Moves Diagonal Right/Down
        for(int i = 1; curRow+i<=9&&curCol+i<=9; i++) {
            LocationData lData = new LocationData(curRow+i,curCol+i);
            if(!checked[curRow+i][curCol+i])
                q.add(lData);
            // If a Game piece was hit, break adding queen moves
            if(bl.board[curRow+i][curCol+i]!=null)
                break;
        }

        // Legal Moves Down
        for(int i = 1; curRow+i<=9; i++) {
            LocationData lData = new LocationData(curRow+i,curCol);
            if(!checked[curRow+i][curCol])
                q.add(lData);
            // If a Game piece was hit, break adding queen moves
            if(bl.board[curRow+i][curCol]!=null)
                break;
        }

        //Legal Moves Diagonal Left/Down
        for(int i = 1; curRow+i<=9&&curCol-i>=0; i++) {
            LocationData lData = new LocationData(curRow+i,curCol-i);
            if(!checked[curRow+i][curCol-i])
                q.add(lData);
            // If a Game piece was hit, break adding queen moves
            if(bl.board[curRow+i][curCol-i]!=null)
                break;
        }

        return q;
    }
}