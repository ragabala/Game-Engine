package com.hw3.networks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.hw3.eventManager.Event;
import com.hw3.eventManager.types.UserInputEvent;
import com.hw3.sketcher.Color;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Player;
import com.hw3.sketcher.Renderable;

import processing.core.PApplet;

/**
 * @author ragbalak ShapeSender class is a Thread impl, which takes of sending
 *         Game objects to the server from the client for every 2 secs.
 */
class PlayerSender implements Runnable {

	Socket socket;
	String[] playerString;


	public PlayerSender(Socket socket, String[] playerString) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.playerString = playerString;
	}

	@Override
	public void run() {
		try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
			while (true) {
				String s = playerString[0]+"~"+playerString[1]+"~"+playerString[2]+"~"+playerString[3];
				outputStream.writeUTF(s);		
				
			}
		} catch (IOException e1) {
			System.out.println("Closing Sender thread in client");
		}
	}

}

/**
 * @author ragbalak Clientreceiver class is another Thread Implementation that
 *         takes care of receiving the counter values form the server. This runs
 *         async with the Shape Sender.
 */
class ClientReceiver implements Runnable {
	Socket socket;
	ConcurrentMap<String, GameObject> gameObjects;
	PApplet sketcher;
	Player playerCurrentPlayer;
	
	public ClientReceiver(PApplet sketcher, Socket socket, ConcurrentMap<String, GameObject> gameObjects, Player current) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.gameObjects = gameObjects;
		this.sketcher = sketcher;
		playerCurrentPlayer = current;
	}

	@Override
	public void run() {
		try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
			GameObject temp = null;
			while (true) {
				String inputObtained = inputStream.readUTF();
				String[] inputVals = inputObtained.split("~~");
				for (String gameObjectInput : inputVals) {
					String[] gameObjectVals = gameObjectInput.split("~");
					String gameGUID = gameObjectVals[1];
					
					// If the object returned is the player object
					if(playerCurrentPlayer.GAME_OBJECT_ID.equals(gameGUID))
					{
						
						// if there is no update dont update
						if(playerCurrentPlayer.x_pos == Integer.parseInt(gameObjectVals[2])&&playerCurrentPlayer.y_pos == Integer.parseInt(gameObjectVals[3]))
							continue;
						playerCurrentPlayer.updateGameObject(gameObjectVals);
						if(gameObjectVals[9] != null)
						{
							if(gameObjects.containsKey(gameObjectVals[9]))
								playerCurrentPlayer.connectedObject = gameObjects.get(gameObjectVals[9]);
						}
						continue;
					}			
					// For other objects .. We create the object in the client and just update positions based on the 
					// string sent from the server
					// This makes it Pure Distributed system.
					if (gameObjects.containsKey(gameGUID)) {
						temp = gameObjects.get(gameGUID);
						temp.updateGameObject(gameObjectVals);
					} else {
						temp = GameObject.parseGameString(gameObjectVals);
						temp.setSketcher(sketcher);
						gameObjects.put(gameGUID, temp);
					}
				}
			}
		} catch (IOException e1) {
			System.out.println("Closing receiver thread in client");
		}
	}

}

/**
 * @author ragbalak The main client thread that spawns the sender and receiver
 *         threads.
 */
public class GameClient {

	public static void main(String[] args) throws IOException, InterruptedException {
		String[] processingArgs = { "MySketch" };
		Game mySketch = new Game();
		PApplet.runSketch(processingArgs, mySketch);
	}
}

