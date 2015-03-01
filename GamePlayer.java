import net.n3.nanoxml.IXMLElement;
import ubco.ai.connection.ServerMessage;
import ubco.ai.games.GameClient;
import ubco.ai.games.GameMessage;

import java.util.Scanner;

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
		BoardLogic bl = new BoardLogic(true);
		
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
		
		String msg = "";
		msg = "<action type='"+GameMessage.MSG_CHAT+"'>"+ "Testing a chat message yo" +"</action>";
		String messageToSend = ServerMessage.compileGameMessage(GameMessage.MSG_CHAT, 8, msg);
		gameClient.sendToServer(messageToSend);
	}
	
	//general message from the server
	public boolean handleMessage(String msg) throws Exception {
		System.out.println("Game message: "+msg);
		return true;
	}

	//game-specific message from the server
	public boolean handleMessage(GameMessage gmsg) throws Exception {
		IXMLElement xml = ServerMessage.parseMessage(gmsg.msg); 
		String xmlType = xml.getAttribute("type", null);
		System.out.println("Message Type: " + xmlType);
		
		switch(xmlType) {
		case GameMessage.ACTION_GAME_START:
			System.out.println("Message Type: ACTION_GAME_START");
			
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
	 * Method that sends messages (chat or movement)
	 */
	public void sendToServer(String msgType, int roomID){
		String actionMsg = "<action type='";
		actionMsg += msgType + "'>";
		Scanner in = new Scanner(System.in);
		
		if (msgType == "move") {						// If the action is a move type, asks for info on move of queen and arrow
			actionMsg += "<queen move='";
			System.out.println("Move from where to where? (in form start-end: ");
			String move = in.nextLine();
			actionMsg += move + "'></queen><arrow move='";
			
			System.out.println("Throw arrow to where?: ");
			String arrow = in.nextLine();
			actionMsg += arrow + "'></arrow>";
			
		}
		else if (msgType == "chat") {				// If action is chat, ask for input of chat message
			System.out.println("Chat:  ");
			String message = in.nextLine();
			actionMsg += message;			
		}
		actionMsg += "</action>";
		System.out.println("Message that is going : "+actionMsg);
		String msg = ServerMessage.compileGameMessage(GameMessage.MSG_GAME, roomID, actionMsg);		// GET ID OF ROOM
		gameClient.sendToServer(msg, true);
	}
}