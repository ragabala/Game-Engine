package com.hw2.performance.objectbased;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hw2.sketcher.Color;
import com.hw2.sketcher.DeathZone;
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
					int[] pos = Player.spawnPlayerPosition(sketcher);
					player = new Player(sketcher, pos[0], pos[1], playerDiameter, Color.getRandomColor());
				}
				player.setMovement(move_x, move_y);
				// add the player to the map with the UUID sent from the client
				playerMap.put(player.GAME_OBJECT_ID, player);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	CopyOnWriteArrayList<GameObject> scene;
	ConcurrentMap<String, Player> playerMap;

	public ClientResponseHandler(Socket socket, CopyOnWriteArrayList<GameObject> scene,
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
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				// send all scene objects and player objects to all clients
				for (GameObject gameObject : scene) {
					if (!(gameObject instanceof DeathZone))
						outputStream.writeObject(gameObject);
				}
				for (GameObject gameObject : playerMap.values()) {
					outputStream.writeObject(gameObject);
				}
				outputStream.reset();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	CopyOnWriteArrayList<GameObject> scene;
	ConcurrentMap<String, Player> playerMap;
	PApplet sketcher;

	public ClientConnectionHandler(PApplet sketcher, CopyOnWriteArrayList<GameObject> scene,
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
		}
	}

}

/**
 * @author ragbalak This is the main thread that takes care of the game loop.
 *         The Game loop access the game objects that are received from the
 *         client and renders then on the screen.
 */
public class GameServer extends PApplet {

	static int width = 800, height = 800;
	CopyOnWriteArrayList<GameObject> scene;
	ConcurrentMap<String, Player> playerMap;
	int noOfPlatforms, noOfMovingPlatforms;
	/*
	 * Let us construct 5 platforms of different colors three platforms has to move
	 * and two are static Adding Platforms
	 */

	public GameServer(CopyOnWriteArrayList<GameObject> scene, ConcurrentMap<String, Player> playerMap,
			int noOfPlatforms, int noOfMovingPlatforms) {
		// TODO Auto-generated constructor stub
		this.scene = scene;
		this.playerMap = playerMap;
		this.noOfPlatforms = noOfPlatforms;
		this.noOfMovingPlatforms = noOfMovingPlatforms;
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
		for (GameObject gameObject : scene) {
			if (gameObject instanceof Renderable)
				((Renderable) gameObject).render();
			if (gameObject instanceof Movable)
				((Movable) gameObject).step();
		}
		// Player
		for (GameObject gameObject : playerMap.values()) {
			((Renderable) gameObject).render();
			((Movable) gameObject).step();
		}
		// collision check
		for (GameObject gameObject : scene)
			for (GameObject player : playerMap.values())
				((Player) player).isConnected(gameObject);
	}

	public void createScene(CopyOnWriteArrayList< GameObject> scene) {
		float _temp_x = (float) (width * 0.7) / noOfPlatforms;
		float _temp_y = (float) (height * 0.7) / noOfPlatforms;
		int[][] movements = {{0,1},{1,0}};
		int iter = 1;
		for (int i = 1; i <= noOfPlatforms; i++) {
			int x_pos = (int) random(_temp_x * i, _temp_x * (i + 1));
			int y_pos = (int) random(_temp_y * i, _temp_y * (i + 1));
			Platform temp = new Platform(this, x_pos, y_pos, 60, 10, Color.getRandomColor());
			if(iter <= noOfMovingPlatforms)
			{
				int move = iter%2;
				temp.setMotion(movements[move][0], movements[move][1]);
				iter++;
			}
			scene.add(temp);
		}
		Floor temp = new Floor(this, height, width);
		scene.add(temp);
		DeathZone deathZone = new DeathZone(height, width);
		scene.add(deathZone);

	}

	public static void main(String[] args) {
		int noOfPlatforms = 0, noOfMovingPlatforms = 0;
		String[] processingArgs = { "MySketch" };
		System.out.println("Number of Arguments passed : " + args.length);
		if (args.length < 2) {
			System.err.println(
					"The Game server should be invoked with the following parameters <# Of Platforms> <# Of moving platforms>");
			System.exit(0);
		}
		noOfPlatforms = Integer.parseInt(args[0]);
		noOfMovingPlatforms = Integer.parseInt(args[1]);
		if (noOfPlatforms < noOfMovingPlatforms) {
			System.err.println("# Of moving platforms should be lesser than # of platforms");
			System.exit(0);
		}

		CopyOnWriteArrayList<GameObject> scene = new CopyOnWriteArrayList<>();
		ConcurrentMap<String, Player> playerMap = new ConcurrentHashMap<>();
		GameServer mySketch = new GameServer(scene, playerMap, noOfPlatforms, noOfMovingPlatforms);
		PApplet.runSketch(processingArgs, mySketch);

	}

}
