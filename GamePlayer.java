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

    String role;                    // role is the string that holds if we go first (B) or second (W)
    String teamName;
    int roomID;

    /*
     * Constructor
     */
    public GamePlayer(String name, String passwd) {

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
                String teamRole = "";

                IXMLElement firstChild = xml.getChildAtIndex(0);
                IXMLElement[] grandchildren = new IXMLElement[3];
                grandchildren[0] = firstChild.getChildAtIndex(0);
                grandchildren[1] = firstChild.getChildAtIndex(1);

                for (int i = 0; i < grandchildren.length; i++) {            // gets our role (either 'B' or 'W')
                    if (grandchildren[i].getAttribute("name", null).equals(teamName)) {
                        teamRole = grandchildren[i].getAttribute("role", null);
                        break;
                    }
                }
                role = teamRole;                                // stores our role internally
                System.out.println("We are gameplayer "+ teamName + " and our Role is: " + role);

                if (role.equals("W")) {
                    System.out.println("Our Move");
                    bl = new BoardLogic(true);
                    SuccessorFunction sf = new SuccessorFunction();

                    // instead, we perform a random move
                    randomMove(bl);
                    // now, we would call our heuristic on our successor function, and obtain the board we want to use

                    // pass the move to sendToServer
//                    sendToServer(GameMessage.ACTION_MOVE, roomID);
                }
                else {
                    bl = new BoardLogic(false);
                    System.out.println(bl.toString());
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

                translateIn(queen_move1);
                translateArrowIn(arrow_move1);
                System.out.println("We are gameplayer "+ teamName + " and our Role is: " + role);
                System.out.println("Board after opponents move: ");
                System.out.println(bl.toString());
                boolean end = randomMove(bl);            // for using our random player
                if (end) {
                    System.out.println();
                    System.out.println("GAME OVER");
                    System.out.println();
                }
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
     * arrow_move should be in the format h3    (h3 is the (column,row) of the arrow to be placed)
     */
    public void sendToServer(String msgType, int roomID, String queen_move, String arrow_move) {
        String actionMsg = "<action type='";
        actionMsg += msgType + "'>";
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
     * returns: BoardLogic representing the new game state after our random move.
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

//        try {
//            Thread.sleep(10000);                 //1000 milliseconds is one second.
//        } catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }

        sendToServer(GameMessage.MSG_GAME, roomID, translateOut(m), translateArrowOut(a));
        System.out.println("We are gameplayer "+ teamName + " and our Role is: " + role);
        System.out.print("Board after our move: ");
        System.out.println(b.toString());
        System.out.println();
        return b.goalTest();
    }

     /*
      * Method to translate a queen's move to the server (from row,col to a3-g3 syntax)
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
     * Method to translate move from server (from a3-g3 to row, col)
     * Also updates the board to show what move has occured (by calling updateAfterMove()
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
                System.out.println("Invalid row position");
                break;
        }

        for (Queen q: bl.getEnemies()) {
            if (q.colPos == oldCol && q.rowPos == oldRow) {
                q.move(newRow, newCol);
                bl.updateAfterMove();
            }
        }
        move moveMessage = new move(newCol, newRow, oldCol, oldRow);

        System.out.println(moveMessage.toString());
    }


    /*
     * Method to translate an arrow placement to be used by sendToServer()
     * from (row, col to a3 format)
     */
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


    /*
     * Method to translate an arrow placement from server (from a3 to row, col)
     * Also places the arrow on the board and updates it
     */
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
        bl.updateAfterMove();
    }
}