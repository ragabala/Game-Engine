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
import com.hw3.eventManager.types.CharacterCollisionEvent;
import com.hw3.eventManager.types.UserInputEvent;
import com.hw3.sketcher.Color;
import com.hw3.sketcher.DeathZone;
import com.hw3.sketcher.Floor;
import com.hw3.sketcher.GameObject;
import com.hw3.sketcher.Movable;
import com.hw3.sketcher.Platform;
import com.hw3.sketcher.Player;
import com.hw3.sketcher.Renderable;

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
	Clock clock;
	int playerDiameter = 20;

	public ClientRequestHandler(PApplet sketcher, Socket socket, ConcurrentMap<String, GameObject> scene,
			ConcurrentMap<String, Player> playerMap, Clock clock) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.playerMap = playerMap;
		this.sketcher = sketcher;
		this.clock = clock;
		this.scene = scene;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int port = socket.getPort();
		Player player = null;
		
		int prev_x = 0, prev_y = 0;
		
		try (DataInputStream inputStream = new DataInputStream(socket.getInputStream())) {
			// This player object is corresponding to one thread
			// (i.e) one particular player

			while (true) {
				String input = inputStream.readUTF();
				String playerVals[] = input.split("~");
				int move_x = Integer.parseInt(playerVals[0]);
				int move_y = Integer.parseInt(playerVals[1]);

				// action is the new parameter that handles the pause/ record / playback
				// features.
				int action = Integer.parseInt(playerVals[2]);
				// This will handle the replays, pause, unpause
				ManageAction.manage(action, clock, scene, playerMap);
				if (player == null) {
					// The player object gets created only during the first iteration.
					int[] pos = Player.spawnPlayerPosition(sketcher);
					player = new Player(sketcher, pos[0], pos[1], playerDiameter, Color.getRandomColor(), clock);
					// add the player to the map with the UUID sent from the client
					playerMap.put(player.GAME_OBJECT_ID, player);
				}
				if(prev_x!=move_x || prev_y!=move_y)
				{
					ManageAction.addInputEvent(move_x, move_y, player, clock);
				}
				player.setMovement(move_x, move_y);
				prev_x = move_x;
				prev_y = move_y;
				
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
	Clock clock;

	public ClientConnectionHandler(PApplet sketcher, ConcurrentMap<String, GameObject> scene,
			ConcurrentMap<String, Player> playerMap, Clock clock) {
		// TODO Auto-generated constructor stub
		this.scene = scene;
		this.playerMap = playerMap;
		this.sketcher = sketcher;
		this.clock = clock;
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
				ClientRequestHandler requestHandler = new ClientRequestHandler(sketcher, socket, scene, playerMap,
						clock);
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

	static int noOfPlatforms = 5;
	static int width = 800, height = 800;
	ConcurrentMap<String, GameObject> scene;
	ConcurrentMap<String, Player> playerMap;
	Clock clock;

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
		clock = new Clock();
		createScene(scene);
		new Thread(new ClientConnectionHandler(this, scene, playerMap, clock)).start();

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
		// update Block - Just updates are based on clock

		// This will just replay all the events according the the timelines
		
		if(Record.events.isEmpty())
			Replay.stopReplay();

		if (Replay.isReplaying()) {
			// If the game is in recording mode, we do not update the current Time
			// This allows us to compute lastUpdatedTime and currentTime tics
			// and compare it with the tics in the events
			// when they match the event is trigged in the replay
			// 60 tics is the default tic size for non replay mode

			Event headEvent = Record.events.peek();
			// System.out.println("Events size" +Record.events.size());
			double ticsGame = Clock.getTics(clock.lastUpdatedTime(), clock.getSystemTime(), 1000/ Clock.DEFAULT_TIC_SIZE);
			double ticsReplay = Clock.getTics(Record.recordingStartTime, headEvent.getTimestamp(), 1000/clock.ticSize);

			// If the current game loop surpasses the event tics play the event
			if (ticsGame >= ticsReplay) {
				System.out.println("Game Tics"+ticsGame);
				System.out.println("Replay Tics"+ticsReplay);
				System.out.println("Event : "+headEvent.getType());
				// This means it is time to execute the current Event
				if (Event.Type.USER_INPUT == headEvent.getType()) {
					UserInputEvent e = ((UserInputEvent) headEvent);
					e.player.setDir(e.x, e.y);
				} else if (Event.Type.CHARACTER_COLLSION == headEvent.getType()) {
					CharacterCollisionEvent e = (CharacterCollisionEvent) headEvent;
					e.collider.landOnObject(e.collided);
				} else if (Event.Type.CHARACTER_DEATH == headEvent.getType()) {

				} else if (Event.Type.CHARACTER_SPAWN == headEvent.getType()) {

				}

				// after executing the event can be removed
				Record.events.poll();

			}

			tock();
			render();

		} else {
			// If not inreplay mode .. Then act normally
			clock.setCurrentTime();
			clock.updateDelta();
			clock.setLastToCurrent();

			if (!clock.isPaused()) {
				while (clock.getdeltaTime() >= clock.getTimeStep()) {
					// System.out.println(clock.getdeltaTime() );
					clock.decrementDelta();
					tick();
				}
			}
			else
				clock.updateDelta();
			render();

		}

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
		for (GameObject gameObject : scene.values()) {
			if (gameObject instanceof Movable)
			{
				((Movable) gameObject).step(); // No events attached to the step of scene objects
			}
		}
		// Player
		for (Player player : playerMap.values()) {
			// The events are generated within the player class on step and collision
			player.step();
			player.resolveCollision(scene.values());
		}
	}

	// tock takes care of updates to the objects
	// from replay function only
	public void tock() {
		for (GameObject gameObject : scene.values()) {
			if (gameObject instanceof Movable)
				((Movable) gameObject).step(); // No events attached to the step of scene objects

		}
		// Player
		for (Player player : playerMap.values()) {
			// The events are generated within the player class on step and collision
			player.step(player.dir_x, player.dir_y);
			player.resolveCollision(scene.values());
		}
	}

	public void createScene(ConcurrentMap<String, GameObject> scene) {
		float _temp_x = (float) (width * 0.7) / noOfPlatforms;
		float _temp_y = (float) (height * 0.7) / noOfPlatforms;
		for (int i = 1; i <= noOfPlatforms; i++) {
			int x_pos = (int) random(_temp_x * i, _temp_x * (i + 1));
			int y_pos = (int) random(_temp_y * i, _temp_y * (i + 1));
			Platform temp = new Platform(this, x_pos, y_pos, 60, 10, Color.getRandomColor(), clock);
			if (i == 1)
				temp.setMotion(0, 1);
			if (i == 1 + (noOfPlatforms / 2))
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
		GameServer mySketch = new GameServer(scene, playerMap);
		PApplet.runSketch(processingArgs, mySketch);
	}
}
