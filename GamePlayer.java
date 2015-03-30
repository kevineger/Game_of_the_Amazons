import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import net.n3.nanoxml.IXMLElement;
import ubco.ai.GameRoom;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;

/**
 *
 * Illustrate how to write a player. A player has to implement the GamePlayer interface.  
 *
 * @author Kevin
 *
 */

public class GamePlayer implements ubco.ai.games.GamePlayer {

    GameClient gameClient = null;
    BoardLogic bl;
    SearchTree T;

    String role;                    // role is the string that holds if we go first (B) or second (W)
    String teamName;
    int roomID;

    Board frame = null;

    /*
     * Constructor
     */
    public GamePlayer(String name, String passwd) {

        //A player has to maintain an instance of GameClient, and register itself with the
        //GameClient. Whenever there is a message from the server, the Gameclient will invoke
        //the player's handleMessage() method.
        //Three arguments: user name (any), passwd (any), this (delegate)
        teamName = name;
        role = "";

        gameClient = new GameClient(name, passwd, this);
        System.out.printf(getRooms());
        joinRoom();
    }

    //	Gets room list
    public String getRooms() {
        String rooms = gameClient.getRoomLists().toString();
        rooms = rooms.replace("[", "");
        rooms = rooms.replace("]", "");
        return rooms.replaceAll("Name", "\n   Name");
    }

    //	Join a room
    public void joinRoom() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nWhich room would you like to join?");

        String room = in.nextLine();            // stores the name of the room we will be joining
        gameClient.joinGameRoom(room);

