import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    String teamName = "";

    /*
     * Constructor
     */
    public GamePlayer(String name, String passwd) {

        //A player has to maintain an instance of GameClient, and register itself with the
        //GameClient. Whenever there is a message from the server, the Gameclient will invoke
        //the player's handleMessage() method.
        bl = new BoardLogic(true);

        //Three arguments: user name (any), passwd (any), this (delegate)
        teamName = name;

        gameClient = new GameClient(name, passwd, this);
        System.out.printf(getRooms());
        joinRoom();

        SuccessorFunction sf = new SuccessorFunction();

        bl = sf.getSuccessors(bl).get(0);
        System.out.println("\nFirst Successor\n"+bl.toString());
//
//        MinKingDistHeuristic h = new MinKingDistHeuristic(bl);
//        h.calculate();
//        System.out.println("Owned by Us: "+h.ownedByUs);
//        System.out.println("Owned by Them: "+h.ownedByThem);
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
        gameClient.joinGameRoom(in.nextLine());

        String msg = "";
        msg = "<action type='" + GameMessage.MSG_CHAT + "'>" + "Testing a chat message yo" + "</action>";
        String messageToSend = ServerMessage.compileGameMessage(GameMessage.MSG_CHAT, 8, msg);
        gameClient.sendToServer(messageToSend);
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
                teamName = "1";                // the GamePlayer Name (should be the same as name in Run.java) CHANGE NAME L8R
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
                System.out.println("Team: "+teamName+" is Role: " + teamRole);
                break;
            case GameMessage.ACTION_MOVE:
                System.out.println("Message Type: ACTION_MOVE");
                IXMLElement queen = xml.getChildAtIndex(0);
                String queen_move = queen.getAttribute("move", null);            // queen_move is the value of the opponent's move
                IXMLElement arrow = xml.getChildAtIndex(1);
                String arrow_move = arrow.getAttribute("move", null);            // arrow_move is the value of the opponent's arrow
                System.out.println("Queen Move: " + queen_move);
                System.out.println("Arrow Move: " + arrow_move);
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
                System.out.println("Message Type: MSG_GAME");
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
    }
}