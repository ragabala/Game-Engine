package com.hw1.networks;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import com.hw1.sketcher.Color;
import com.hw1.sketcher.Rectangle;
import com.hw1.sketcher.Shape;

/**
 * @author ragbalak
 * ShapeSender class is a Thread impl, which takes of 
 * sending Game objects to the server from the client 
 * for every 2 secs.
 */
class ShapeSender implements Runnable {

	Socket socket;
	Random randGen;

	public ShapeSender(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	/**
	 * @param values
	 * This function takes care of randomizing the rectangle values.
	 * The values generated are 
	 *  1. Length
	 *  2. Breadth
	 *  3. Pos_x
	 *  4. Pox_y
	 */
	public void collectRectData(int[] values) {
		// side lengths : 50 - 100
		values[0] = randGen.nextInt(50) + 50;
		values[1] = values[0] + randGen.nextInt(30);

		// pos x,y = 100 - 1000
		values[2] = randGen.nextInt(901) + 100;
		values[3] = randGen.nextInt(901) + 100;

	}

	@Override
	public void run() {
		ObjectOutputStream outputStream;
		randGen = new Random();
		Color color = new Color(randGen.nextInt(256), randGen.nextInt(256), randGen.nextInt(256));
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			while (true) {
				int rectData[] = new int[4];
				collectRectData(rectData);
				// the Papplet component of the shape will be set in the server side
				Shape shape = new Rectangle(null, rectData[0], rectData[1], rectData[2], rectData[3], color);
				outputStream.writeObject(shape);
				// generate a shape every 2 secs
				Thread.sleep(2000);
			}
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
	}

}


/**
 * @author ragbalak
 * Clientreceiver class is another Thread Implementation
 * that takes care of receiving the counter values form the server.
 * This runs async with the Shape Sender.
 */
class ClientReceiver implements Runnable {
	Socket socket;

	public ClientReceiver(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			while (true) {
				System.out.println("From Server : " + inputStream.readInt());
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

}


/**
 * @author ragbalak
 * The main client thread that spawns the sender and receiver threads.
 */
public class AsynchronousClient {

	public static void main(String[] args) throws IOException, InterruptedException {
		Socket socket = new Socket("127.0.0.1", 15001);
		System.out.println("Client to server socket established");
		Thread sender = new Thread(new ShapeSender(socket));
		Thread receiver = new Thread(new ClientReceiver(socket));
		sender.start();
		receiver.start();
		sender.join();
		receiver.join();

	}
}
