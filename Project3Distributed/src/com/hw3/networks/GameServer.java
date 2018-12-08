package com.hw3.networks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.hw3.actionmanager.Clock;
import com.hw3.actionmanager.ManageAction;
import com.hw3.actionmanager.Record;
import com.hw3.actionmanager.Replay;
import com.hw3.eventManager.Event;
import com.hw3.eventManager.EventListener;
import com.hw3.eventManager.HandleEventDispatch;
import com.hw3.sketcher.Color;
import com.hw3.sketcher.DeathZone;
import com.hw3.sketcher.Floor;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Movable;
import com.hw3.sketcher.Platform;
import com.hw3.sketcher.Player;
import com.hw3.sketcher.Renderable;
import com.hw3.sketcher.SpawnPoint;

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
	int prev_x,prev_y;

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
		try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
			// This player object is corresponding to one thread
			// (i.e) one particular player

			while (true) {
				String input = inputStream.readUTF();
				if(input.equals(""))
					continue;
				String playerVals[] = input.split("~");
				
				// action is the new parameter that handles the pause/ record / playback
				// features.
				int pos_x = Integer.parseInt(playerVals[0]);
				int shoot = Integer.parseInt(playerVals[1]);
				if (shoot != 0) // If the player has shoot, it has to be added in the scene
					
				
				String playerUUID = playerVals[3];
				// This will handle the replays, pause, unpause

				if (player == null) {
					// The player object gets created only during the first iteration.
					
					player = new Player(sketcher, 0,0, playerDiameter, Color.getRandomColor());
					player.GAME_OBJECT_ID = playerUUID;
					// add the player to the map with the UUID sent from the client
					playerMap.put(player.GAME_OBJECT_ID, player);
					
				}
				// this is the position corresponding to the player 
				//which is updated in the client
				if(pos_x != prev_x || pos_y != prev_y)
					player.setPos(pos_x, pos_y);
				
				prev_x = pos_x;
				prev_y = pos_y;
				
				// The input events are handled by the client 
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
			while (true) {
				// send all scene objects and player objects to all clients
				for (GameObject gameObject : scene.values()) {
					if (!(gameObject instanceof DeathZone))
						buffer.append(gameObject.toGameObjectString() + "~~");
				}
				// System.out.println("scene : "+playerMap.values().size());
				for (Player gameObject : playerMap.values()) {
					buffer.append(gameObject.toGameObjectString() + "~~");
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

	int noOfPlatforms = 0;
	int noOfMovingPlatforms = 0;
	static int width = 800, height = 800;
	ConcurrentMap<String, GameObject> scene;
	ConcurrentMap<String, Player> playerMap;
	boolean flag = true;
	EventListener eventListener = new HandleEventDispatch();

	/*
	 * Let us construct 5 platforms of different colors three platforms has to move
	 * and two are static Adding Platforms
	 */

	public GameServer(ConcurrentMap<String, GameObject> scene, ConcurrentMap<String, Player> playerMap, int numberofPlatforms, int numberOfMovingPlatforms) {
		// TODO Auto-generated constructor stub
		this.scene = scene;
		this.playerMap = playerMap;
		this.noOfPlatforms = numberofPlatforms;
		this.noOfMovingPlatforms = numberOfMovingPlatforms;
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
		// Rendering should happen irrespective of tics
		// Time should be a factor in updating the steps for the player and the platform
		// clock updates reffered from
		// https://en.wikibooks.org/wiki/Guide_to_Game_Development/Theory/Game_logic/Setting_up_ticks_and_frames
		// update Block - Just updates are based on cloc
		// This will just replay all the events according the the timelines
		Clock.setCurrentTime();
		Clock.updateDelta();
		Clock.setLastToCurrent();
		if (Replay.isReplaying() && flag) {
			Clock.setReplayStartTime();
			flag = false;
		}

		while (Clock.getdeltaTime() >= Clock.getTimeStep()) {
			Clock.decrementDelta();
			if (Record.events.isEmpty()) {
				Replay.stopReplay(scene.values(), playerMap.values());
				flag = true;
			}
			if (Replay.isReplaying()) {
				Event headEvent = Record.events.peek();
				double ticsGame = Clock.getTics(Clock.getReplayTime(), Clock.getSystemTime());
				if (ticsGame >= headEvent.getTics()) {
					eventListener.onEvent(headEvent);
					Record.events.remove();
				}

			}
		
			if (!Clock.isPaused() || Replay.isReplaying())
				tick();

		}
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
		long start = System.nanoTime();
		for (GameObject gameObject : scene.values())
			if (gameObject instanceof Movable)
				((Movable) gameObject).step(); // No events attached to the step of scene objects
		// Player
		for (Player player : playerMap.values()) {
			// The events are generated within the player class on step and collision
			// In the distributed model we just do collision events
			// The user input events are handled by the clients
			player.resolveCollision(scene.values());
		}
		System.out.println("Loop update "+(System.nanoTime() - start));
	}

	public void createScene(ConcurrentMap<String, GameObject> scene) {
		float _temp_x = (float) (width * 0.7) / noOfPlatforms;
		float _temp_y = (float) (height * 0.7) / noOfPlatforms;
		for (int i = 1; i <= noOfPlatforms; i++) {
			int x_pos = (int) random(_temp_x * i, _temp_x * (i + 1));
			int y_pos = (int) random(_temp_y * i, _temp_y * (i + 1));
			Platform temp = new Platform(this, x_pos, y_pos, 60, 10, Color.getRandomColor());
			if (i <= noOfMovingPlatforms && i%2 ==0)
				temp.setMotion(0, 1);
			if (i <= noOfMovingPlatforms && i%2 ==1)
				temp.setMotion(1, 0);
			scene.put(temp.GAME_OBJECT_ID, temp);
		}
		Floor temp = new Floor(this, height, width);
		scene.put(temp.GAME_OBJECT_ID, temp);
		DeathZone deathZone = new DeathZone(height, width);
		scene.put(deathZone.GAME_OBJECT_ID, deathZone);

	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		ConcurrentMap<String, GameObject> scene = new ConcurrentHashMap<>();
		ConcurrentMap<String, Player> playerMap = new ConcurrentHashMap<>();
		if(args.length < 2)
		{
			System.out.println("Call with <no of platforms> <no of moving platforms>");
			System.exit(1);
		}
		int noOfPlatforms = Integer.parseInt(args[0]);
		int noOfMovingPlatforms = Integer.parseInt(args[1]);
		GameServer mySketch = new GameServer(scene, playerMap, noOfPlatforms, noOfMovingPlatforms);
		PApplet.runSketch(processingArgs, mySketch);
	}
}
