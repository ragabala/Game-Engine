package com.hw1.networks;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hw1.sketcher.Shape;

import processing.core.PApplet;

/**
 * @author ragbalak
 * The clienthandler is a thread impl that handles read and write to 
 * each client. Thus every client connected to the server has its own thread spawned.
 * Each thread is responsible for both sending and receiving part.
 * Counter values are send from the server to the client.
 * Game objects are received by the server from the client.
 *
 */
class ClientHandler implements Runnable {

	static ServerSocket sSocket;
	Socket socket;
	DataOutputStream outputStream;
	ObjectInputStream inputStream;
	CopyOnWriteArrayList<Shape> inputShapeList;

	public ClientHandler(Socket socket, CopyOnWriteArrayList<Shape> inputShapeList) throws IOException {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.inputShapeList = inputShapeList;
		this.outputStream = new DataOutputStream(socket.getOutputStream());
		this.inputStream = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int iter = 0;
		try {
			while (true) {
				// This writes data from the server to client
				outputStream.writeInt(iter++);
				// This reads data from client
				
				Shape readShape = (Shape) inputStream.readObject();
				System.out.println("Got shape from client ["+socket.getPort()+"]");
				// this adds a new shape for the server to render
				inputShapeList.add(readShape);
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

/**
 * @author ragbalak
 * The Server class is the renderer, that executes the main game loop.
 * The game loop is resposible for rendering all the game objects that are
 * collected by the client handler.
 */
public class Server extends PApplet {

	static CopyOnWriteArrayList<Shape> inputShapeList;
	
	
	@Override
	public void settings() {
		size(1024, 1024);
	}
	
	@Override
	public void setup() {


	}
	
	@Override
	public void draw() {
		background(100);
		for (Shape shape : inputShapeList) {
			shape.setSketcher(this);
			shape.render();
		}
	}

	public static void main(String[] args) {
		
		ServerSocket sSocket;
		inputShapeList = new CopyOnWriteArrayList<Shape>();
		String[] processingArgs = { "MySketch" };
		Server mySketch = new Server();
		PApplet.runSketch(processingArgs, mySketch);
		try {
			sSocket = new ServerSocket(15000);
			System.out.println("server listening at " + sSocket.getLocalPort());
			while (true) {
				Socket socket = sSocket.accept();
				System.out.println("Client with port number: " + socket.getPort()+ " is connected");
				ClientHandler newClient = new ClientHandler(socket, inputShapeList);
				new Thread(newClient).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
