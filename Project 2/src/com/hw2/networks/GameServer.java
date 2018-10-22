package com.hw2.networks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.hw2.sketcher.Floor;
import com.hw2.sketcher.GameObject;
import com.hw2.sketcher.Movable;
import com.hw2.sketcher.Platform;
import com.hw2.sketcher.Renderable;

import processing.core.PApplet;

/*
 * This is run as a thread for handling reads the Game objects from client 
 * and add those Game Objects into a Thread Safe Array list. 
 * 
 * */
class ClientRequestHandler implements Runnable {

	ConcurrentMap<UUID, GameObject> scene;
	Socket socket;
	PApplet sketcher;

	public ClientRequestHandler(PApplet sketcher, Socket socket, ConcurrentMap<UUID, GameObject> scene) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.scene = scene;
		this.sketcher = sketcher;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			while (true) {
				GameObject gameObject = (GameObject) inputStream.readObject();
				// System.out.println("Got shape from client ["+socket.getPort()+"]");
				gameObject.setClientId(socket.getPort());
				gameObject.setSketcher(sketcher);
				// this adds a new shape for the server to render
				scene.put(gameObject.GAME_OBJECT_ID, gameObject);
			}
		} catch (IOException | ClassNotFoundException e) {
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
	ConcurrentMap<UUID, GameObject> scene;

	public ClientResponseHandler(Socket socket, ConcurrentMap<UUID, GameObject> scene) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.scene = scene;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				// send a counter to the client every two seconds
				for (GameObject gameObject : scene.values()) {
					// Do not write back objects to a client which was send from it.
					if (gameObject.clientId != socket.getPort())
						outputStream.writeObject(gameObject);
				}
				outputStream.reset();
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

	ConcurrentMap<UUID, GameObject> scene;
	PApplet sketcher;

	public ClientConnectionHandler(PApplet sketcher, ConcurrentMap<UUID, GameObject> scene) {
		// TODO Auto-generated constructor stub
		this.scene = scene;
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
				ClientRequestHandler requestHandler = new ClientRequestHandler(sketcher, socket, scene);
				new Thread(requestHandler).start();
				// start a new thread for handling responses for the client
				ClientResponseHandler responseHandler = new ClientResponseHandler(socket, scene);
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
	ConcurrentMap<UUID, GameObject> scene;
	/*
	 * Let us construct 5 platforms of different colors three platforms has to move
	 * and two are static Adding Platforms
	 */

	public GameServer(ConcurrentMap<UUID, GameObject> scene) {
		// TODO Auto-generated constructor stub
		this.scene = scene;
	}

	@Override
	public void setup() {
		createScene(scene);
		new Thread(new ClientConnectionHandler(this, scene)).start();
	}

	@Override
	public void settings() {
		// TODO Auto-generated method stub
		size(800, 800);
	}

	@Override
	public void draw() {
		background(0);
		for (GameObject gameObject : scene.values()) {
			if (gameObject instanceof Renderable)
				((Renderable) gameObject).render();
			if (gameObject instanceof Platform)
				((Movable) gameObject).step();
		}
	}

	public void createScene(ConcurrentMap<UUID, GameObject> scene) {
		float _temp_x = (float) (width * 0.9) / noOfPlatforms;
		float _temp_y = (float) (height * 0.9) / noOfPlatforms;
		for (int i = 0; i < noOfPlatforms; i++) {
			int x_pos = (int) random(_temp_x * i, _temp_x * (i + 1));
			int y_pos = (int) random(_temp_y * i, _temp_y * (i + 1));
			Platform temp = new Platform(this, x_pos, y_pos, 60, 10);
			scene.put(temp.GAME_OBJECT_ID, temp);
			if (i == 0)
				temp.setMotion(1, 0);
			if (i == noOfPlatforms / 2)
				temp.setMotion(0, 1);
		}
		Floor temp = new Floor(this, height, width);
		scene.put(temp.GAME_OBJECT_ID, temp);
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		ConcurrentMap<UUID, GameObject> scene = new ConcurrentHashMap<>();
		GameServer mySketch = new GameServer(scene);
		PApplet.runSketch(processingArgs, mySketch);

	}

}
