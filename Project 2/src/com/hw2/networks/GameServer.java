package com.hw2.networks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.hw2.sketcher.Color;
import com.hw2.sketcher.Floor;
import com.hw2.sketcher.GameObject;
import com.hw2.sketcher.Movable;
import com.hw2.sketcher.Platform;
import com.hw2.sketcher.Player;
import com.hw2.sketcher.Renderable;

import processing.core.PApplet;

/*
 * This is run as a thread for handling reads the Game objects from client 
 * and add those Game Objects into a Thread Safe Array list. 
 * 
 * */
class ClientRequestHandler implements Runnable {

	ConcurrentMap<String, Player> playerMap;
	Socket socket;
	PApplet sketcher;
	int playerDiameter = 20;

	public ClientRequestHandler(PApplet sketcher, Socket socket, ConcurrentMap<String, Player> playerMap) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.playerMap = playerMap;
		this.sketcher = sketcher;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			// This player object is corresponding to one thread
			// (i.e) one particular player
			Player player = null;
			while (true) {
				String playerVals[] = inputStream.readUTF().split("~");
				int move_x = Integer.parseInt(playerVals[0]);
				int move_y = Integer.parseInt(playerVals[1]);
				if (player == null) {
					// The player object gets created only during the first iteration.
					int[] pos = Player.spawnPlayerPosition(sketcher);
					player = new Player(sketcher, pos[0], pos[1], playerDiameter, Color.getRandomColor());
					// add the player to the map with the UUID sent from the client
					playerMap.put(player.GAME_OBJECT_ID, player);
				}
				player.setMovement(move_x, move_y);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public ClientResponseHandler(Socket socket,ConcurrentMap<String, GameObject>  scene,
			ConcurrentMap<String, Player> playerMap) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.scene = scene;
		this.playerMap = playerMap;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			StringBuffer buffer = new StringBuffer();
			while (true) {
				// System.out.println("scene : "+scene.values().size());
				// send all scene objects and player objects to all clients
				for (GameObject gameObject : scene.values()) {
					buffer.append(gameObject.toGameObjectString()+"~~");	
				}
				// System.out.println("scene : "+playerMap.values().size());
				for (GameObject gameObject : playerMap.values()) {
					buffer.append(gameObject.toGameObjectString()+"~~");
				}
				outputStream.writeUTF(buffer.toString());
				buffer.delete(0, buffer.length());
				outputStream.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				ClientRequestHandler requestHandler = new ClientRequestHandler(sketcher, socket, playerMap);
				new Thread(requestHandler).start();
				// start a new thread for handling responses for the client
				ClientResponseHandler responseHandler = new ClientResponseHandler(socket, scene, playerMap);
				new Thread(responseHandler).start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

/**
 * @author ragbalak This is the main thread that takes care of the game loop.
 *         The Game loop access the game objects that are received from the
 *         client and renders then on the screen.
 */
public class GameServer extends PApplet {

	static int noOfPlatforms = 5;
	static int width = 800, height = 800;
	ConcurrentMap<String, GameObject> scene;
	ConcurrentMap<String, Player> playerMap;
	/*
	 * Let us construct 5 platforms of different colors three platforms has to move
	 * and two are static Adding Platforms
	 */

	public GameServer(ConcurrentMap<String, GameObject> scene, ConcurrentMap<String, Player> playerMap) {
		// TODO Auto-generated constructor stub
		this.scene = scene;
		this.playerMap = playerMap;
	}

	@Override
	public void setup() {
		createScene(scene);
		new Thread(new ClientConnectionHandler(this, scene, playerMap)).start();
	}

	@Override
	public void settings() {
		// TODO Auto-generated method stub
		size(800, 800);
	}

	@Override
	public void draw() {
		background(0);
		// scene
		for (GameObject gameObject : scene.values()) {
		if (gameObject instanceof Renderable)
				((Renderable) gameObject).render();
			if (gameObject instanceof Movable)
				((Movable) gameObject).step();
		}
		// Player
		for (Player player : playerMap.values()) {
			player.render();
			player.step();
			player.resolveCollision(scene.values());
		}
	}

	public void createScene(ConcurrentMap<String, GameObject> scene) {
		float _temp_x = (float) (width * 0.9) / noOfPlatforms;
		float _temp_y = (float) (height * 0.9) / noOfPlatforms;
		for (int i = 0; i < noOfPlatforms; i++) {
			int x_pos = (int) random(_temp_x * i, _temp_x * (i + 1));
			int y_pos = (int) random(_temp_y * i, _temp_y * (i + 1));
			Platform temp = new Platform(this, x_pos, y_pos, 60, 10, Color.getRandomColor());
			if (i == 0)
				temp.setMotion(1, 0);
			if (i == noOfPlatforms / 2)
				temp.setMotion(0, 1);
			scene.put(temp.GAME_OBJECT_ID, temp);
		}
		Floor temp = new Floor(this, height, width);
		scene.put(temp.GAME_OBJECT_ID, temp);
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		ConcurrentMap<String, GameObject> scene = new ConcurrentHashMap<>();
		ConcurrentMap<String, Player> playerMap = new ConcurrentHashMap<>();
		GameServer mySketch = new GameServer(scene, playerMap);
		PApplet.runSketch(processingArgs, mySketch);

	}

}
