package com.hw3.networks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Renderable;

import processing.core.PApplet;

/**
 * @author ragbalak ShapeSender class is a Thread impl, which takes of sending
 *         Game objects to the server from the client for every 2 secs.
 */
class PlayerSender implements Runnable {

	Socket socket;
	StringBuffer playerString;
	String playerUUID;

	public PlayerSender(Socket socket, StringBuffer playerString) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.playerString = playerString;
	}

	@Override
	public void run() {
		try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
			while (true) {
				// This will ensure that the client will send only if there is
				// a change in its position
				if (playerString.length() > 0)
					outputStream.writeUTF(playerString.toString());
				playerString.setLength(0);
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

	public ClientReceiver(PApplet sketcher, Socket socket, ConcurrentMap<String, GameObject> gameObjects) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.gameObjects = gameObjects;
		this.sketcher = sketcher;
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
	StringBuffer playerString;
	int playerDiameter = 30;
	int[] keys = { 0, 0 ,0 };
	String playerUUID;
	@Override
	public void setup() {
		// TODO Auto-generated method stub
		// Adding Current Player to the gameObjects
		gameObjects = new ConcurrentHashMap<>();
		// This player is just used for converting it into string
		// The actual player creation happens in the server
		// Also we are adding a new parameter for adding the user actions
		// like pause/unpause ; record/unrecord ; replay(various speeds) etc
		playerString = new StringBuffer("0~0~0");
		Socket socket;
		try {
			socket = new Socket("127.0.0.1", 15001);
			System.out.println("Client to server socket established");
			Thread sender = new Thread(new PlayerSender(socket, playerString));
			Thread receiver = new Thread(new ClientReceiver(this, socket, gameObjects));
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
		for (GameObject gameObject : gameObjects.values()) {
			if (gameObject instanceof Renderable)
				((Renderable) gameObject).render();
		}
		playerString.replace(0, playerString.length(), keys[0] + "~" + keys[1] + "~" + keys[2]);
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
