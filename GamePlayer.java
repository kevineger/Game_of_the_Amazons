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
    TranslateMove translate = new TranslateMove();

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
        frame = new Board();
        frame.setVisible(true);
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
                    T = new SearchTree(new SearchNode(bl));
                    this.makeMove();
                    // now, we would call our heuristic on our successor function, and obtain the board we want to use
                    // get the move from the bl
                    // pass the move to sendToServer
                }
                else {
                    bl = new BoardLogic(false);
                    T = new SearchTree(new SearchNode(bl));
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

                move queenMove = translate.translateIn(queen_move1);
                Arrow arrow = translate.translateArrowIn(arrow_move1);
                frame.update(queenMove, arrow);
                T.getRoot().B.updateAfterMove();

                System.out.println(bl.toString());
                bl.updateAfterMove();
                System.out.println(bl.toString());

                T.makeMoveOnRoot(queenMove, arrow);
                frame.update(queenMove, arrow);
                System.out.println(T.getRoot().B.toString());
                this.makeMove();
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

        String qMove = this.translate.translateOut(made);
        String aMove = this.translate.translateArrowOut(arrowShot);

        this.sendToServer(GameMessage.MSG_GAME, roomID,qMove, aMove);
    }

}