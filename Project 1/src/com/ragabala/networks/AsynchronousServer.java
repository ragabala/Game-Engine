package com.ragabala.networks;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ragabala.sketcher.Color;
import com.ragabala.sketcher.Shape;

import processing.core.PApplet;

/*
 * This is run as a thread for handling reads from client and writing it into the 
 * 
 * */
class ClientRequestHandler implements Runnable {

	Socket socket;
	CopyOnWriteArrayList<Shape> inputShapeList;
	PApplet sketcher;

	public ClientRequestHandler(Socket socket, CopyOnWriteArrayList<Shape> inputShapeList, PApplet sketcher) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.inputShapeList = inputShapeList;
		this.sketcher = sketcher;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			while (true) {
				Shape readShape = (Shape) inputStream.readObject();
				readShape.setSketcher(sketcher);
				readShape.init_color(new Color(255, 0, 0));
				// this adds a new shape for the server to render
				inputShapeList.add(readShape);
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

	public ClientResponseHandler(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			int iter = 0;
			while (true) {
				// send a counter to the client every two seconds
				outputStream.writeInt(iter++);
				Thread.sleep(1000000);
			}
		} catch (IOException | InterruptedException e) {
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

	CopyOnWriteArrayList<Shape> inputShapeList;
	PApplet sketcher;

	public ClientConnectionHandler(CopyOnWriteArrayList<Shape> inputShapeList, PApplet sketcher) {
		// TODO Auto-generated constructor stub
		this.inputShapeList = inputShapeList;
		this.sketcher = sketcher;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket sSocket;
		try {
			sSocket = new ServerSocket(15001);
			System.out.println("server listening at "+sSocket.getLocalPort());
			while (true) {
				Socket socket = sSocket.accept();
				System.out.println("Client Connected to : " + socket.getPort());
				// start a new thread for handling requests from the client
				ClientRequestHandler requestHandler = new ClientRequestHandler(socket, inputShapeList, sketcher);
				new Thread(requestHandler).start();
				// start a new thread for handling responses for the client
				ClientResponseHandler responseHandler = new ClientResponseHandler(socket);
				new Thread(responseHandler).start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

public class AsynchronousServer extends PApplet {

	CopyOnWriteArrayList<Shape> inputShapeList;

	@Override
	public void settings() {
		size(1024, 1024);
	}

	@Override
	public void setup() {
		// Setting up the Client Handler here that takes care of the
		inputShapeList = new CopyOnWriteArrayList<Shape>();
		// initializing the client handler which will give us the required shapes for
		// the renderer
		new Thread(new ClientConnectionHandler(inputShapeList, this)).start();

	}

	@Override
	public void draw() {
		background(100);
		for (Shape shape : inputShapeList) {
			shape.render();
		}
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		AsynchronousServer mySketch = new AsynchronousServer();
		PApplet.runSketch(processingArgs, mySketch);
	}

}
