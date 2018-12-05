package com.hw4.networks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.hw4.sketcher.Color;
import com.hw4.sketcher.Enemy;
import com.hw4.sketcher.GameObject;
import com.hw4.sketcher.Movable;
import com.hw4.sketcher.Player;
import com.hw4.sketcher.Renderable;
import com.hw4.sketcher.Scorer;
import com.hw4.sketcher.SpawnPoint;

import processing.core.PApplet;

/*
 * This is run as a thread for handling reads the Game objects from client 
 * and add those Game Objects into a Thread Safe Array list. 
 * 
 * */
class ClientRequestHandler implements Runnable {

	ConcurrentMap<String, Player> playerMap;
	ConcurrentMap<String, GameObject> scene;
	Socket socket;
	PApplet sketcher;
	int playerDiameter = 20;

	public ClientRequestHandler(PApplet sketcher, Socket socket, ConcurrentMap<String, GameObject> scene,
			ConcurrentMap<String, Player> playerMap) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.playerMap = playerMap;
		this.sketcher = sketcher;
		this.scene = scene;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int port = socket.getPort();
		Player player = null;
		GameObject bullet = null;

		try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
			// This player object is corresponding to one thread
			// (i.e) one particular player

			while (true) {
				String input = inputStream.readUTF();
				String playerVals[] = input.split("~");
				int move_x = Integer.parseInt(playerVals[0]);
				int move_y = Integer.parseInt(playerVals[1]);
				if (player == null) {
					player = new Player(sketcher, 0, 0, playerDiameter, Color.getRandomColor());
					player.clientId = socket.getPort();
					new SpawnPoint(sketcher, player);
					// add the player to the map with the UUID sent from the client
					playerMap.put(player.GAME_OBJECT_ID, player);
				}
				player.setMovement(move_x, move_y);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			player.kill();
			System.out.println("Client [" + port + "] Request handler is closed");
		}
	}

}

/*
 * This is run as a thread for handling writes to the client. This sends a
 * counter to the client for every two seconds.
 * 
 */
class ClientResponseHandler implements Runnable {
	Socket socket;
	ConcurrentMap<String, GameObject> scene;
	ConcurrentMap<String, Player> playerMap;

	public ClientResponseHandler(Socket socket, ConcurrentMap<String, GameObject> scene,
			ConcurrentMap<String, Player> playerMap) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.scene = scene;
		this.playerMap = playerMap;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int port = socket.getPort();
		try (DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())) {
			StringBuffer buffer = new StringBuffer();
			Scorer scorer = new Scorer();
			while (true) {
				// send all scene objects and player objects to all clients
				for (GameObject gameObject : scene.values()) {
						buffer.append(gameObject.toGameObjectString() + "~~");

					// If the game Object is of type space Invaders we add the enemies rather than
					// the
					// space invaders

				}
				// System.out.println("scene : "+playerMap.values().size());
				for (Player gameObject : playerMap.values()) {
					buffer.append(gameObject.toGameObjectString() + "~~");
					if (gameObject.clientId == socket.getPort()) // if this is the player of this socket
					{
						gameObject.updateScorer(scorer);
						if (!gameObject.isAlive())
							scorer.alive = false;
						// System.out.println("scorer"+scorer.toGameObjectString());
						buffer.append(scorer.toGameObjectString() + "~~");
					}
				}
				outputStream.writeUTF(buffer.toString());
				buffer.delete(0, buffer.length());
				outputStream.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client [" + port + "]Response handler is closed");
		}
	}

}

/*
 * This is the thread that takes care of handling new connections to the server.
 * This in turn runs two new thread for each connection established with the
 * server. one for write data and another for reading data.
 * 
 */
class ClientConnectionHandler implements Runnable {
	ConcurrentMap<String, GameObject> scene;
	ConcurrentMap<String, Player> playerMap;
	PApplet sketcher;

	public ClientConnectionHandler(PApplet sketcher, ConcurrentMap<String, GameObject> scene,
			ConcurrentMap<String, Player> playerMap) {
		// TODO Auto-generated constructor stub
		this.scene = scene;
		this.playerMap = playerMap;
		this.sketcher = sketcher;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		ServerSocket sSocket;
		try {
			sSocket = new ServerSocket(15001);
			System.out.println("server listening at " + sSocket.getLocalPort());
			while (true) {
				// this is an unique socket for each client
				Socket socket = sSocket.accept();
				System.out.println("Client with port number: " + socket.getPort() + " is connected");
				// start a new thread for handling requests from the client
				ClientRequestHandler requestHandler = new ClientRequestHandler(sketcher, socket, scene, playerMap);
				new Thread(requestHandler).start();
				// start a new thread for handling responses for the client
				ClientResponseHandler responseHandler = new ClientResponseHandler(socket, scene, playerMap);
				new Thread(responseHandler).start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

}

/**
 * @author ragbalak This is the main thread that takes care of the game loop.
 *         The Game loop access the game objects that are received from the
 *         client and renders then on the screen.
 */
public class GameServer extends PApplet {

	static int noOfEnemyRows = 6;
	static int noOfEnemyCols = 10;

	static int width = 800, height = 800;
	ConcurrentMap<String, GameObject> scene;
	ConcurrentMap<String, Player> playerMap;
	boolean flag = true;
	/*
	 * Let us construct 5 platforms of different colors three platforms has to move
	 * and two are static Adding Platforms
	 */

	public GameServer(ConcurrentMap<String, GameObject> scene, ConcurrentMap<String, Player> playerMap) {
		// TODO Auto-generated constructor stub
		this.scene = scene;
		this.playerMap = playerMap;
		// Default tic is at 60 meaning 60 frames in a sec
	}

	@Override
	public void setup() {
		System.out.println("setup");
		createScene(scene);
		new Thread(new ClientConnectionHandler(this, scene, playerMap)).start();

	}

	@Override
	public void settings() {
		// TODO Auto-generated method stub
		System.out.println("settings");
		size(800, 800);
	}

	@Override
	public void draw() {
		tick();
		render();
	}

	public void render() {
		// Render Block
		background(0);
		// scene
		for (GameObject gameObject : scene.values()) {
			if (gameObject instanceof Renderable)
				((Renderable) gameObject).render();
		}
		// Player
		for (Player player : playerMap.values()) {
			player.render();
		}
	}

	// tick takes care of updates to the objects
	// tick is called only when a certain time is elapsed
	// tick also reduces the delta
	public void tick() {

		Iterator<GameObject> sceneIterator = scene.values().iterator();

		while (sceneIterator.hasNext()) {
			GameObject gameObject = sceneIterator.next();
			if (gameObject instanceof Movable)
				((Movable) gameObject).step();
		}
		// Player
		for (Player player : playerMap.values()) {
			// The events are generated within the player class on step and collision
			player.step();
			player.isHit(scene.values());
		}
	}

	public void createScene(ConcurrentMap<String, GameObject> scene) {
		int[] pos = SpawnPoint.getRandomPos(this);
		GameObject enemy = new Enemy(this, pos[0], pos[1]);
		// The space invaders added in the scene will be sent to the client
		// as distinct enemies and not the entire space invaders object
		scene.put(enemy.GAME_OBJECT_ID, enemy);
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		ConcurrentMap<String, GameObject> scene = new ConcurrentHashMap<>();
		ConcurrentMap<String, Player> playerMap = new ConcurrentHashMap<>();
		GameServer mySketch = new GameServer(scene, playerMap);
		PApplet.runSketch(processingArgs, mySketch);
	}
}
