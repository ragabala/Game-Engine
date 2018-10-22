package com.hw2.networks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;
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
	GameObject player;

	public PlayerSender(Socket socket, GameObject player) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.player = player;
	}

	@Override
	public void run() {
		ObjectOutputStream outputStream;
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				outputStream.writeObject(player);
				outputStream.reset();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
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
	ConcurrentMap<UUID, GameObject> gameObjects;
	PApplet sketcher;

	public ClientReceiver(PApplet sketcher, Socket socket, ConcurrentMap<UUID, GameObject> gameObjects) {
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
				gameObjects.put(temp.GAME_OBJECT_ID, temp);
				// generate a shape every 2 secs

			}
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
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
	ConcurrentMap<UUID, GameObject> gameObjects;
	Player player;
	int playerDiameter = 30;
	int[] keys = { 0, 0 };


	@Override
	public void setup() {
		// TODO Auto-generated method stub
		// Adding Current Player to the gameObjects
		gameObjects = new ConcurrentHashMap<>();
		player = new Player(this, (int) random(10, width), (int) random(10, height), playerDiameter);
		gameObjects.put(player.GAME_OBJECT_ID, player);
		Socket socket;
		try {
			socket = new Socket("127.0.0.1", 15001);
			System.out.println("Client to server socket established");
			Thread sender = new Thread(new PlayerSender(socket, player));
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
		for (GameObject gameObject : gameObjects.values()) {
			if (gameObject instanceof Renderable)
				((Renderable) gameObject).render();
			if (!(gameObject instanceof Player))
				player.isConnected(gameObject);
		}
		player.step(keys[0], keys[1]);
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
}
