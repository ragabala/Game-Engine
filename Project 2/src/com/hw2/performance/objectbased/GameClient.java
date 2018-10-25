package com.hw2.performance.objectbased;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.hw2.sketcher.GameObject;
import com.hw2.sketcher.Player;
import com.hw2.sketcher.Renderable;

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
		DataOutputStream outputStream;
		try {
			outputStream = new DataOutputStream(socket.getOutputStream());
			while (true) {
				// This will ensure that the client will send only if there is 
				// a change in its position
				if (playerString.length() > 0)
					outputStream.writeUTF(playerString.toString());
				playerString.setLength(0);
			}
		} catch (IOException e1) {
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
		ObjectInputStream inputStream;
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			while (true) {
				GameObject temp = (GameObject) inputStream.readObject();
				temp.setSketcher(sketcher);
				// This will add both player and other platform objects
				gameObjects.put(temp.GAME_OBJECT_ID, temp);
			}
		} catch (IOException | ClassNotFoundException e1) {
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
	int[] keys = { 0, 0 };
	String playerUUID;

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		// Adding Current Player to the gameObjects
		gameObjects = new ConcurrentHashMap<>();
		// This player is just used for converting it into string
		// The actual player creation happens in the server
		playerString = new StringBuffer("0~0");
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
		for (GameObject gameObject : gameObjects.values()) {
			if (gameObject instanceof Renderable)
				((Renderable) gameObject).render();
			int n = playerString.length();
			playerString.replace(0, n, keys[0] + "~" + keys[1]);
		}

	}

	@Override
	public void keyPressed() {
		// TODO Auto-generated method stub
		if (keyCode == RIGHT)
			keys[0] = 1;
		if (keyCode == LEFT)
			keys[0] = -1;
		if (keyCode == 32)
			keys[1] = 1;
	}

	@Override
	public void keyReleased() {
		// TODO Auto-generated method stub
		if (keyCode == RIGHT)
			keys[0] = 0;
		if (keyCode == LEFT)
			keys[0] = 0;
		if (keyCode == 32)
			keys[1] = 0;
	}

	/*
	 * public void setPlayerRelativeToGameObjects() { // Since the platforms changes
	 * each time through the socket // Setting up the connected object to the
	 * current connected object UUID tempUuid = player.getConnectedObjectID(); if
	 * (tempUuid != null) { GameObject temp = gameObjects.get(tempUuid); if (temp !=
	 * null) player.setConnectedObject(temp); } }
	 */
}