class Game extends PApplet {
	ConcurrentMap<String, GameObject> gameObjects;
	String[] playerString= {"0","0","0","0"};
	int playerDiameter = 30;
	int[] keys = { 0, 0 ,0 };
	int[] prev = {0,0,0};
	String playerUUID;
	Player player;			
	Event input ;
	
	
	@Override
	public void setup() {
		// TODO Auto-generated method stub
		// Adding Current Player to the gameObjects
		gameObjects = new ConcurrentHashMap<>();
		// This player is just used for converting it into string
		// The actual player creation happens in the server
		// Also we are adding a new parameter for adding the user actions
		// like pause/unpause ; record/unrecord ; replay(various speeds) etc
		player = new Player(this, (int)random(width*0.1f, width*0.9f),(int)random(height*0.1f, height*0.9f), playerDiameter, Color.getRandomColor());
		// In the distributed system the client takes care of 
		// input events there by reducing the load of stepping clients in the server
		// The server just takes care of collision handling event, death, spawn events
		
		// Since we need to know the player object that is sent to the server and returned back
		// we save the player game object id and use it for creation in server as well
		
		playerString[0] = player.x_pos+"";
		playerString[1] = player.y_pos+"";
		playerString[2] = 0+"";
		playerString[3] = player.GAME_OBJECT_ID;
		
		System.out.println("created player : "+playerString);
		Socket socket;
		try {
			socket = new Socket("127.0.0.1", 15001);
			System.out.println("Client to server socket established");
			Thread sender = new Thread(new PlayerSender(socket, playerString));
			Thread receiver = new Thread(new ClientReceiver(this, socket, gameObjects, player));
			sender.start();
			receiver.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void settings() {
		// TODO Auto-generated method stub
		size(800, 800);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		background(0);
		// System.out.println("gameObjects"+gameObjects.size());
		
		if(prev[0] != keys[0] || prev[1] != keys[1])
			input = new UserInputEvent(keys[0],keys[1], player);
		
		
		for (GameObject gameObject : gameObjects.values()) {
			if (gameObject instanceof Renderable)
				((Renderable) gameObject).render();
		}
		
		player.step(keys[0], keys[1]);
		player.render();	
		// This ensures the players new position is updated to the server
		playerString[0] = player.x_pos+"";
		playerString[1] = player.y_pos+"";
		playerString[2] = keys[2]+"";
		playerString[3] = player.GAME_OBJECT_ID;
	}

	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		if (keyCode == RIGHT)
			keys[0] = 1;
		else if (keyCode == LEFT)
			keys[0] = -1;
		else if (keyCode == 32)
			keys[1] = 1;

		// for pause, unpause, record, stoprecord, play 0.5x, play 1x, play 2x
		// p : pause/ unpause
		// r: record / stoprecord
		// j: play 0.5x
		// k: play 1x
		// l: play 2x

		// Pause 'p|P'
		else if (keyCode == 80 || keyCode == 112)
			keys[2] = 1;
		
		// Record 'r|R'
		else if (keyCode == 82 || keyCode == 114)
			keys[2] = 2;
		// play 0.5x 'j|J'
		else if (keyCode == 74 || keyCode == 106)
			keys[2] = 3;
		// play 1x 'k|K'
		else if (keyCode == 75 || keyCode == 107)
			keys[2] = 4;
		
		// play 2x 'l|L'
		else if (keyCode == 76 || keyCode == 108)
			keys[2] = 5;

		// unpause 'u|U'
		else if (keyCode == 85 || keyCode == 117)
			keys[2] = 6;
		
		//stop record  's|S'
		else if (keyCode == 83 || keyCode == 115)
			keys[2] = 7;
	}

	@Override
	public void keyReleased() {
		// TODO Auto-generated method stub
		if (keyCode == RIGHT)
			keys[0] = 0;
		else if (keyCode == LEFT)
			keys[0] = 0;
		else if (keyCode == 32)
			keys[1] = 0;
		//else if (  keyCode == 80 || keyCode == 112 || keyCode == 82 || keyCode == 114 || keyCode == 74 || keyCode == 106
			//	|| keyCode == 75 || keyCode == 107 || keyCode == 76 || keyCode == 108 || keyCode == 85 || keyCode == 117 
				//|| keyCode == 83 || keyCode == 115)
			keys[2] = 0;
	}
}
