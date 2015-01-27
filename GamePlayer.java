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
	
	
	/*
	 * Constructor 
	 */
	public GamePlayer(String name, String passwd) {
		
		//A player has to maintain an instance of GameClient, and register itself with the  
		//GameClient. Whenever there is a message from the server, the Gameclient will invoke 
		//the player's handleMessage() method.
		
		//Three arguments: user name (any), passwd (any), this (delegate)   
	    gameClient = new GameClient(name, passwd, this);
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
	}
	
	//general message from the server
	public boolean handleMessage(String arg0) throws Exception {
		System.out.println("Game message: "+arg0);
		return true;
	}

	//game-specific message from the server
	public boolean handleMessage(GameMessage arg0) throws Exception {
		IXMLElement xml = ServerMessage.parseMessage(arg0.msg); 
		String xmlType = xml.getAttribute("type", null);
		System.out.println("THE TYPE IS: " + xmlType);
		return true;
	}

    // You may want to implement a method like this as a central point for sending messages 
	// to the server.  
	public void sendToServer(String msgType, int roomID){
	  // before sending the message to the server, you need to (1) build the text of the message 
	  // as a string,  (2) compile the message by calling 
	  // the static method ServerMessage.compileGameMessage(msgType, roomID, actionMsg),
	  // and (3) call the method gameClient.sendToServer() to send the compiled message.
		
	  // For message types and message format, see the GameMessage API and the project notes	
	}
}