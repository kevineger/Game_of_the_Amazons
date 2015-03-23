import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import net.n3.nanoxml.IXMLElement;
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

    String role;                    // role is the string that holds if we go first (B) or second (W)
    String teamName;
    int roomID;

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
                //find name and associated
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
                role = teamRole;                                // stores our role internally
                System.out.println("We are gameplayer "+ teamName + " and our Role is: " + role);


                // Now, what we create a board logic depending on whether we move first (true) or second (false)
                if (role.equals("W")) {
                    System.out.println("Our Move");                 // message to ourselves saying we are moving first
                    bl = new BoardLogic(true);
                    SuccessorFunction sf = new SuccessorFunction();

                    // instead, we perform a random move
                    bl = randomMove(bl);
                    System.out.println(bl.toString());

                    // now, we would call our heuristic on our successor function, and obtain the board we want to use
//                    bl = sf.getSuccessors(bl).get(0);               // or whatever board we define as the best

                    // get the move from the bl
                    // pass the move to sendToServer
//                    sendToServer(GameMessage.ACTION_MOVE, roomID);
                }
                else {
                    bl = new BoardLogic(false);
                    System.out.println(bl.toString());
                    // now, we will wait for a message from opponent, and deal with it as such
                }
                break;
            case GameMessage.ACTION_MOVE:
                System.out.println("Message Type: ACTION_MOVE");
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
                System.out.println("Message Type: MSG_GAME");
                IXMLElement queen1 = xml.getChildAtIndex(0);
                String queen_move1 = queen1.getAttribute("move", null);            // queen_move is the value of the opponent's move
                IXMLElement arrow1 = xml.getChildAtIndex(1);
                String arrow_move1 = arrow1.getAttribute("move", null);            // arrow_move is the value of the opponent's arrow
                System.out.println("Queen Move: " + queen_move1);
                System.out.println("Arrow Move: " + arrow_move1);

                translateIn(queen_move1);
                translateArrowIn(arrow_move1);
                bl.updateAfterMove();
                System.out.println(bl.toString());
                bl = randomMove(bl);
                bl.updateAfterMove();
                System.out.println(bl.toString());
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


    /*
     * Method that randomly chooses a queen to move, moves that queen, and randomly throws an arrow from that queen
     * BoardLogic b: the current BoardLogic that is representing the game state
     *
     * The strings printed out at the end are in the form (col row) to follow the convention of sending to the server
     *
     * returns: BoardLogic representing the new game state after our random move.
     */
    private BoardLogic randomMove(BoardLogic b) {
        Random r = new Random();
        String originalPos;
        // first we randomly choose a friendly queen, q
        Queen q = b.getFriendly()[r.nextInt(4)];
        System.out.println("Our randomly chosen queen is " + q.toString());

        move m =null;
        // Chooses the random move that queen q will make
        if(b.getLegalMoves(q).size()==0){
            for(Queen e : b.getFriendly()){
                if(b.getLegalMoves(e).size()!=0) {
                    // Since board.getLegalMoves returns a moveData, we need to convert it to a move
                    moveData md = b.getLegalMoves(e).get(r.nextInt(b.getLegalMoves(e).size()));
                    m = new move(md.colPos, md.rowPos, md.Q.colPos, md.Q.rowPos);
                    System.out.println("Our randomly chosen move is " + m);
                    q.move(md.rowPos, md.colPos);
                    break;
                }
            }
        }
        else{
            // Since board.getLegalMoves returns a moveData, we need to convert it to a move
            moveData md = b.getLegalMoves(q).get(r.nextInt(b.getLegalMoves(q).size()));
            m = new move(md.colPos, md.rowPos, md.Q.colPos, md.Q.rowPos);
            System.out.println("Our randomly chosen move is " + m);
            q.move(md.rowPos, md.colPos);
        }

        // then we move q to a random legal place on the board
             // may want to change this to m (move) instead of md (moveData
        bl.updateAfterMove();
        String newPos = "" + q.rowPos + " " + q.colPos;

        // now, choose a random arrowshot that the moved queen can perform
        Arrow a = bl.getArrowShots(q).get(r.nextInt(bl.getArrowShots(q).size()));
        System.out.println("Our Randomly chosen arrow location is: " + a.toString());
        String arrowPos = "" + a.rowPos + " " + a.colPos;

        // finally, we throw an arrow, a, from q's new position
        bl.addArrow(a.rowPos, a.colPos);
        // at last, we update the board and GUI
        bl.updateAfterMove();

        //System.out.println("Original Queen Position: " + originalPos);
        System.out.println("New Queen Position: " + newPos);
        System.out.println("Using the translator:" + translateOut(m));
        System.out.println("New Arrow Location: " + arrowPos);
        System.out.println("Using the translator:" + translateArrowOut(a));

//        try {
//            Thread.sleep(10000);                 //1000 milliseconds is one second.
//        } catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }

        sendToServer(GameMessage.MSG_GAME, roomID, translateOut(m), translateArrowOut(a));
        return b;


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

    public void translateIn(String move){

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

        for (Queen q: bl.getEnemies()) {
            if (q.colPos == oldCol && q.rowPos == oldRow) {
                q.move(newRow, newCol);
            }
        }
        move moveMessage = new move(newCol, newRow, oldCol, oldRow);

        System.out.println(moveMessage.toString());
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
        return oldCol + oldRow;
    }


    public void translateArrowIn(String move) {

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
        bl.addArrow(a);
    }
}