        // now we will check through the list of game rooms and see which one has this name and determine the room number
        for (GameRoom r : gameClient.getRoomLists()) {
            if (r.roomName.equals(room)) {
                roomID = r.roomID;
                System.out.println("The room ID of the room we are in (" + room + ") is " + roomID);
                break;
            }
        }
    }

    //general message from the server
    public boolean handleMessage(String msg) throws Exception {
        System.out.println("Game message: " + msg);
        return true;
    }

    //game-specific message from the server
    public boolean handleMessage(GameMessage gmsg) throws Exception {
        IXMLElement xml = ServerMessage.parseMessage(gmsg.msg);
        String xmlType = xml.getAttribute("type", null);
        System.out.println("Message Type: " + xmlType);

        switch (xmlType) {
            case GameMessage.ACTION_GAME_START:
                System.out.println("Message Type: ACTION_GAME_START");
                //find name and associated room number
                String teamRole = "";

                IXMLElement firstChild = xml.getChildAtIndex(0);
                IXMLElement[] grandchildren = new IXMLElement[3];
                grandchildren[0] = firstChild.getChildAtIndex(0);
                grandchildren[1] = firstChild.getChildAtIndex(1);

                for (int i = 0; i < grandchildren.length; i++) {
                    if (grandchildren[i].getAttribute("name", null).equals(teamName)) {
                        teamRole = grandchildren[i].getAttribute("role", null);
                        break;
                    }
                }
                if(teamRole.equals("W")) frame = new Board(true);
                else frame = new Board(false);
                frame.setVisible(true);
                role = teamRole;                                // stores our role internally
                System.out.println("We are gameplayer "+ teamName + " and our Role is: " + role);


                // Now, what we create a board logic depending on whether we move first (true) or second (false)
                if (role.equals("W")) {
                    System.out.println("Our Move");                 // message to ourselves saying we are moving first
                    bl = new BoardLogic(true);
                    T = new SearchTree(new SearchNode(bl));
                    this.makeMove();
                }
                else {
                    bl = new BoardLogic(false);
                    T = new SearchTree(new SearchNode(bl));
                    System.out.println(bl.toString());
                    // now, we will wait for a message from opponent, and deal with it as such
                }
                break;
            case GameMessage.ACTION_MOVE:
                boolean end2 = false;
                System.out.println("Message Type: ACTION_MOVE");
                IXMLElement queen2 = xml.getChildAtIndex(0);
                String queen_move2 = queen2.getAttribute("move", null);            // queen_move is the value of the opponent's move
                IXMLElement arrow2 = xml.getChildAtIndex(1);
                String arrow_move2 = arrow2.getAttribute("move", null);            // arrow_move is the value of the opponent's arrow

                move queenMove2 = translateIn(queen_move2);
                Arrow arrow3 = translateArrowIn(arrow_move2);
                T.makeMoveOnRoot(queenMove2, arrow3);
                frame.update(queenMove2, arrow3);
//                System.out.println("\nEnemy's Move"+T.getRoot().B.toString());
                System.out.println("We are gameplayer " + teamName + " and our Role is: " + role);
                System.out.println("Board after enemy move: ");
                System.out.println(bl.toString());

                bl.setEnemyHasMove();
                bl.updateLegalQueenMoves();
                end2 = bl.goalTest();
                System.out.println("Goal test after opponent's move: " + bl.goalTest());
                if (end2) {
                    System.out.println();
                    System.out.println("GAME OVER");
                    System.out.println();
                    break;
                }
                this.makeMove();
                System.out.println("We are gameplayer " + teamName + " and our Role is: " + role);
                System.out.print("Board after our move: ");
                System.out.println(bl.toString());

                bl.setEnemyHasMove();
                bl.updateLegalQueenMoves();
                end2 = bl.goalTest();
                System.out.println("Goal test after our move: " + bl.goalTest());
                if (end2) {
                    System.out.println();
                    System.out.println("GAME OVER");
                    System.out.println();
                    break;
                }

                // Update the current board with the new board state
                // Then, create new set of viable moves (for arrows and queens)
                break;
            case GameMessage.ACTION_POS_MARKED:
                System.out.println("Message Type: ACTION_POS_MARKED");
                break;
            case GameMessage.ACTION_ROOM_JOINED:
                System.out.println("Message Type: ACTION_ROOM_JOINED");
                break;
            case GameMessage.MSG_CHAT:
                System.out.println("Message Type: MSG_CHAT");
                break;
            case GameMessage.MSG_GAME:
                boolean end = false;
                System.out.println("Message Type: MSG_GAME");
                IXMLElement queen1 = xml.getChildAtIndex(0);
                String queen_move1 = queen1.getAttribute("move", null);            // queen_move is the value of the opponent's move
                IXMLElement arrow1 = xml.getChildAtIndex(1);
                String arrow_move1 = arrow1.getAttribute("move", null);            // arrow_move is the value of the opponent's arrow

                move queenMove = translateIn(queen_move1);
                Arrow arrow = translateArrowIn(arrow_move1);
                T.makeMoveOnRoot(queenMove, arrow);
                frame.update(queenMove, arrow);
//                System.out.println("\nEnemy's Move"+T.getRoot().B.toString());
                System.out.println("We are gameplayer " + teamName + " and our Role is: " + role);
                System.out.println("Board after enemy move: ");
                System.out.println(bl.toString());

                bl.setEnemyHasMove();
                bl.updateLegalQueenMoves();
                end = bl.goalTest();
                System.out.println("Goal test after opponent's move: " + bl.goalTest());
                if (end) {
                    System.out.println();
                    System.out.println("GAME OVER");
                    System.out.println();
                    break;
                }
                this.makeMove();
                System.out.println("We are gameplayer " + teamName + " and our Role is: " + role);
                System.out.print("Board after our move: ");
                System.out.println(bl.toString());

                bl.setEnemyHasMove();
                bl.updateLegalQueenMoves();
                end = bl.goalTest();
                System.out.println("Goal test after our move: " + bl.goalTest());
                if (end) {
                    System.out.println();
                    System.out.println("GAME OVER");
                    System.out.println();
                    break;
                }

                // Update the current board with the new board state
                // Then, create new set of viable moves (for arrows and queens)
                break;
            case GameMessage.MSG_GENERAL:
                System.out.println("Message Type: MSG_GENERAL");
                break;
            case GameMessage.MSG_JOIN_ROOM:
                System.out.println("Message Type: MSG_JOIN_ROOM");
                break;
        }
        return true;
    }

    /*
     * Method that sends messages (movement)
     * queen_move should be in the format a3-g3 (where it refers to (column,row) of start position and end position)
     * arrow_move should be in the format h3    ( h3 is the (column,row) of the arrow to be placed
     */
    public void sendToServer(String msgType, int roomID, String queen_move, String arrow_move) {
        String actionMsg = "<action type='";
        actionMsg += msgType + "'>";

        // If the action is a move type, asks for info on move of queen and arrow
        actionMsg += "<queen move='";
        actionMsg += queen_move + "'></queen><arrow move='";
        actionMsg += arrow_move + "'></arrow>";
        actionMsg += "</action>";

        System.out.println("Message that is going : " + actionMsg);
        String msg = ServerMessage.compileGameMessage(GameMessage.MSG_GAME, roomID, actionMsg);        // GET ID OF ROOM
        gameClient.sendToServer(msg, true);
        System.out.println(roomID);
    }

    public void makeMove(){
        SearchNode S = T.sendMoveToServer();
        move made = S.getMove();
        Arrow arrowShot = S.getArrowShot();

        frame.update(made,arrowShot);

        String qMove = this.translateOut(made);
        String aMove = this.translateArrowOut(arrowShot);

        this.sendToServer(GameMessage.ACTION_MOVE, roomID,qMove, aMove);
    }


     /*
     * Method that randomly chooses a queen to move, moves that queen, and randomly throws an arrow from that queen
     * BoardLogic b: the current BoardLogic that is representing the game state
     *
     * The strings printed out at the end are in the form (col row) to follow the convention of sending to the server
     * returns: boolean representing the if the game has finished or not.
     */
    private boolean randomMove(BoardLogic b) {
        Random r = new Random();
        moveData md = null;
        move m;
        Queen q = b.getFriendly()[r.nextInt(4)];

        if(b.getLegalMoves(q).size()==0){
            for(Queen e : b.getFriendly()){
                if(b.getLegalMoves(e).size()!=0) {
                    q = e;
                    md = b.getLegalMoves(e).get(r.nextInt(b.getLegalMoves(e).size()));     // picks a random move
                    break;
                }
            }
        }
        else {
            md = b.getLegalMoves(q).get(r.nextInt(b.getLegalMoves(q).size()));

        }
        m = new move(md.colPos, md.rowPos, md.Q.colPos, md.Q.rowPos);
        q.move(md.rowPos, md.colPos);
        b.updateAfterMove();

        Arrow a = b.getArrowShots(q.rowPos,q.colPos).get(r.nextInt(b.getArrowShots(q.rowPos,q.colPos).size()));
        b.addArrow(a.rowPos, a.colPos);

        sendToServer(GameMessage.ACTION_MOVE, roomID, translateOut(m), translateArrowOut(a));
        System.out.println("We are gameplayer " + teamName + " and our Role is: " + role);
        System.out.print("Board after our move: ");
        System.out.println(b.toString());
        System.out.println();
        System.out.println(b.goalTest());
        b.updateLegalQueenMoves();
        b.setEnemyHasMove();
        return b.goalTest();
    }

     /*
     * Method to translate move to sever
     */

    public String translateOut(move m){

        //move=’a3-g3’ syntax

        int oldRow = -1;
        String oldCol = "";
        int newRow = -1;
        String newCol = "";
        String move="";
        int y = m.oldRowPos;
        int x = m.oldColPos;
        int y2 = m.newRowPos;
        int x2 = m.newColPos;

        switch(x){
            case 0: oldCol = "a";
                break;
            case 1: oldCol = "b";
                break;
            case 2: oldCol = "c";
                break;
            case 3: oldCol = "d";
                break;
            case 4: oldCol = "e";
                break;
            case 5: oldCol = "f";
                break;
            case 6: oldCol = "g";
                break;
            case 7: oldCol = "h";
                break;
            case 8: oldCol = "i";
                break;
            case 9: oldCol = "j";
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y){
            case 0: oldRow = 9;
                break;
            case 1: oldRow = 8;
                break;
            case 2: oldRow = 7;
                break;
            case 3: oldRow = 6;
                break;
            case 4: oldRow = 5;
                break;
            case 5: oldRow = 4;
                break;
            case 6: oldRow = 3;
                break;
            case 7: oldRow = 2;
                break;
            case 8: oldRow = 1;
                break;
            case 9: oldRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;
        }
        switch(x2){
            case 0: newCol = "a";
                break;
            case 1: newCol = "b";
                break;
            case 2: newCol = "c";
                break;
            case 3: newCol = "d";
                break;
            case 4: newCol = "e";
                break;
            case 5: newCol = "f";
                break;
            case 6: newCol = "g";
                break;
            case 7: newCol = "h";
                break;
            case 8: newCol = "i";
                break;
            case 9: newCol = "j";
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y2){
            case 0: newRow = 9;
                break;
            case 1: newRow = 8;
                break;
            case 2: newRow = 7;
                break;
            case 3: newRow = 6;
                break;
            case 4: newRow = 5;
                break;
            case 5: newRow = 4;
                break;
            case 6: newRow = 3;
                break;
            case 7: newRow = 2;
                break;
            case 8: newRow = 1;
                break;
            case 9: newRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;
        }

        move = move.concat(oldCol+oldRow+"-"+newCol+newRow);
        return move;

    }
    /*
     * Method to translate move from server
     */

    public move translateIn(String move){

        char x = move.charAt(0);
        char y = move.charAt(1);
        char x2 = move.charAt(3);
        char y2 = move.charAt(4);

        int oldCol = -1;
        int oldRow = -1;
        int newCol = -1;
        int newRow = -1;

        switch(x){
            case 'a': oldCol = 0;
                break;
            case 'b': oldCol = 1;
                break;
            case 'c': oldCol = 2;
                break;
            case 'd': oldCol = 3;
                break;
            case 'e': oldCol = 4;
                break;
            case 'f': oldCol = 5;
                break;
            case 'g': oldCol = 6;
                break;
            case 'h': oldCol = 7;
                break;
            case 'i': oldCol = 8;
                break;
            case 'j': oldCol = 9;
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y){
            case '0': oldRow = 9;
                break;
            case '1': oldRow = 8;
                break;
            case '2': oldRow = 7;
                break;
            case '3': oldRow = 6;
                break;
            case '4': oldRow = 5;
                break;
            case '5': oldRow = 4;
                break;
            case '6': oldRow = 3;
                break;
            case '7': oldRow = 2;
                break;
            case '8': oldRow = 1;
                break;
            case '9': oldRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;
        }
        switch(x2){
            case 'a': newCol = 0;
                break;
            case 'b': newCol = 1;
                break;
            case 'c': newCol = 2;
                break;
            case 'd': newCol = 3;
                break;
            case 'e': newCol = 4;
                break;
            case 'f': newCol = 5;
                break;
            case 'g': newCol = 6;
                break;
            case 'h': newCol = 7;
                break;
            case 'i': newCol = 8;
                break;
            case 'j': newCol = 9;
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y2){
            case '0': newRow = 9;
                break;
            case '1': newRow = 8;
                break;
            case '2': newRow = 7;
                break;
            case '3': newRow = 6;
                break;
            case '4': newRow = 5;
                break;
            case '5': newRow = 4;
                break;
            case '6': newRow = 3;
                break;
            case '7': newRow = 2;
                break;
            case '8': newRow = 1;
                break;
            case '9': newRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;
        }
        move moveMessage = new move(newRow, newCol, new Queen(oldRow,oldCol, true));

        System.out.println(moveMessage.toString());
        return moveMessage;
    }



    public String translateArrowOut(Arrow a) {

        String oldCol = "";
        int oldRow = 0;
        int x = a.colPos;
        int y = a.rowPos;
        switch(x){
            case 0: oldCol = "a";
                break;
            case 1: oldCol = "b";
                break;
            case 2: oldCol = "c";
                break;
            case 3: oldCol = "d";
                break;
            case 4: oldCol = "e";
                break;
            case 5: oldCol = "f";
                break;
            case 6: oldCol = "g";
                break;
            case 7: oldCol = "h";
                break;
            case 8: oldCol = "i";
                break;
            case 9: oldCol = "j";
                break;
            default:
                System.out.println("Invalid col position");
                break;
        }
        switch(y){
            case 0: oldRow = 9;
                break;
            case 1: oldRow = 8;
                break;
            case 2: oldRow = 7;
                break;
            case 3: oldRow = 6;
                break;
            case 4: oldRow = 5;
                break;
            case 5: oldRow = 4;
                break;
            case 6: oldRow = 3;
                break;
            case 7: oldRow = 2;
                break;
            case 8: oldRow = 1;
                break;
            case 9: oldRow = 0;
                break;
            default:
                System.out.println("Invalid row position");
                break;
        }
        return oldCol + oldRow;
    }


    public Arrow translateArrowIn(String move) {

        char x = move.charAt(0);
        char y = move.charAt(1);

        int oldCol = -1;
        int oldRow = -1;


        switch(x){
            case 'a': oldCol = 0;
                break;
            case 'b': oldCol = 1;
                break;
            case 'c': oldCol = 2;
                break;
            case 'd': oldCol = 3;
                break;
            case 'e': oldCol = 4;
                break;
            case 'f': oldCol = 5;
                break;
            case 'g': oldCol = 6;
                break;
            case 'h': oldCol = 7;
                break;
            case 'i': oldCol = 8;
                break;
            case 'j': oldCol = 9;
                break;
            default:
                System.out.println("Invalid col position");;
                break;
        }
        switch(y){
            case '0': oldRow = 9;
                break;
            case '1': oldRow = 8;
                break;
            case '2': oldRow = 7;
                break;
            case '3': oldRow = 6;
                break;
            case '4': oldRow = 5;
                break;
            case '5': oldRow = 4;
                break;
            case '6': oldRow = 3;
                break;
            case '7': oldRow = 2;
                break;
            case '8': oldRow = 1;
                break;
            case '9': oldRow = 0;
                break;
            default:
                System.out.println("Invalid row position");;
                break;

        }
        Arrow a = new Arrow(oldRow, oldCol);
        return a;
    }
}